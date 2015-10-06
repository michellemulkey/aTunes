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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.model.info.TreeObject;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class NavigationTreeMouseListener extends MouseAdapter {
	
	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationTreeMouseListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
	}
		
	private boolean isNewRowSelection(JTree tree, MouseEvent e) {
		int[] rowsSelected = tree.getSelectionRows();
		int selected = tree.getRowForLocation(e.getX(), e.getY());
		boolean found = false;
		int i = 0;
		while (!found && i < rowsSelected.length) {
			if (rowsSelected[i] == selected)
				found = true;
			i++;
		}
		return !found;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == panel.getNavigationTree()) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				
				// BUG 1626896
				if (isNewRowSelection(panel.getNavigationTree(), e))
					panel.getNavigationTree().setSelectionRow(panel.getNavigationTree().getRowForLocation(e.getX(), e.getY()));
				// BUG 1626896
				
				controller.getState().setPopupmenuCaller(panel.getNavigationTree());
				panel.getNonFavoriteSetAsFavoriteSongMenuItem().setEnabled(false);
				panel.getNonFavoriteSetAsFavoriteAlbumMenuItem().setEnabled(true);
				panel.getNonFavoriteSetAsFavoriteArtistMenuItem().setEnabled(true);
				panel.getNonFavoriteEditTagMenuItem().setEnabled(!(panel.getNavigationTree().isRowSelected(0)) && panel.getNavigationTree().getSelectionCount() == 1);
				panel.getNonFavoriteClearTagMenuItem().setEnabled(!(panel.getNavigationTree().isRowSelected(0)) && panel.getNavigationTree().getSelectionCount() == 1);
				panel.getNonFavoriteExtractPictureMenuItem().setEnabled(false);
				TreePath path = panel.getNavigationTree().getSelectionPath();
				if (path != null) {
					Object obj = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
					panel.getNonFavoriteEditTitlesMenuItem().setEnabled(obj instanceof Album);
					panel.getNonFavoriteSearch().setEnabled(obj instanceof Artist && !(((Artist)obj).getName().equals(LanguageTool.getString("UNKNOWN_ARTIST"))));
					panel.getNonFavoriteSearchAt().setEnabled(obj instanceof Artist && !(((Artist)obj).getName().equals(LanguageTool.getString("UNKNOWN_ARTIST"))));
				}
				else {
					panel.getNonFavoriteEditTitlesMenuItem().setEnabled(false);
					panel.getNonFavoriteSearch().setEnabled(false);
					panel.getNonFavoriteSearchAt().setEnabled(false);
				}
				panel.getNonFavoriteMenu().show(controller.getState().getPopupmenuCaller(), e.getX(), e.getY());
			}
			else {
				int selRow = panel.getNavigationTree().getRowForLocation(e.getX(), e.getY());
				TreePath selPath = panel.getNavigationTree().getPathForLocation(e.getX(), e.getY());
				if(selRow != -1) {
					if(e.getClickCount() == 2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						ArrayList<AudioFile> songs = controller.getSongsForTreeNode(node);
						HandlerProxy.getPlayListHandler().addToPlayList(songs);
					}
				}
			}

		}
		else if (e.getSource() == panel.getFileNavigationTree()) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				
				//	BUG 1626896
				if (isNewRowSelection(panel.getFileNavigationTree(), e))
				panel.getFileNavigationTree().setSelectionRow(panel.getFileNavigationTree().getRowForLocation(e.getX(), e.getY()));
				//	 BUG 1626896
				
				controller.getState().setPopupmenuCaller(panel.getFileNavigationTree());
				panel.getNonFavoriteSetAsFavoriteSongMenuItem().setEnabled(false);
				panel.getNonFavoriteSetAsFavoriteAlbumMenuItem().setEnabled(true);
				panel.getNonFavoriteSetAsFavoriteArtistMenuItem().setEnabled(true);
				panel.getNonFavoriteEditTagMenuItem().setEnabled(!(panel.getFileNavigationTree().isRowSelected(0)) && panel.getFileNavigationTree().getSelectionCount() == 1);
				panel.getNonFavoriteEditTitlesMenuItem().setEnabled(false);
				panel.getNonFavoriteSearch().setEnabled(false);
				panel.getNonFavoriteSearchAt().setEnabled(false);
				panel.getNonFavoriteClearTagMenuItem().setEnabled(!(panel.getFileNavigationTree().isRowSelected(0)) && panel.getFileNavigationTree().getSelectionCount() == 1);
				panel.getNonFavoriteExtractPictureMenuItem().setEnabled(false);
				panel.getNonFavoriteMenu().show(controller.getState().getPopupmenuCaller(), e.getX(), e.getY());
			}
			else {
				int selRow = panel.getFileNavigationTree().getRowForLocation(e.getX(), e.getY());
				TreePath selPath = panel.getFileNavigationTree().getPathForLocation(e.getX(), e.getY());
				if(selRow != -1) {
					if(e.getClickCount() == 2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						ArrayList<AudioFile> songs = controller.getSongsForTreeNode(node);
						HandlerProxy.getPlayListHandler().addToPlayList(songs);
					}
				}
			}

		}
		else if (e.getSource() == panel.getFavoritesTree()) {
			if (e.getButton() == MouseEvent.BUTTON3) {

				//	BUG 1626896
				if (isNewRowSelection(panel.getFavoritesTree(), e))
					panel.getFavoritesTree().setSelectionRow(panel.getFavoritesTree().getRowForLocation(e.getX(), e.getY()));
				//	 BUG 1626896

				TreePath path = panel.getFavoritesTree().getSelectionPath();
				if (path != null) {
					Object obj = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
					if (obj instanceof Album) {
						Object[] objs = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObjectPath();
						boolean foundArtist = false;
						for (int i = 0; i < objs.length; i++) {
							if (objs[i] instanceof Artist) {
								foundArtist = true;
								break;
							}
						}
						panel.getRemoveFromFavoritesMenuItem().setEnabled(!foundArtist);
					} 
					else
						panel.getRemoveFromFavoritesMenuItem().setEnabled(obj instanceof TreeObject);
					controller.getState().setPopupmenuCaller(panel.getFavoritesTree());
					panel.getFavoriteMenu().show(panel.getFavoritesTree(), e.getX(), e.getY());
				}
			}
			else {
				int selRow = panel.getFavoritesTree().getRowForLocation(e.getX(), e.getY());
				TreePath selPath = panel.getFavoritesTree().getPathForLocation(e.getX(), e.getY());
				if(selRow != -1) {
					if(e.getClickCount() == 2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						ArrayList<AudioFile> songs = controller.getSongsForFavoriteTreeNode(node);
						HandlerProxy.getPlayListHandler().addToPlayList(songs);
					}
				}
			}
		}
		else if (e.getSource() == panel.getDeviceTree()) {
			if (e.getButton() == MouseEvent.BUTTON3) {

				//	BUG 1626896
				if (isNewRowSelection(panel.getDeviceTree(), e))
					panel.getDeviceTree().setSelectionRow(panel.getDeviceTree().getRowForLocation(e.getX(), e.getY()));
				//	 BUG 1626896

				if (controller.isDeviceAttached()) {
					controller.getState().setPopupmenuCaller(panel.getDeviceTree());
					panel.getDeviceMenu().show(controller.getState().getPopupmenuCaller(), e.getX(), e.getY());
				}
			}
			else {
				int selRow = panel.getDeviceTree().getRowForLocation(e.getX(), e.getY());
				TreePath selPath = panel.getDeviceTree().getPathForLocation(e.getX(), e.getY());
				if(selRow != -1) {
					if(e.getClickCount() == 2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						ArrayList<AudioFile> songs = controller.getSongsForDeviceTreeNode(node);
						HandlerProxy.getPlayListHandler().addToPlayList(songs);
					}
				}
			}
		}
	}
	
	public void mouseExited(MouseEvent arg0) {
		if (!Kernel.getInstance().state.SHOW_ALBUM_TOOLTIP)
			return;

		controller.setLastAlbumToolTipContent(null);
		controller.getAlbumToolTip().setVisible(false);
		controller.getAlbumToolTip().timer.stop();
	}
	
	

}
