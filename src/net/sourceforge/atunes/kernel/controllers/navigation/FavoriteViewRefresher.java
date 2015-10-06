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

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class FavoriteViewRefresher {

	public static void refresh(JTree tree, HashMap<String, Artist> artists, HashMap<String, Album> albums, DefaultTreeModel treeModel) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
		root.setUserObject(LanguageTool.getString("FAVORITES"));
		root.removeAllChildren();
		
		DefaultMutableTreeNode artistsNode = new DefaultMutableTreeNode();
		artistsNode.setUserObject(LanguageTool.getString("ARTISTS"));
		addArtistNodes(artistsNode, artists);
		root.add(artistsNode);
		
		DefaultMutableTreeNode albumsNode = new DefaultMutableTreeNode();
		albumsNode.setUserObject(LanguageTool.getString("ALBUMS"));
		addAlbumNodes(albumsNode, albums);
		root.add(albumsNode);
		
		DefaultMutableTreeNode songsNode = new DefaultMutableTreeNode();
		songsNode.setUserObject(LanguageTool.getString("SONGS"));
		root.add(songsNode);
		
		treeModel.reload();

		tree.expandPath(new TreePath(artistsNode.getPath()));
		tree.expandPath(new TreePath(albumsNode.getPath()));
	}
	
	private static void addArtistNodes(DefaultMutableTreeNode root, HashMap<String, Artist> artists) {
		ArrayList<String> artistNamesList = new ArrayList<String>(artists.keySet());
		Collections.sort(artistNamesList);
				
		for (int i = 0; i < artistNamesList.size(); i++) {
			Artist artist = artists.get(artistNamesList.get(i));
			DefaultMutableTreeNode artistNode = new DefaultMutableTreeNode(artist);
			addAlbumNodes(artistNode, artist.getAlbums());
			root.add(artistNode);
		}
	}
	
	private static void addAlbumNodes(DefaultMutableTreeNode root, HashMap<String, Album> albums) {
		ArrayList<String> albumsNamesList = new ArrayList<String>(albums.keySet());
		Collections.sort(albumsNamesList);
				
		for (int i = 0; i < albumsNamesList.size(); i++) {
			Album album = albums.get(albumsNamesList.get(i));
			DefaultMutableTreeNode albumNode = new DefaultMutableTreeNode(album);
			root.add(albumNode);
		}
	}

}
