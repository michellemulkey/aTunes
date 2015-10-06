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

package net.sourceforge.atunes.kernel.controllers.playListControls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sourceforge.atunes.gui.model.PlayListTableModel;
import net.sourceforge.atunes.gui.views.panels.PlayListControlsPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;


public class PlayListControlsListener implements ActionListener {
	
	private PlayListControlsPanel panel;
	
	public PlayListControlsListener(PlayListControlsPanel panel) {
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(panel.getSortByTrack()))
			HandlerProxy.getPlayListHandler().sortPlaylistByTrack();
		else if (e.getSource().equals(panel.getSortByTitle()))
			HandlerProxy.getPlayListHandler().sortPlaylistByTitle();
		else if (e.getSource().equals(panel.getSortByArtist()))
			HandlerProxy.getPlayListHandler().sortPlaylistByArtist();
		else if (e.getSource().equals(panel.getSortByAlbum()))
			HandlerProxy.getPlayListHandler().sortPlaylistByAlbum();
		else if (e.getSource().equals(panel.getSortByGenre()))
			HandlerProxy.getPlayListHandler().sortPlaylistByGenre();
		else if (e.getSource().equals(panel.getSavePlaylistButton()))
			HandlerProxy.getPlayListHandler().savePlaylist();
		else if (e.getSource().equals(panel.getLoadPlaylistButton()))
			HandlerProxy.getPlayListHandler().loadPlaylist();
		else if (e.getSource().equals(panel.getTopButton()))
			HandlerProxy.getControllerHandler().getPlayListController().moveToTop();
		else if (e.getSource().equals(panel.getUpButton()))
			HandlerProxy.getControllerHandler().getPlayListController().moveUp();
		else if (e.getSource().equals(panel.getDeleteButton()))
			HandlerProxy.getControllerHandler().getPlayListController().deleteSelection();
		else if (e.getSource().equals(panel.getDownButton()))
			HandlerProxy.getControllerHandler().getPlayListController().moveDown();
		else if (e.getSource().equals(panel.getBottomButton()))
			HandlerProxy.getControllerHandler().getPlayListController().moveToBottom();
		else if (e.getSource().equals(panel.getInfoButton()))
			HandlerProxy.getVisualHandler().showInfo();				
		else if (e.getSource().equals(panel.getClearButton()))
			HandlerProxy.getPlayListHandler().clearList();
		else if (e.getSource().equals(panel.getFavoriteSong()))
			HandlerProxy.getControllerHandler().getPlayListController().setAsFavoriteSongs(HandlerProxy.getControllerHandler().getPlayListController().getSelectedSongs());
		else if (e.getSource().equals(panel.getFavoriteAlbum()))
			HandlerProxy.getControllerHandler().getPlayListController().setAsFavoriteAlbums(HandlerProxy.getControllerHandler().getPlayListController().getSelectedSongs());
		else if (e.getSource().equals(panel.getFavoriteArtist()))
			HandlerProxy.getControllerHandler().getPlayListController().setAsFavoriteArtists(HandlerProxy.getControllerHandler().getPlayListController().getSelectedSongs());
		else if (e.getSource().equals(panel.getShowTrack())) {
			PlayListTableModel model = HandlerProxy.getVisualHandler().getPlayListTableModel();
			model.setTrackVisible(panel.getShowTrack().isSelected());
			Kernel.getInstance().state.setShowTrackInPlayList(panel.getShowTrack().isSelected());
			HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
			HandlerProxy.getControllerHandler().getPlayListFilterController().reapplyFilter();
		}
		else if (e.getSource().equals(panel.getShowArtist())) {
			PlayListTableModel model = HandlerProxy.getVisualHandler().getPlayListTableModel();
			model.setArtistVisible(panel.getShowArtist().isSelected());
			Kernel.getInstance().state.setShowArtistInPlayList(panel.getShowArtist().isSelected());
			HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
			HandlerProxy.getControllerHandler().getPlayListFilterController().reapplyFilter();
		}
		else if (e.getSource().equals(panel.getShowAlbum())) {
			PlayListTableModel model = HandlerProxy.getVisualHandler().getPlayListTableModel();
			model.setAlbumVisible(panel.getShowAlbum().isSelected());
			Kernel.getInstance().state.setShowAlbumInPlayList(panel.getShowAlbum().isSelected());
			HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
			HandlerProxy.getControllerHandler().getPlayListFilterController().reapplyFilter();
		}
		else if (e.getSource().equals(panel.getShowGenre())) {
			PlayListTableModel model = HandlerProxy.getVisualHandler().getPlayListTableModel();
			model.setGenreVisible(panel.getShowGenre().isSelected());
			Kernel.getInstance().state.setShowGenreInPlayList(panel.getShowGenre().isSelected());
			HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
			HandlerProxy.getControllerHandler().getPlayListFilterController().reapplyFilter();
		}
		else if (e.getSource().equals(panel.getArtistButton()))
			HandlerProxy.getControllerHandler().getPlayListController().setArtistAsPlaylist();
		else if (e.getSource().equals(panel.getAlbumButton()))
			HandlerProxy.getControllerHandler().getPlayListController().setAlbumAsPlaylist();
	}
}
