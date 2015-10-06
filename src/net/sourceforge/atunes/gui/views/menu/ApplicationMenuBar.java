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

package net.sourceforge.atunes.gui.views.menu;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.utils.language.LanguageTool;



public class ApplicationMenuBar extends JMenuBar {

	private static final long serialVersionUID = 234977404080329591L;

	private JMenu file;
	private JMenu edit;
	private JMenu view;
	private JMenu player;
	private JMenu playList;
	private JMenu tools;
	private JMenu device;
	private JMenu about;
	
	
	// File Menu
	private JMenuItem selectRepository;
	private JMenuItem refreshRepository;
	private JMenu recentRepositories;
	private JMenuItem exit;
	
	// Edit Menu
	private JMenuItem editPreferences;
	
	// View Menu
	private JMenuItem tagView;
	private JMenuItem folderView;
	private JMenuItem favoriteView;
	private JMenuItem deviceView;
	private JCheckBoxMenuItem showStatusBar;
	private JCheckBoxMenuItem showNavigationPanel;
	private JCheckBoxMenuItem showNavigationTable;
	private JCheckBoxMenuItem showProperties;
	private JCheckBoxMenuItem showOSD;
	
	// Player Menu
	private JMenuItem volumeDown;
	private JMenuItem volumeUp;
	private JMenuItem mute;
	
	
	
	// PlayList Menu
	private JMenuItem playListPlay;
	private JMenuItem playListTags;
	private JMenuItem playListEditTag;
	private JMenuItem playListAutoSetTrack;
	private JMenuItem playListAutoSetGenre;
	private JMenuItem playListSave;
	private JMenuItem playListLoad;
	private JMenuItem playListFilter;
	private JMenuItem playListInfo;
	private JMenuItem playListDelete;
	private JMenuItem playListClear;
	private JMenuItem playListTop;
	private JMenuItem playListUp;
	private JMenuItem playListDown;
	private JMenuItem playListBottom;
	private JMenuItem playListFavoriteSong;
	private JMenuItem playListFavoriteAlbum;
	private JMenuItem playListFavoriteArtist;
	private JMenuItem playListArtist;
	private JMenuItem playListAlbum;
	private JCheckBoxMenuItem playListShowButtons;
	
	// Tools Menu
	private JMenuItem toolsExport;
	private JMenuItem ripCd;
	private JMenuItem stats;
	
	// Device Menu
	private JMenuItem deviceConnect;
	private JMenuItem deviceRefresh;
	private JMenuItem deviceDisconnect;
	private JRadioButtonMenuItem deviceViewByTag;
	private JRadioButtonMenuItem deviceViewByFolder;
	
	// About Menu
	private JMenuItem checkUpdates;
	private JMenuItem aboutItem;
	
	public ApplicationMenuBar() {
		super();
		addMenus();
	}
	
