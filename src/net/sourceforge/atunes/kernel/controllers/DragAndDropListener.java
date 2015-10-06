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

package net.sourceforge.atunes.kernel.controllers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.atunes.gui.model.TransferableArrayList;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.handlers.PlayListHandler;
import net.sourceforge.atunes.kernel.modules.repository.RepositoryLoader;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.TreeObject;
import net.sourceforge.atunes.utils.CollectionUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class DragAndDropListener implements DropTargetListener{
	
	DropTarget dropTarget;
	DropTarget dropTarget2;
	
	public DragAndDropListener() {
		dropTarget = new DropTarget(HandlerProxy.getControllerHandler().getPlayListController().getMainPlayListScrollPane(), this);
		dropTarget2 = new DropTarget(HandlerProxy.getControllerHandler().getPlayListController().getMainPlayListTable(), this);
	}
	
	public void dragEnter(DropTargetDragEvent dtde) {}
	public void dragExit(DropTargetEvent dte) {}
	public void dragOver(DropTargetDragEvent dtde) {}
	
	public void drop(DropTargetDropEvent dtde) {
		Transferable transferable = dtde.getTransferable();
		DataFlavor aTunesFlavorAccepted = new TransferableArrayList(null).getTransferDataFlavors()[0];
		if(transferable.isDataFlavorSupported(aTunesFlavorAccepted)) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			try {
				ArrayList<AudioFile> songsToAdd = new ArrayList<AudioFile>();
				ArrayList listOfObjectsDragged = (ArrayList) transferable.getTransferData(aTunesFlavorAccepted);
				for (int i = 0; i < listOfObjectsDragged.size(); i++) {
					Object obj = listOfObjectsDragged.get(i);
					if (obj instanceof TreeObject) {
						TreeObject object = (TreeObject) obj; 
						songsToAdd.addAll(object.getSongs());
					}
					else if (obj instanceof Integer) {
						Integer row = (Integer) obj;
						songsToAdd.add(HandlerProxy.getControllerHandler().getNavigationController().getSongInNavigationTable(row));
					}
					else if (obj instanceof String) { // The root of the tree
						String str = (String) obj;
						if (str.equals(LanguageTool.getString("REPOSITORY")) || str.startsWith(LanguageTool.getString("FOLDERS")))
							songsToAdd.addAll(HandlerProxy.getRepositoryHandler().getSongs());
						else if (str.equals(LanguageTool.getString("FAVORITES")))
							songsToAdd.addAll(HandlerProxy.getRepositoryHandler().getFavoriteSongs());
						else if (str.equals(LanguageTool.getString("ARTISTS"))) {
							HashMap<AudioFile, AudioFile> songsMap = new HashMap<AudioFile, AudioFile>();
							songsMap.putAll(CollectionUtils.vector2HashMap(HandlerProxy.getRepositoryHandler().getSongsForArtists(HandlerProxy.getRepositoryHandler().getFavoriteArtistsInfo())));
							songsToAdd = new ArrayList<AudioFile>(songsMap.values());
						} else if (str.equals(LanguageTool.getString("ALBUMS"))) {
							HashMap<AudioFile, AudioFile> songsMap = new HashMap<AudioFile, AudioFile>();
							songsMap.putAll(CollectionUtils.vector2HashMap(HandlerProxy.getRepositoryHandler().getSongsForAlbums(HandlerProxy.getRepositoryHandler().getFavoriteAlbumsInfo())));
							songsToAdd = new ArrayList<AudioFile>(songsMap.values());
						} else if (str.equals(LanguageTool.getString("SONGS"))) {
							songsToAdd.addAll(HandlerProxy.getRepositoryHandler().getFavoriteSongsInfo().values());
						} else if (str.equals(LanguageTool.getString("DEVICE"))) {
							songsToAdd.addAll(HandlerProxy.getRepositoryHandler().getDeviceSongs());
						}
					}
				}
				ArrayList<AudioFile> songsSorted = HandlerProxy.getRepositoryHandler().sort(songsToAdd, HandlerProxy.getControllerHandler().getNavigationController().getState().getSortType());
				HandlerProxy.getPlayListHandler().addToPlayList(songsSorted);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			dtde.getDropTargetContext().dropComplete(true);
		}
		else {
			try {
				DataFlavor fileListFlavorAccepted = new DataFlavor("application/x-java-file-list; class=java.util.List");
				if (transferable.isDataFlavorSupported(fileListFlavorAccepted)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY);
					List list = (List) transferable.getTransferData(fileListFlavorAccepted);
					ArrayList<AudioFile> filesToAdd = new ArrayList<AudioFile>();
					for (int i = 0; i < list.size(); i++) {
						File f = (File) list.get(i);
						if (f.isDirectory()) {
							filesToAdd.addAll(RepositoryLoader.getSongsForDir(f));
						}
						else if (AudioFile.isValidAudioFile(f)) {
							AudioFile song = new AudioFile(f.getAbsolutePath());
							filesToAdd.add(song);
						}
						else if (f.getName().toLowerCase().endsWith("m3u")) {
							filesToAdd.addAll(PlayListHandler.getFilesFromList(f));
						}
					}
					HandlerProxy.getPlayListHandler().addToPlayList(filesToAdd);
					dtde.getDropTargetContext().dropComplete(true);
				}
				else
					dtde.rejectDrop();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void dropActionChanged(DropTargetDragEvent dtde) {}
}
