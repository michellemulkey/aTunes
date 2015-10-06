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

package net.sourceforge.atunes.kernel.controllers.navigation.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.gui.model.NavigationTableModel;
import net.sourceforge.atunes.gui.views.dialogs.CopyProgressDialog;
import net.sourceforge.atunes.gui.views.dialogs.SearchDialog;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.executors.ImportProcess;
import net.sourceforge.atunes.kernel.executors.processes.ExportFilesProcess;
import net.sourceforge.atunes.kernel.handlers.SearchInternetHandler;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.writer.TagModifier;
import net.sourceforge.atunes.kernel.utils.PictureExporter;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.model.info.TreeObject;
import net.sourceforge.atunes.model.search.Search;

import org.apache.log4j.Logger;


public class NavigationMenusActionListener implements ActionListener {
	
	private Logger logger = Logger.getLogger(NavigationMenusActionListener.class);
	
	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationMenusActionListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == panel.getAddToPlaylistMenuItem()) {
			if (controller.getState().getPopupmenuCaller() == panel.getFavoritesTree()) {
				TreePath[] paths = panel.getFavoritesTree().getSelectionPaths();
				if (paths != null) {
					ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
					for (int i = 0; i < paths.length; i++) {
						songs.addAll(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs());
					}
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
				}
			}
			else {
				int[] rows = panel.getNavigationTable().getSelectedRows();
				if (rows.length > 0) {
					ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
					for (int i = 0; i < rows.length; i++) {
						songs.add(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(rows[i]));
					}
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
				}
			}
		}
		else if (e.getSource() == panel.getSetAsPlaylistMenuItem()) {
			HandlerProxy.getPlayListHandler().clearList();
			if (controller.getState().getPopupmenuCaller() == panel.getFavoritesTree()) {
				TreePath[] paths = panel.getFavoritesTree().getSelectionPaths();
				if (paths != null) {
					ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
					for (int i = 0; i < paths.length; i++) {
						songs.addAll(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs());
					}
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
				}
			}
			else {
				int[] rows = panel.getNavigationTable().getSelectedRows();
				if (rows.length > 0) {
					ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
					for (int i = 0; i < rows.length; i++) {
						songs.add(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(rows[i]));
					}
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
				}
			}

		}
		else if (e.getSource() == panel.getRemoveFromFavoritesMenuItem()) {
			if (controller.getState().getPopupmenuCaller() == panel.getFavoritesTree()) {
				TreePath[] paths = panel.getFavoritesTree().getSelectionPaths();
				if (paths != null) {
					for (int i = 0; i < paths.length; i++) {
						TreeObject obj = (TreeObject) ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
						controller.getRepositoryHandler().removeFromFavorites(obj);
					}
				}
			}
			else {
				int[] rows = panel.getNavigationTable().getSelectedRows();
				if (rows.length > 0) {
					for (int i = 0; i < rows.length; i++) {
						AudioFile file = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(rows[i]);
						controller.getRepositoryHandler().removeSongFromFavorites(file);
					}
				}
			}
			controller.refreshFavoriteTree();
		}
		else if (e.getSource() == panel.getNonFavoriteAddToPlaylistMenuItem()) {
			if (controller.getState().getPopupmenuCaller() == panel.getNavigationTable()) {
				int[] rows = panel.getNavigationTable().getSelectedRows();
				if (rows.length > 0) {
					ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
					for (int i = 0; i < rows.length; i++) {
						songs.add(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(rows[i]));
					}
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
				}
			}
			else {
				TreePath[] paths = ((JTree)controller.getState().getPopupmenuCaller()).getSelectionPaths();
				if (paths != null) {
					ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
					for (int i = 0; i < paths.length; i++) {
						TreeObject obj = (TreeObject) ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
						songs.addAll(obj.getSongs());
					}
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
				}
			}
		}
		else if (e.getSource() == panel.getNonFavoriteSetAsPlaylistMenuItem()) {
			HandlerProxy.getPlayListHandler().clearList();
			if (controller.getState().getPopupmenuCaller() == panel.getNavigationTable()) {
				int[] rows = panel.getNavigationTable().getSelectedRows();
				if (rows.length > 0) {
					ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
					for (int i = 0; i < rows.length; i++) {
						songs.add(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(rows[i]));
					}
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
				}
			}
			else {
				TreePath[] paths = ((JTree)controller.getState().getPopupmenuCaller()).getSelectionPaths();
				if (paths != null) {
					HandlerProxy.getPlayListHandler().addToPlayList(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs());
				}
			}
		}
		else if (e.getSource() == panel.getNonFavoriteSetAsFavoriteSongMenuItem()) {
			int[] rows = panel.getNavigationTable().getSelectedRows();
			if (rows.length > 0) {
				ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
				for (int i = 0; i < rows.length; i++) {
					songs.add(((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(rows[i]));
				}
				controller.getRepositoryHandler().addFavoriteSongs(songs);
			}
		}
		else if (e.getSource() == panel.getNonFavoriteSetAsFavoriteAlbumMenuItem()) {
			TreePath[] paths = ((JTree)controller.getState().getPopupmenuCaller()).getSelectionPaths();
			if (paths != null) {
				ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
				for (int i = 0; i < paths.length; i++) {
					Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
					if (obj instanceof TreeObject)
						songs.addAll(((TreeObject)obj).getSongs());
				}
				controller.getRepositoryHandler().addFavoriteAlbums(songs);
			}
		}
		else if (e.getSource() == panel.getNonFavoriteSetAsFavoriteArtistMenuItem()) {
			TreePath[] paths = ((JTree)controller.getState().getPopupmenuCaller()).getSelectionPaths();
			if (paths != null) {
				ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
				for (int i = 0; i < paths.length; i++) {
					Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
					if (obj instanceof TreeObject)
						songs.addAll(((TreeObject)obj).getSongs());
				}
				controller.getRepositoryHandler().addFavoriteArtists(songs);
			}
		}
		else if (e.getSource() == panel.getNonFavoriteEditTagMenuItem()) {
			if (controller.getState().getPopupmenuCaller() == panel.getNavigationTable()) {
				int[] rows = panel.getNavigationTable().getSelectedRows();
				ArrayList<AudioFile> files = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongsAt(rows);
				HandlerProxy.getControllerHandler().getEditTagDialogController().editFiles(files);
			}
			else if (controller.getState().getPopupmenuCaller() == panel.getNavigationTree()) {
				TreePath path = panel.getNavigationTree().getSelectionPath();
				if (((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof TreeObject) {
					ArrayList<AudioFile> files = ((TreeObject)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject()).getSongs();
					HandlerProxy.getControllerHandler().getEditTagDialogController().editFiles(files);
				}
			}
			else if (controller.getState().getPopupmenuCaller() == panel.getFileNavigationTree()) {
				TreePath path = panel.getFileNavigationTree().getSelectionPath();
				if (((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof TreeObject) {
					ArrayList<AudioFile> files = ((TreeObject)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject()).getSongs();
					HandlerProxy.getControllerHandler().getEditTagDialogController().editFiles(files);
				}
			}
		}
		else if (e.getSource() == panel.getNonFavoriteEditTitlesMenuItem()) {
			TreePath path = panel.getNavigationTree().getSelectionPath();
			Album a = (Album)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
			HandlerProxy.getControllerHandler().getEditTitlesDialogController().editFiles(a);
		}
		else if (e.getSource() == panel.getNonFavoriteClearTagMenuItem()) {
			if (controller.getState().getPopupmenuCaller() == panel.getNavigationTable()) {
				int[] rows = panel.getNavigationTable().getSelectedRows();
				ArrayList files = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongsAt(rows);
				for (int i = 0; i < files.size(); i++) {
					AudioFile file = (AudioFile)files.get(i);
					TagModifier.deleteTags(file);
				}
				TagModifier.refreshAfterTagModify(files);
			}
			else if (controller.getState().getPopupmenuCaller() == panel.getNavigationTree()) {
				TreePath path = panel.getNavigationTree().getSelectionPath();
				if (((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof TreeObject) {
					ArrayList files = ((TreeObject)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject()).getSongs();
    				for (int i = 0; i < files.size(); i++) {
    					AudioFile file = (AudioFile)files.get(i);
    					TagModifier.deleteTags(file);
    				}
    				TagModifier.refreshAfterTagModify(files);
				}
			}
			else if (controller.getState().getPopupmenuCaller() == panel.getFileNavigationTree()) {
				TreePath path = panel.getFileNavigationTree().getSelectionPath();
				if (((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof TreeObject) {
					ArrayList files = ((TreeObject)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject()).getSongs();
    				for (int i = 0; i < files.size(); i++) {
    					AudioFile file = (AudioFile)files.get(i);
    					TagModifier.deleteTags(file);
    				}
    				TagModifier.refreshAfterTagModify(files);
				}
			}
		}
		else if (e.getSource() == panel.getNonFavoriteExtractPictureMenuItem()) {
			AudioFile file = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(panel.getNavigationTable().getSelectedRow());
			try {
				PictureExporter.exportPicture(file);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		}
		else if (e.getSource() == panel.getDeviceCopyToRepositoryMenuItem()) {
			ArrayList songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs();
			int filesToExport = songs.size();
			CopyProgressDialog copyProgressDialog = HandlerProxy.getVisualHandler().getCopyProgressDialog();
			copyProgressDialog.getTotalFilesLabel().setText(" / " + filesToExport);
			copyProgressDialog.getProgressBar().setMaximum(filesToExport);
			copyProgressDialog.setVisible(true);
			final ImportProcess importer = new ImportProcess(songs, ExportFilesProcess.FULL_STRUCTURE, null);
			copyProgressDialog.getCancelButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					importer.notifyCancel();
				}
			});
			importer.start();
		}
		else if (e.getSource() == panel.getNonFavoriteSearch()) {
			Search defaultSearch = Kernel.getInstance().state.getDefaultSearch(); 
			if (defaultSearch != null) {
				if (controller.getState().getPopupmenuCaller() == panel.getNavigationTree()) {
					TreePath path = panel.getNavigationTree().getSelectionPath();
					if (((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof Artist) {
						Artist a = (Artist) ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
						SearchInternetHandler.openSearch(defaultSearch, a.getName());
					}
				}
			}
			else {
				SearchDialog dialog = HandlerProxy.getVisualHandler().getSearchDialog();
				Search selectedSearch = openSearchDialog(dialog, false);
				if (selectedSearch != null)
					Kernel.getInstance().state.setDefaultSearch(selectedSearch);
			}
		}
		else if (e.getSource() == panel.getNonFavoriteSearchAt()) {
			SearchDialog dialog = HandlerProxy.getVisualHandler().getSearchDialog();
			Search s = openSearchDialog(dialog, true);
			if (dialog.isSetAsDefault() && s != null)
				Kernel.getInstance().state.setDefaultSearch(s);
			
		}
	}
	
	private Search openSearchDialog(SearchDialog dialog, boolean setAsDefaultVisible) {
		dialog.setSetAsDefaultVisible(setAsDefaultVisible);
		dialog.setVisible(true);
		Search s = dialog.getResult();
		if (controller.getState().getPopupmenuCaller() == panel.getNavigationTree()) {
			TreePath path = panel.getNavigationTree().getSelectionPath();
			if (((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof Artist) {
				Artist a = (Artist) ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
				SearchInternetHandler.openSearch(s, a.getName());
			}
		}
		return s;
	}
}