	private void addMenus() {
		file = new JMenu(LanguageTool.getString("FILE"));
		selectRepository = new JMenuItem(LanguageTool.getString("SELECT_REPOSITORY"), ImageLoader.FOLDER);
		file.add(selectRepository);
		refreshRepository = new JMenuItem(LanguageTool.getString("REFRESH_REPOSITORY"), ImageLoader.REFRESH);
		file.add(refreshRepository);
		file.add(new JSeparator());
		recentRepositories = new JMenu(LanguageTool.getString("RECENT_REPOSITORIES"));
		file.add(recentRepositories);
		file.add(new JSeparator());
		exit = new JMenuItem(LanguageTool.getString("EXIT"), ImageLoader.EXIT);
		file.add(exit);
		
		edit = new JMenu(LanguageTool.getString("EDIT"));
		editPreferences = new JMenuItem(LanguageTool.getString("PREFERENCES") + "...", ImageLoader.PREFS);
		edit.add(editPreferences);
		
		view = new JMenu(LanguageTool.getString("VIEW"));
		tagView = new JMenuItem(LanguageTool.getString("TAGS"), ImageLoader.INFO);
		folderView = new JMenuItem(LanguageTool.getString("FOLDERS"), ImageLoader.FOLDER);
		favoriteView = new JMenuItem(LanguageTool.getString("FAVORITES"), ImageLoader.FAVORITE);
		deviceView = new JMenuItem(LanguageTool.getString("DEVICE"), ImageLoader.DEVICE);
		showStatusBar = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_STATUS_BAR"), null);
		showNavigationPanel = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_NAVIGATION_PANEL"), null);
		showNavigationTable = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_NAVIGATION_TABLE"), null);
		showProperties = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_SONG_PROPERTIES"), null);
		showOSD = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_OSD"), null);

		view.add(tagView);
		view.add(folderView);
		view.add(favoriteView);
		view.add(deviceView);
		view.add(new JSeparator());
		view.add(showStatusBar);
		view.add(showNavigationPanel);
		view.add(showNavigationTable);
		view.add(showProperties);
		view.add(showOSD);
		
		player = new JMenu(LanguageTool.getString("PLAYER"));
		volumeDown = new JMenuItem(LanguageTool.getString("VOLUME_DOWN"));
		volumeUp = new JMenuItem(LanguageTool.getString("VOLUME_UP"));
		mute = new JMenuItem(LanguageTool.getString("MUTE"));
		player.add(volumeDown);
		player.add(volumeUp);
		player.add(mute);
		
		playList = new JMenu(LanguageTool.getString("PLAYLIST"));
		playListPlay = new JMenuItem(LanguageTool.getString("PLAY"), ImageLoader.PLAY_MENU);
		playListTags = new JMenu(LanguageTool.getString("TAGS"));
		playListEditTag = new JMenuItem(LanguageTool.getString("EDIT_TAG"));
		playListAutoSetTrack = new JMenuItem(LanguageTool.getString("AUTO_SET_TRACK_NUMBER"));
		playListAutoSetGenre = new JMenuItem(LanguageTool.getString("AUTO_SET_GENRE"));
		playListTags.add(playListEditTag);
		playListTags.add(playListAutoSetTrack);
		playListTags.add(playListAutoSetGenre);

		
		playListSave = new JMenuItem(LanguageTool.getString("SAVE") + "...", ImageLoader.SAVE);
		playListLoad = new JMenuItem(LanguageTool.getString("LOAD") + "...", ImageLoader.FOLDER);
		playListFilter = new JMenuItem(LanguageTool.getString("FILTER"));
		playListInfo = new JMenuItem(LanguageTool.getString("INFO"), ImageLoader.INFO);
		playListDelete = new JMenuItem(LanguageTool.getString("REMOVE"), ImageLoader.REMOVE);
		playListClear = new JMenuItem(LanguageTool.getString("CLEAR"), ImageLoader.CLEAR);
		playListTop = new JMenuItem(LanguageTool.getString("MOVE_TO_TOP"), ImageLoader.GO_TOP);
		playListUp = new JMenuItem(LanguageTool.getString("MOVE_UP"), ImageLoader.GO_UP);
		playListDown = new JMenuItem(LanguageTool.getString("MOVE_DOWN"), ImageLoader.GO_DOWN);
		playListBottom = new JMenuItem(LanguageTool.getString("MOVE_TO_BOTTOM"), ImageLoader.GO_BOTTOM);
		playListFavoriteSong = new JMenuItem(LanguageTool.getString("SET_FAVORITE_SONG"), ImageLoader.FAVORITE);
		playListFavoriteAlbum = new JMenuItem(LanguageTool.getString("SET_FAVORITE_ALBUM"), ImageLoader.FAVORITE);
		playListFavoriteArtist = new JMenuItem(LanguageTool.getString("SET_FAVORITE_ARTIST"), ImageLoader.FAVORITE);
		playListArtist = new JMenuItem(LanguageTool.getString("SET_ARTIST_AS_PLAYLIST"), ImageLoader.ARTIST);
		playListAlbum = new JMenuItem(LanguageTool.getString("SET_ALBUM_AS_PLAYLIST"), ImageLoader.ALBUM);
		playListShowButtons = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_PLAYLIST_CONTROLS"), null);
		
		tools = new JMenu(LanguageTool.getString("TOOLS"));
		toolsExport = new JMenuItem(LanguageTool.getString("EXPORT") + "...");
		ripCd = new JMenuItem(LanguageTool.getString("RIP_CD") + "...");
		stats = new JMenuItem(LanguageTool.getString("STATS"));
		tools.add(toolsExport);
		tools.add(ripCd);
		tools.add(stats);
		
		device = new JMenu(LanguageTool.getString("DEVICE"));
		deviceConnect = new JMenuItem(LanguageTool.getString("CONNECT"));
		device.add(deviceConnect);
		deviceRefresh = new JMenuItem(LanguageTool.getString("REFRESH"));
		device.add(deviceRefresh);
		deviceDisconnect = new JMenuItem(LanguageTool.getString("DISCONNECT"));
		device.add(deviceDisconnect);
		device.add(new JSeparator());
		deviceViewByTag = new JRadioButtonMenuItem(LanguageTool.getString("SORT_BY_TAG"));
		device.add(deviceViewByTag);
		deviceViewByFolder = new JRadioButtonMenuItem(LanguageTool.getString("SORT_BY_FOLDER"));
		device.add(deviceViewByFolder);
		ButtonGroup group2 = new ButtonGroup();
		group2.add(deviceViewByTag);
		group2.add(deviceViewByFolder);
		
		playList.add(playListPlay);
		playList.add(playListInfo);
		playList.add(playListTags);
		playList.add(new JSeparator());
		playList.add(playListSave);
		playList.add(playListLoad);
		playList.add(new JSeparator());
		playList.add(playListFilter);
		playList.add(new JSeparator());
		playList.add(playListDelete);
		playList.add(playListClear);
		playList.add(new JSeparator());
		playList.add(playListTop);
		playList.add(playListUp);
		playList.add(playListDown);
		playList.add(playListBottom);
		playList.add(new JSeparator());
		playList.add(playListFavoriteSong);
		playList.add(playListFavoriteAlbum);
		playList.add(playListFavoriteArtist);
		playList.add(new JSeparator());
		playList.add(playListArtist);
		playList.add(playListAlbum);
		playList.add(new JSeparator());
		playList.add(playListShowButtons);
		
		about = new JMenu(LanguageTool.getString("ABOUT"));
		checkUpdates = new JMenuItem(LanguageTool.getString("CHECK_FOR_UPDATES"));
		aboutItem = new JMenuItem(LanguageTool.getString("ABOUT"));

		about.add(checkUpdates);
		about.add(new JSeparator());
		about.add(aboutItem);
		
		add(file);
		add(edit);
		add(view);
		add(player);
		add(playList);
		add(device);
		add(tools);
		add(about);
	}
	
