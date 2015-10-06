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

package net.sourceforge.atunes.kernel.controllers.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sourceforge.atunes.gui.views.menu.ApplicationMenuBar;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationControllerViews;
import net.sourceforge.atunes.kernel.modules.updates.ApplicationUpdates;


public class AppMenuListener implements ActionListener {

	private ApplicationMenuBar menu;
	
	protected AppMenuListener(ApplicationMenuBar menu) {
		this.menu = menu;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(menu.getSelectRepository()))
			HandlerProxy.getRepositoryHandler().selectRepository();
		else if (e.getSource().equals(menu.getRefreshRepository()))
			HandlerProxy.getRepositoryHandler().refreshRepository();
		else if (e.getSource().equals(menu.getExit())) {
			HandlerProxy.getVisualHandler().setFullFrameVisible(false);
			Kernel.getInstance().finish();
		}
		else if (e.getSource().equals(menu.getEditPreferences()))
			HandlerProxy.getControllerHandler().getEditPreferencesDialogController().start();
		else if (e.getSource().equals(menu.getTagView())) 
			HandlerProxy.getControllerHandler().getNavigationController().setNavigationView(NavigationControllerViews.TAG_VIEW);
		else if (e.getSource().equals(menu.getFolderView()))
			HandlerProxy.getControllerHandler().getNavigationController().setNavigationView(NavigationControllerViews.FILE_VIEW);
		else if (e.getSource().equals(menu.getFavoriteView()))
			HandlerProxy.getControllerHandler().getNavigationController().setNavigationView(NavigationControllerViews.FAVORITE_VIEW);
		else if (e.getSource().equals(menu.getDeviceView()))
			HandlerProxy.getControllerHandler().getNavigationController().setNavigationView(NavigationControllerViews.DEVICE_VIEW);
		else if (e.getSource().equals(menu.getShowStatusBar()))
			HandlerProxy.getVisualHandler().showStatusBar(menu.getShowStatusBar().isSelected());
		else if (e.getSource().equals(menu.getShowNavigationPanel()))
			HandlerProxy.getVisualHandler().showNavigationPanel(menu.getShowNavigationPanel().isSelected(), true);
		else if (e.getSource().equals(menu.getShowNavigationTable()))
			HandlerProxy.getVisualHandler().showNavigationTable(menu.getShowNavigationTable().isSelected());
		else if (e.getSource().equals(menu.getShowProperties()))
			HandlerProxy.getVisualHandler().showSongProperties(menu.getShowProperties().isSelected(), true);
		else if (e.getSource().equals(menu.getShowOSD())) {
			Kernel.getInstance().state.setShowOSD(menu.getShowOSD().isSelected());
			HandlerProxy.getSystemTrayHandler().setShowOSD(menu.getShowOSD().isSelected());
		}
		else if (e.getSource().equals(menu.getVolumeDown()))
			HandlerProxy.getPlayerHandler().volumeDown();
		else if (e.getSource().equals(menu.getVolumeUp()))
			HandlerProxy.getPlayerHandler().volumeUp();
		else if (e.getSource().equals(menu.getMute()))
			HandlerProxy.getControllerHandler().getPlayerControlsController().setMute(!HandlerProxy.getPlayerHandler().isMute());
		else if (e.getSource().equals(menu.getPlayListPlay()))
			HandlerProxy.getControllerHandler().getPlayListController().playSelectedSong();
		else if (e.getSource().equals(menu.getPlayListEditTag()))
			HandlerProxy.getPlayListHandler().editTags();
		else if (e.getSource().equals(menu.getPlayListAutoSetTrack()))
			HandlerProxy.getControllerHandler().getPlayListController().setTrackNumber();
		else if (e.getSource().equals(menu.getPlayListAutoSetGenre()))
			HandlerProxy.getControllerHandler().getPlayListController().setGenre();
		else if (e.getSource().equals(menu.getPlayListSave()))
			HandlerProxy.getPlayListHandler().savePlaylist();
		else if (e.getSource().equals(menu.getPlayListLoad()))
			HandlerProxy.getPlayListHandler().loadPlaylist();
		else if (e.getSource().equals(menu.getPlayListFilter()))
			HandlerProxy.getVisualHandler().showFilter(true);
		else if (e.getSource().equals(menu.getPlayListInfo()))
			HandlerProxy.getVisualHandler().showInfo();
		else if (e.getSource().equals(menu.getPlayListDelete()))
			HandlerProxy.getControllerHandler().getPlayListController().deleteSelection();
		else if (e.getSource().equals(menu.getPlayListClear()))
			HandlerProxy.getPlayListHandler().clearList();
		else if (e.getSource().equals(menu.getPlayListTop()))
			HandlerProxy.getControllerHandler().getPlayListController().moveToTop();
		else if (e.getSource().equals(menu.getPlayListUp()))
			HandlerProxy.getControllerHandler().getPlayListController().moveUp();
		else if (e.getSource().equals(menu.getPlayListDown()))
			HandlerProxy.getControllerHandler().getPlayListController().moveDown();
		else if (e.getSource().equals(menu.getPlayListBottom()))
			HandlerProxy.getControllerHandler().getPlayListController().moveToBottom();
		else if (e.getSource().equals(menu.getPlayListFavoriteSong()))
			HandlerProxy.getControllerHandler().getPlayListController().setAsFavoriteSongs(HandlerProxy.getControllerHandler().getPlayListController().getSelectedSongs());
		else if (e.getSource().equals(menu.getPlayListFavoriteAlbum()))
			HandlerProxy.getControllerHandler().getPlayListController().setAsFavoriteAlbums(HandlerProxy.getControllerHandler().getPlayListController().getSelectedSongs());
		else if (e.getSource().equals(menu.getPlayListFavoriteArtist()))
			HandlerProxy.getControllerHandler().getPlayListController().setAsFavoriteArtists(HandlerProxy.getControllerHandler().getPlayListController().getSelectedSongs());
		else if (e.getSource().equals(menu.getPlayListArtist()))
			HandlerProxy.getControllerHandler().getPlayListController().setArtistAsPlaylist();
		else if (e.getSource().equals(menu.getPlayListAlbum()))
			HandlerProxy.getControllerHandler().getPlayListController().setAlbumAsPlaylist();
		else if (e.getSource().equals(menu.getPlayListShowButtons()))
			HandlerProxy.getControllerHandler().getPlayListController().showPlaylistControls(menu.getPlayListShowButtons().isSelected());
		else if (e.getSource().equals(menu.getDeviceConnect()))
			HandlerProxy.getRepositoryHandler().connectDevice();
		else if (e.getSource().equals(menu.getDeviceRefresh()))
			HandlerProxy.getRepositoryHandler().refreshDevice();
		else if (e.getSource().equals(menu.getDeviceDisconnect()))
			HandlerProxy.getRepositoryHandler().disconnectDevice();
		else if (e.getSource().equals(menu.getDeviceViewByTag())) {
			Kernel.getInstance().state.setSortDeviceByTag(true);
			HandlerProxy.getControllerHandler().getNavigationController().refreshDeviceTreeContent();
		}
		else if (e.getSource().equals(menu.getDeviceViewByFolder())) {
			Kernel.getInstance().state.setSortDeviceByTag(false);
			HandlerProxy.getControllerHandler().getNavigationController().refreshDeviceTreeContent();
		}
		else if (e.getSource().equals(menu.getToolsExport()))
			HandlerProxy.getControllerHandler().getExportOptionsController().beginExportProcess();
		else if (e.getSource().equals(menu.getRipCd()))
			HandlerProxy.getRipperHandler().startCdRipper();
		else if (e.getSource().equals(menu.getStats()))
			HandlerProxy.getControllerHandler().getStatsDialogController().showStats();
		else if (e.getSource().equals(menu.getCheckUpdates()))
			ApplicationUpdates.checkUpdates(Kernel.getInstance().state.getProxy());
		else if (e.getSource().equals(menu.getAboutItem()))
			HandlerProxy.getVisualHandler().showAboutDialog();
	}
}
