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

package net.sourceforge.atunes.kernel.controllers.playList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.kernel.HandlerProxy;


public class PlayListListener extends MouseAdapter implements ActionListener, ListSelectionListener {

	private PlayListTable table;
	private PlayListController controller;
	
	protected PlayListListener(PlayListTable table, PlayListController controller) {
		this.table = table;
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(table.getPlayItem()))
			controller.playSelectedSong();
		else if (e.getSource().equals(table.getEditTagItem())) 
			HandlerProxy.getPlayListHandler().editTags();
		else if (e.getSource().equals(table.getAutoSetTrackNumberItem()))
			controller.setTrackNumber();
		else if (e.getSource().equals(table.getAutoSetGenreItem()))
			controller.setGenre();
		else if (e.getSource().equals(table.getSaveItem()))
			HandlerProxy.getPlayListHandler().savePlaylist();
		else if (e.getSource().equals(table.getLoadItem()))
			HandlerProxy.getPlayListHandler().loadPlaylist();
		else if (e.getSource().equals(table.getFilterItem()))
			HandlerProxy.getVisualHandler().showFilter(true);
		else if (e.getSource().equals(table.getTopItem()))
			controller.moveToTop();
		else if (e.getSource().equals(table.getUpItem())) 
			controller.moveUp();
		else if (e.getSource().equals(table.getDownItem()))
			controller.moveDown();
		else if (e.getSource().equals(table.getBottomItem()))
			controller.moveToBottom();
		else if (e.getSource().equals(table.getDeleteItem()))
			controller.deleteSelection();
		else if (e.getSource().equals(table.getInfoItem()))
			HandlerProxy.getVisualHandler().showInfo();
		else if (e.getSource().equals(table.getClearItem()))
			HandlerProxy.getPlayListHandler().clearList();
		else if (e.getSource().equals(table.getFavoriteSong()))
			controller.setAsFavoriteSongs(controller.getSelectedSongs());
		else if (e.getSource().equals(table.getFavoriteAlbum()))
			controller.setAsFavoriteAlbums(controller.getSelectedSongs());
		else if (e.getSource().equals(table.getFavoriteArtist()))
			controller.setAsFavoriteArtists(controller.getSelectedSongs());
		else if (e.getSource().equals(table.getArtistItem()))
			controller.setArtistAsPlaylist();
		else if (e.getSource().equals(table.getAlbumItem()))
			controller.setAlbumAsPlaylist();
		else if (e.getSource().equals(table.getShowControls()))
			controller.showPlaylistControls(table.getShowControls().isSelected());
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(table)) {
			if (e.getClickCount() == 2) {
				controller.playSelectedSong();
			}
			else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
				int[] currentlySelected = table.getSelectedRows();
				int selected = table.rowAtPoint(e.getPoint());
				boolean found = false;
				int i = 0;
				while (!found && i < currentlySelected.length) {
					if (currentlySelected[i] == selected)
						found = true;
					i++;
				}
				if (!found)
					table.getSelectionModel().setSelectionInterval(selected, selected);
				table.getMenu().show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		HandlerProxy.getControllerHandler().getMenuController().disablePlayListItems(lsm.isSelectionEmpty());
		controller.disablePlayListItems(lsm.isSelectionEmpty());
		HandlerProxy.getControllerHandler().getPlayListControlsController().disablePlayListControls(lsm.isSelectionEmpty());
	}

}
