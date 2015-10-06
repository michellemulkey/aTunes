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

package net.sourceforge.atunes.kernel.modules.state;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

import net.sourceforge.atunes.gui.LookAndFeelSelector;
import net.sourceforge.atunes.kernel.modules.proxy.ProxyBean;
import net.sourceforge.atunes.model.search.Search;

import com.fleax.ant.BuildNumberReader;


public class ApplicationState implements Serializable {

	private static final long serialVersionUID = 4771296638947290335L;

	private boolean showNavigationPanel = true;
	private boolean showNavigationTable = true;
	private boolean showSongProperties;
	private boolean showStatusBar = true;
	private boolean showOSD;
	private boolean shuffle;
	private boolean repeat;
	private boolean sortDeviceByTag;
	private boolean showSystemTray;
	private boolean showTrayPlayer;
	private int navigationView;
	private String language;
	private Search defaultSearch;
	private boolean useAudioScrobbler = true;
	private boolean multipleWindow;
	private int version;
	private boolean showPlaylistControls = true;
	private ProxyBean proxy;
	private boolean showTrackInPlayList = true;
	private boolean showArtistInPlayList = true;
	private boolean showAlbumInPlayList = true;
	private boolean showGenreInPlayList = true;
	private String theme = LookAndFeelSelector.DEFAULT_THEME;
	
	// Constant properties editable in edit preferences
	public boolean SHOW_TITLE = true;
	public int OSD_DURATION = 2; // In seconds
	public int AUTO_REPOSITORY_REFRESH_TIME = 60; // In minutes
	public boolean ANIMATE_OSD = true;
	public boolean SHOW_FAVORITES_IN_NAVIGATOR = true;
	public boolean SAVE_PICTURE_FROM_AUDIO_SCROBBLER = true;
	public boolean SHOW_ALBUM_TOOLTIP = true;
	public int ALBUM_TOOLTIP_DELAY = 1; // In seconds

	
	// Next attributes are not always consistent since are changed when storing state
	private Point windowLocation;
	private boolean maximized;
	private Dimension windowSize;
	private Point multipleViewLocation;
	private Dimension mutipleViewSize;
	private int volume = 50;
	
	public ApplicationState() {
		version = BuildNumberReader.getBuildNumber();
	}
	
	public boolean isMaximized() {
		return maximized;
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public boolean isShowNavigationTable() {
		return showNavigationTable;
	}

	public void setShowNavigationTable(boolean showNavigationTable) {
		this.showNavigationTable = showNavigationTable;
	}

	public boolean isShowOSD() {
		return showOSD;
	}

	public void setShowOSD(boolean showOSD) {
		this.showOSD = showOSD;
	}

	public boolean isShowSongProperties() {
		return showSongProperties;
	}

	public void setShowSongProperties(boolean showSongProperties) {
		this.showSongProperties = showSongProperties;
	}

	public boolean isShuffle() {
		return shuffle;
	}

	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}

	public boolean isSortDeviceByTag() {
		return sortDeviceByTag;
	}

	public void setSortDeviceByTag(boolean sortDeviceByTag) {
		this.sortDeviceByTag = sortDeviceByTag;
	}

	public boolean isShowSystemTray() {
		return showSystemTray;
	}

	public void setShowSystemTray(boolean showSystemTray) {
		this.showSystemTray = showSystemTray;
	}

	public boolean isShowTrayPlayer() {
		return showTrayPlayer;
	}

	public void setShowTrayPlayer(boolean showTrayPlayer) {
		this.showTrayPlayer = showTrayPlayer;
	}


	public Point getWindowLocation() {
		return windowLocation;
	}


	public void setWindowLocation(Point windowLocation) {
		this.windowLocation = windowLocation;
	}

	public int isNavigationView() {
		return navigationView;
	}

	public void setNavigationView(int navigationView) {
		this.navigationView = navigationView;
	}

	public int getNavigationView() {
		return navigationView;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Search getDefaultSearch() {
		return defaultSearch;
	}

	public void setDefaultSearch(Search defaultSearch) {
		this.defaultSearch = defaultSearch;
	}

	public boolean isShowNavigationPanel() {
		return showNavigationPanel;
	}

	public void setShowNavigationPanel(boolean showNavigationPanel) {
		this.showNavigationPanel = showNavigationPanel;
	}

	public boolean isMultipleWindow() {
		return multipleWindow;
	}

	public void setMultipleWindow(boolean multipleWindow) {
		this.multipleWindow = multipleWindow;
	}

	public int getVersion() {
		return version;
	}

	public Dimension getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(Dimension windowSize) {
		this.windowSize = windowSize;
	}

	public boolean isShowPlaylistControls() {
		return showPlaylistControls;
	}

	public void setShowPlaylistControls(boolean showPlaylistControls) {
		this.showPlaylistControls = showPlaylistControls;
	}

	public boolean isUseAudioScrobbler() {
		return useAudioScrobbler;
	}

	public void setUseAudioScrobbler(boolean useAudioScrobbler) {
		this.useAudioScrobbler = useAudioScrobbler;
	}

	public ProxyBean getProxy() {
		return proxy;
	}

	public void setProxy(ProxyBean proxy) {
		this.proxy = proxy;
	}

	public boolean isShowStatusBar() {
		return showStatusBar;
	}

	public void setShowStatusBar(boolean showStatusBar) {
		this.showStatusBar = showStatusBar;
	}

	public Point getMultipleViewLocation() {
		return multipleViewLocation;
	}

	public Dimension getMutipleViewSize() {
		return mutipleViewSize;
	}

	public void setMultipleViewLocation(Point multipleViewLocation) {
		this.multipleViewLocation = multipleViewLocation;
	}

	public void setMutipleViewSize(Dimension mutipleViewSize) {
		this.mutipleViewSize = mutipleViewSize;
	}

	public boolean isShowAlbumInPlayList() {
		return showAlbumInPlayList;
	}

	public void setShowAlbumInPlayList(boolean showAlbumInPlayList) {
		this.showAlbumInPlayList = showAlbumInPlayList;
	}

	public boolean isShowArtistInPlayList() {
		return showArtistInPlayList;
	}

	public void setShowArtistInPlayList(boolean showArtistInPlayList) {
		this.showArtistInPlayList = showArtistInPlayList;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public boolean isShowGenreInPlayList() {
		return showGenreInPlayList;
	}

	public void setShowGenreInPlayList(boolean showGenreInPlayList) {
		this.showGenreInPlayList = showGenreInPlayList;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public boolean isShowTrackInPlayList() {
		return showTrackInPlayList;
	}

	public void setShowTrackInPlayList(boolean showTrackInPlayList) {
		this.showTrackInPlayList = showTrackInPlayList;
	}
}
