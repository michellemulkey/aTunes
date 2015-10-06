/**
 * aTunes 1.6.0
 * Copyright (C) 2006-2007 Alex Aranda (fleax) alex.aranda@gmail.com
 *
 * http://www.atunes.org
 * http://sourceforge.net/projects/atunes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package net.sourceforge.atunes.kernel.controllers.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class DeviceViewRefresher {

	public static void refresh(HashMap<String, ?> data, DefaultTreeModel treeModel, boolean showArtist, String currentFilter, boolean isSortDeviceByTag) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
		root.setUserObject(LanguageTool.getString("DEVICE"));
		root.removeAllChildren();
		if (isSortDeviceByTag) {
			ArrayList<String> artistNamesList = new ArrayList<String>(data.keySet());
			Collections.sort(artistNamesList);
			HashMap<String, Album> albums = new HashMap<String, Album>();
			ArrayList<String> albumsList = new ArrayList<String>();
			if (showArtist) {
				for (int i = 0; i < artistNamesList.size(); i++) {
					Artist artist = (Artist) data.get(artistNamesList.get(i));
					DefaultMutableTreeNode artistNode = new DefaultMutableTreeNode(artist);
					ArrayList<String> albumNamesList = new ArrayList<String>(artist.getAlbums().keySet());
					if (currentFilter == null || artist.getName().toUpperCase().contains(currentFilter.toUpperCase())) {
						Collections.sort(albumNamesList);
						for (int j = 0; j < albumNamesList.size(); j++) {
							Album album = artist.getAlbums().get(albumNamesList.get(j));
							DefaultMutableTreeNode albumNode = new DefaultMutableTreeNode(album);
							artistNode.add(albumNode);
							root.add(artistNode);
						}
					}
				}
			}
			else {
				for (int i = 0; i < artistNamesList.size(); i++) {
					Artist artist = (Artist) data.get(artistNamesList.get(i));
					ArrayList<String> albumNamesList = new ArrayList<String>(artist.getAlbums().keySet());
					for (int j = 0; j < albumNamesList.size(); j++) {
						String a = albumNamesList.get(j);
						Album album = artist.getAlbums().get(a);
						if (currentFilter == null || album.getName().toUpperCase().contains(currentFilter.toUpperCase())) {
							albums.put(album.getName(), album);
							albumsList.add(album.getName());
						}
					}
				}
				Collections.sort(albumsList);
				for (int i = 0; i < albumsList.size(); i++) {
					Album a = albums.get(albumsList.get(i));
					DefaultMutableTreeNode albumNode = new DefaultMutableTreeNode(a);
					root.add(albumNode);
				}
			}
		}
		else {
			RefreshUtils.addFolderNodes(data, root, null);
		}
		treeModel.reload();
	}
}
