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
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.TreeObject;
import net.sourceforge.atunes.utils.CollectionUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class NodeToSongsTranslator {

	private NavigationControllerState state;
	
	protected NodeToSongsTranslator(NavigationControllerState state) {
		this.state = state;
	}
	
	protected ArrayList<AudioFile> getSongsForTreeNode(DefaultMutableTreeNode node) {
		ArrayList<AudioFile> songs = null;
		if (node.isRoot()) {
			if (state.getCurrentFilter() == null)
				songs = HandlerProxy.getRepositoryHandler().getSongs();
			else {
				songs = new ArrayList<AudioFile>();
				for (int i = 0; i < node.getChildCount(); i++) {
					TreeObject obj = (TreeObject) ((DefaultMutableTreeNode)node.getChildAt(i)).getUserObject();
					songs.addAll(obj.getSongs());
				}
			}
		} else {
			TreeObject obj = (TreeObject) node.getUserObject();
			songs = obj.getSongs();
		}
		ArrayList<AudioFile> result = HandlerProxy.getRepositoryHandler().sort(songs, state.getSortType());
		return result;
	}

	protected ArrayList<AudioFile> getSongsForFavoriteTreeNode(DefaultMutableTreeNode node) {
		HashMap<AudioFile, AudioFile> songsMap = new HashMap<AudioFile, AudioFile>();
		if (node.isRoot()) {
			songsMap.putAll(CollectionUtils.vector2HashMap(HandlerProxy.getRepositoryHandler().getSongsForArtists(HandlerProxy.getRepositoryHandler().getFavoriteArtistsInfo())));
			songsMap.putAll(CollectionUtils.vector2HashMap(HandlerProxy.getRepositoryHandler().getSongsForAlbums(HandlerProxy.getRepositoryHandler().getFavoriteAlbumsInfo())));
			songsMap.putAll(CollectionUtils.vector2HashMap(new ArrayList<AudioFile>(HandlerProxy.getRepositoryHandler().getFavoriteSongsInfo().values())));
		}
		else {
			if (node.getUserObject() instanceof TreeObject)
				songsMap = CollectionUtils.vector2HashMap(((TreeObject)node.getUserObject()).getSongs());
			else {
				if (node.getUserObject().toString().equals(LanguageTool.getString("ARTISTS"))) {
					songsMap.putAll(CollectionUtils.vector2HashMap(HandlerProxy.getRepositoryHandler().getSongsForArtists(HandlerProxy.getRepositoryHandler().getFavoriteArtistsInfo())));
				}
				else if (node.getUserObject().toString().equals(LanguageTool.getString("ALBUMS"))) {
					songsMap.putAll(CollectionUtils.vector2HashMap(HandlerProxy.getRepositoryHandler().getSongsForAlbums(HandlerProxy.getRepositoryHandler().getFavoriteAlbumsInfo())));
				}
				else {
					songsMap.putAll(CollectionUtils.vector2HashMap(new ArrayList<AudioFile>(HandlerProxy.getRepositoryHandler().getFavoriteSongsInfo().values())));
				}
			}
		}
		
		return HandlerProxy.getRepositoryHandler().sort(new ArrayList<AudioFile>(songsMap.values()), state.getSortType());
	}
	
	protected ArrayList<AudioFile> getSongsForDeviceTreeNode(DefaultMutableTreeNode node) {
		ArrayList<AudioFile> songs = null;
		if (node.isRoot()) {
			if (state.getCurrentFilter() == null)
				songs = HandlerProxy.getRepositoryHandler().getDeviceSongs();
			else {
				songs = new ArrayList<AudioFile>();
				for (int i = 0; i < node.getChildCount(); i++) {
					TreeObject obj = (TreeObject) ((DefaultMutableTreeNode)node.getChildAt(i)).getUserObject();
					songs.addAll(obj.getSongs());
				}
			}
		} else {
			TreeObject obj = (TreeObject) node.getUserObject();
			songs = obj.getSongs();
		}
		return HandlerProxy.getRepositoryHandler().sort(songs, state.getSortType());
	}

	protected ArrayList<AudioFile> getSongsForNavigationTree(TreePath[] paths) {
		if (paths != null) {
			ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
			for (int i = 0; i < paths.length; i++) {
				Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
				if (obj instanceof TreeObject)
					songs.addAll(((TreeObject)obj).getSongs());
				else { // if (((DefaultMutableTreeNode)paths[i].getLastPathComponent()).isRoot()) {
					songs.addAll(HandlerProxy.getRepositoryHandler().getSongs());
				}
			}
			return songs;
		}
		return new ArrayList<AudioFile>();
	}
}
