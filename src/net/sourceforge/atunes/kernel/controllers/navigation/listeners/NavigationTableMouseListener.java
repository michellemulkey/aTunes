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

import javax.swing.tree.DefaultMutableTreeNode;

import net.sourceforge.atunes.gui.model.NavigationTableModel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationControllerViews;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class NavigationTableMouseListener extends MouseAdapter {

	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationTableMouseListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
	}
	
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON3) {
			int[] rowsSelected = panel.getNavigationTable().getSelectedRows();
			int selected = panel.getNavigationTable().rowAtPoint(event.getPoint());
			boolean found = false;
			int i = 0;
			while (!found && i < rowsSelected.length) {
				if (rowsSelected[i] == selected)
					found = true;
				i++;
			}
			if (!found)
				panel.getNavigationTable().getSelectionModel().setSelectionInterval(selected, selected);
			panel.getNonFavoriteEditTitlesMenuItem().setEnabled(false);
			panel.getNonFavoriteSearchAt().setEnabled(false);
			if (panel.getNavigationTable().getSelectedRowCount() == 1) {
				AudioFile file = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(panel.getNavigationTable().getSelectedRow());
				panel.getNonFavoriteExtractPictureMenuItem().setEnabled(file.hasInternalPicture());
			}
			else {
				panel.getNonFavoriteExtractPictureMenuItem().setEnabled(false);
			}
			if (controller.getState().getNavigationView() == NavigationControllerViews.FAVORITE_VIEW && panel.getNavigationTable().getSelectedRowCount() > 0) {
				controller.getState().setPopupmenuCaller(panel.getNavigationTable());
				if (panel.getFavoritesTree().getSelectionCount() == 1) {
					Object obj = ((DefaultMutableTreeNode)panel.getFavoritesTree().getSelectionPath().getLastPathComponent()).getUserObject();
					if (obj instanceof String && obj.equals(LanguageTool.getString("SONGS"))) {
						panel.getRemoveFromFavoritesMenuItem().setEnabled(true);
					}
					else
						panel.getRemoveFromFavoritesMenuItem().setEnabled(false);
				}
				else
					panel.getRemoveFromFavoritesMenuItem().setEnabled(false);
				panel.getFavoriteMenu().show(controller.getState().getPopupmenuCaller(), event.getX(), event.getY());
			}
			else if (panel.getNavigationTable().getSelectedRowCount() > 0) {
				controller.getState().setPopupmenuCaller(panel.getNavigationTable());
				panel.getNonFavoriteSetAsFavoriteSongMenuItem().setEnabled(true);
				panel.getNonFavoriteSetAsFavoriteAlbumMenuItem().setEnabled(false);
				panel.getNonFavoriteSetAsFavoriteArtistMenuItem().setEnabled(false);
				panel.getNonFavoriteMenu().show(controller.getState().getPopupmenuCaller(), event.getX(), event.getY());
			}
		}
		else {
			if (event.getClickCount() == 2) {
				int[] selRow = panel.getNavigationTable().getSelectedRows();
				ArrayList<AudioFile> songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongsAt(selRow);
				if (songs != null && songs.size() >= 1) {
					HandlerProxy.getPlayListHandler().addToPlayList(songs);
					panel.getNavigationTableAddButton().setEnabled(true);
					panel.getNavigationTableInfoButton().setEnabled(true);
				}
			}
			else if (panel.getNavigationTable().getSelectedRowCount() > 0) {
				panel.getNavigationTableAddButton().setEnabled(true);
				panel.getNavigationTableInfoButton().setEnabled(true);
			}
		}
	}
}
