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

package net.sourceforge.atunes.gui.views.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.PopUpButton;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class PlayListControlsPanel extends JPanel {

	private static final long serialVersionUID = -1966827894270243002L;

	private PopUpButton sortPopup;
	private JCheckBoxMenuItem showTrack;
	private JCheckBoxMenuItem showArtist;
	private JCheckBoxMenuItem showAlbum;
	private JCheckBoxMenuItem showGenre;
	private JMenuItem sortByTrack;
	private JMenuItem sortByTitle;
	private JMenuItem sortByArtist;
	private JMenuItem sortByAlbum;
	private JMenuItem sortByGenre;
	
	private JButton savePlaylistButton;
	private JButton loadPlaylistButton;
	
	private JButton artistButton;
	private JButton albumButton;
	
	private JButton topButton;
	private JButton upButton;
	private JButton deleteButton;
	private JButton downButton;
	private JButton bottomButton;
	private JButton infoButton;
	private JButton clearButton;
	
	private PopUpButton favoritePopup;
	private JMenuItem favoriteSong;
	private JMenuItem favoriteAlbum;
	private JMenuItem favoriteArtist;
	
	public PlayListControlsPanel() {
		super(new GridBagLayout());
		addContent();
	}
	
	private void addContent() {
		sortPopup = new PopUpButton(LanguageTool.getString("OPTIONS"), PopUpButton.TOP_RIGHT);
		
		showTrack = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_TRACKS"));
		showArtist = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_ARTISTS"));
		showAlbum = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_ALBUMS"));
		showGenre = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_GENRE"));
		sortByTrack = new JMenuItem(LanguageTool.getString("SORT_BY_TRACK_NUMBER"));
		sortByTitle = new JMenuItem(LanguageTool.getString("SORT_BY_TITLE"));
		sortByArtist = new JMenuItem(LanguageTool.getString("SORT_BY_ARTIST"));
		sortByAlbum = new JMenuItem(LanguageTool.getString("SORT_BY_ALBUM"));
		sortByGenre = new JMenuItem(LanguageTool.getString("SORT_BY_GENRE"));
		
		sortPopup.add(showTrack);
		sortPopup.add(showArtist);
		sortPopup.add(showAlbum);
		sortPopup.add(showGenre);
		sortPopup.add(new JSeparator());
		sortPopup.add(sortByTrack);
		sortPopup.add(sortByTitle);
		sortPopup.add(sortByArtist);
		sortPopup.add(sortByAlbum);
		sortPopup.add(sortByGenre);
		
		savePlaylistButton = new CustomButton(ImageLoader.SAVE, null);
		savePlaylistButton.setToolTipText(LanguageTool.getString("SAVE_PLAYLIST_TOOLTIP"));
		loadPlaylistButton = new CustomButton(ImageLoader.FOLDER, null);
		loadPlaylistButton.setToolTipText(LanguageTool.getString("LOAD_PLAYLIST_TOOLTIP"));
		
		artistButton = new CustomButton(ImageLoader.ARTIST, null);
		artistButton.setEnabled(false);
		artistButton.setToolTipText(LanguageTool.getString("ARTIST_BUTTON_TOOLTIP"));
		albumButton = new CustomButton(ImageLoader.ALBUM, null);
		albumButton.setEnabled(false);
		albumButton.setToolTipText(LanguageTool.getString("ALBUM_BUTTON_TOOLTIP"));

		topButton = new CustomButton(ImageLoader.GO_TOP, null);
		topButton.setEnabled(false);
		topButton.setToolTipText(LanguageTool.getString("MOVE_TO_TOP_TOOLTIP"));
		upButton = new CustomButton(ImageLoader.GO_UP, null);
		upButton.setEnabled(false);
		upButton.setToolTipText(LanguageTool.getString("MOVE_UP_TOOLTIP"));
		deleteButton = new CustomButton(ImageLoader.REMOVE, null);
		deleteButton.setEnabled(false);
		deleteButton.setToolTipText(LanguageTool.getString("REMOVE_TOOLTIP"));
		downButton = new CustomButton(ImageLoader.GO_DOWN, null);
		downButton.setEnabled(false);
		downButton.setToolTipText(LanguageTool.getString("MOVE_DOWN_TOOLTIP"));
		bottomButton = new CustomButton(ImageLoader.GO_BOTTOM, null);
		bottomButton.setEnabled(false);
		bottomButton.setToolTipText(LanguageTool.getString("MOVE_BOTTOM_TOOLTIP"));
		infoButton = new CustomButton(ImageLoader.INFO, null);
		infoButton.setEnabled(false);
		infoButton.setToolTipText(LanguageTool.getString("INFO_BUTTON_TOOLTIP"));
		clearButton = new CustomButton(ImageLoader.CLEAR, null);
		clearButton.setEnabled(false);
		clearButton.setToolTipText(LanguageTool.getString("CLEAR_TOOLTIP"));
		favoritePopup = new PopUpButton(ImageLoader.FAVORITE, PopUpButton.TOP_LEFT);
		favoritePopup.setEnabled(false);
		favoritePopup.setToolTipText(LanguageTool.getString("FAVORITE_TOOLTIP"));
		favoriteSong = new JMenuItem(LanguageTool.getString("SET_FAVORITE_SONG"), ImageLoader.FAVORITE);
		favoriteAlbum = new JMenuItem(LanguageTool.getString("SET_FAVORITE_ALBUM"), ImageLoader.FAVORITE);
		favoriteArtist = new JMenuItem(LanguageTool.getString("SET_FAVORITE_ARTIST"), ImageLoader.FAVORITE);
		favoritePopup.add(favoriteSong);
		favoritePopup.add(favoriteAlbum);
		favoritePopup.add(favoriteArtist);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(0,0,0,10);
		add(sortPopup, c);
		
		c.gridx = 1; c.gridy = 0; c.insets = new Insets(0,0,0,0);
		setButton(savePlaylistButton, c);
		
		c.gridx = 2; c.gridy = 0;
		setButton(loadPlaylistButton, c);
		
		c.gridx = 3; c.gridy = 0; c.weightx = 1; c.anchor = GridBagConstraints.EAST;
		setButton(artistButton, c);
		
		c.gridx = 4; c.gridy = 0; c.weightx = 0; c.insets = new Insets(0,0,0,10);
		setButton(albumButton, c);
		
		c.gridx = 5; c.gridy = 0; c.insets = new Insets(0,0,0,10);
		setButton(infoButton, c);

		c.gridx = 6; c.gridy = 0; c.insets = new Insets(0,0,0,0);
		setButton(deleteButton, c);

		c.gridx = 7; c.gridy = 0; 
		setButton(clearButton, c);
		
		c.gridx = 8; c.gridy = 0; 
		setButton(topButton, c);
		
		c.gridx = 9; c.gridy = 0;
		setButton(upButton, c);
		
		c.gridx = 10; c.gridy = 0;
		setButton(downButton, c);
		
		c.gridx = 11; c.gridy = 0; c.weighty = 1; c.insets = new Insets(0,0,0,10);
		setButton(bottomButton, c);
		
		c.gridx = 12; c.gridy = 0; c.insets = new Insets(0,0,0,0);
		setButton(favoritePopup, c);
	}
	
	private void setButton(JButton button, GridBagConstraints c) {
		button.setPreferredSize(new Dimension(20,20));
		add(button, c);
	}

	public JButton getBottomButton() {
		return bottomButton;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JButton getDownButton() {
		return downButton;
	}

	public JButton getInfoButton() {
		return infoButton;
	}

	public JButton getTopButton() {
		return topButton;
	}

	public JButton getUpButton() {
		return upButton;
	}

	public JButton getClearButton() {
		return clearButton;
	}

	public JButton getLoadPlaylistButton() {
		return loadPlaylistButton;
	}

	public JButton getSavePlaylistButton() {
		return savePlaylistButton;
	}

	public JMenuItem getFavoriteAlbum() {
		return favoriteAlbum;
	}

	public JMenuItem getFavoriteArtist() {
		return favoriteArtist;
	}

	public JMenuItem getFavoriteSong() {
		return favoriteSong;
	}

	public PopUpButton getFavoritePopup() {
		return favoritePopup;
	}

	public JMenuItem getSortByTitle() {
		return sortByTitle;
	}

	public JMenuItem getSortByAlbum() {
		return sortByAlbum;
	}

	public JMenuItem getSortByArtist() {
		return sortByArtist;
	}

	public JCheckBoxMenuItem getShowTrack() {
		return showTrack;
	}
	
	public JCheckBoxMenuItem getShowArtist() {
		return showArtist;
	}

	public JCheckBoxMenuItem getShowAlbum() {
		return showAlbum;
	}

	public JButton getAlbumButton() {
		return albumButton;
	}

	public JButton getArtistButton() {
		return artistButton;
	}

	public JMenuItem getSortByTrack() {
		return sortByTrack;
	}

	public JMenuItem getSortByGenre() {
		return sortByGenre;
	}

	public JCheckBoxMenuItem getShowGenre() {
		return showGenre;
	}
}