	public JPopupMenu getMenuAsPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		menu.add(file);
		menu.add(edit);
		menu.add(view);
		menu.add(player);
		menu.add(playList);
		menu.add(tools);
		menu.add(about);
		return menu;
	}

	public JMenuItem getSelectRepository() {
		return selectRepository;
	}

	public JMenuItem getPlayListBottom() {
		return playListBottom;
	}

	public JMenuItem getPlayListClear() {
		return playListClear;
	}

	public JMenuItem getPlayListDelete() {
		return playListDelete;
	}

	public JMenuItem getPlayListDown() {
		return playListDown;
	}

	public JMenuItem getPlayListInfo() {
		return playListInfo;
	}

	public JMenuItem getPlayListTop() {
		return playListTop;
	}

	public JMenuItem getPlayListUp() {
		return playListUp;
	}

	public JMenuItem getAboutItem() {
		return aboutItem;
	}

	public JCheckBoxMenuItem getShowOSD() {
		return showOSD;
	}

	public JMenuItem getExit() {
		return exit;
	}

	public JCheckBoxMenuItem getShowProperties() {
		return showProperties;
	}

	public JCheckBoxMenuItem getShowNavigationTable() {
		return showNavigationTable;
	}

	public JMenuItem getPlayListLoad() {
		return playListLoad;
	}

	public JMenuItem getPlayListPlay() {
		return playListPlay;
	}

	public JMenuItem getPlayListSave() {
		return playListSave;
	}

	public JMenuItem getPlayListFavoriteAlbum() {
		return playListFavoriteAlbum;
	}

	public JMenuItem getPlayListFavoriteArtist() {
		return playListFavoriteArtist;
	}

	public JMenuItem getPlayListFavoriteSong() {
		return playListFavoriteSong;
	}

	public JMenuItem getRefreshRepository() {
		return refreshRepository;
	}

	public JMenuItem getToolsExport() {
		return toolsExport;
	}

	public JMenuItem getDeviceConnect() {
		return deviceConnect;
	}
	
	public JMenuItem getDeviceRefresh() {
		return deviceRefresh;
	}

	public JMenuItem getDeviceDisconnect() {
		return deviceDisconnect;
	}

	public JRadioButtonMenuItem getDeviceViewByFolder() {
		return deviceViewByFolder;
	}

	public JRadioButtonMenuItem getDeviceViewByTag() {
		return deviceViewByTag;
	}

	public JMenuItem getDeviceView() {
		return deviceView;
	}

	public JMenuItem getFavoriteView() {
		return favoriteView;
	}

	public JMenuItem getFolderView() {
		return folderView;
	}

	public JMenuItem getTagView() {
		return tagView;
	}

	public JCheckBoxMenuItem getShowNavigationPanel() {
		return showNavigationPanel;
	}

	public JMenuItem getPlayListAlbum() {
		return playListAlbum;
	}

	public JMenuItem getPlayListArtist() {
		return playListArtist;
	}

	public JMenuItem getRipCd() {
		return ripCd;
	}

	public JCheckBoxMenuItem getPlayListShowButtons() {
		return playListShowButtons;
	}

	public JMenuItem getEditPreferences() {
		return editPreferences;
	}

	public JMenu getRecentRepositories() {
		return recentRepositories;
	}

	public JCheckBoxMenuItem getShowStatusBar() {
		return showStatusBar;
	}

	public JMenuItem getPlayListEditTag() {
		return playListEditTag;
	}

	public JMenuItem getStats() {
		return stats;
	}

	public JMenuItem getPlayListFilter() {
		return playListFilter;
	}

	public JMenuItem getCheckUpdates() {
		return checkUpdates;
	}

	public JMenuItem getPlayListAutoSetGenre() {
		return playListAutoSetGenre;
	}

	public JMenuItem getPlayListAutoSetTrack() {
		return playListAutoSetTrack;
	}

	public JMenuItem getVolumeDown() {
		return volumeDown;
	}

	public JMenuItem getVolumeUp() {
		return volumeUp;
	}

	public JMenuItem getMute() {
		return mute;
	}

}
