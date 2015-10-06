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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.views.controls.UrlLabel;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class AudioScrobblerPanel extends JPanel {

	private static final long serialVersionUID = 707242790413122482L;

	private JPanel albumTabPanel;
	private JPanel artistTabPanel;
	private JPanel similarTabPanel;
	private JPanel lyricsPanel;
	
	private JPanel albumPanel;
	private UrlLabel artistLabel;
	private UrlLabel albumLabel;
	private UrlLabel yearLabel;
	private JLabel albumCoverLabel;
	private JPanel tracksContainer;
	private JLabel lyricsLabel;
	private JLabel lyricsArtistLabel;
	private JTextArea lyricsContainer;
	
	private JPanel albumsPanel;
	private JScrollPane albumsScrollPane;
	private JLabel artistImageLabel;
	private UrlLabel artistAlbumsLabel;
	private JPanel albumsContainer;
	
	private JPanel similarArtistsPanel;
	private JPanel similarArtistsContainer;
	private JScrollPane similarArtistsScrollPane;
	
	public static Dimension prefSize = new Dimension(230,0);
	public static Dimension minimumSize = new Dimension(170,0);
	
	public AudioScrobblerPanel() {
		super(new GridBagLayout());
		setPreferredSize(prefSize);
		setMinimumSize(minimumSize);
		setContent();
	}
	
	private void setContent() {
		JTabbedPane tabbedPane = new JTabbedPane();
		albumTabPanel = new JPanel(new GridBagLayout());
		artistTabPanel = new JPanel(new GridBagLayout());
		similarTabPanel = new JPanel(new GridBagLayout());
		lyricsPanel = new JPanel(new GridBagLayout());
		tabbedPane.addTab(LanguageTool.getString("SONG"), lyricsPanel);
		tabbedPane.addTab(LanguageTool.getString("ALBUM"), albumTabPanel);
		tabbedPane.addTab(LanguageTool.getString("ARTIST"), artistTabPanel);
		tabbedPane.addTab(LanguageTool.getString("SIMILAR"), similarTabPanel);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(3,2,0,2);
		add(tabbedPane,c);

		addAlbumPanel();
		addAlbumsPanel();
		addSimilarArtistsPanel();
		addLyricsPanel();
	}
		
	private void addAlbumPanel() {
		albumPanel = new JPanel(new GridBagLayout());
		
		artistLabel = new UrlLabel();
		artistLabel.setHorizontalAlignment(SwingConstants.CENTER);
		artistLabel.setFont(FontSingleton.GENERAL_FONT);
		albumLabel = new UrlLabel();
		albumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		albumLabel.setFont(FontSingleton.AUDIO_SCROBBLER_BIG_FONT);
		albumCoverLabel = new JLabel();
		albumCoverLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		albumCoverLabel.setVisible(false);
		yearLabel = new UrlLabel();
		yearLabel.setHorizontalAlignment(SwingConstants.CENTER);
		yearLabel.setFont(FontSingleton.GENERAL_FONT);

		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(15,0,0,0);
		albumPanel.add(albumCoverLabel, c);
		
		c.gridy = 1; c.insets = new Insets(5,5,0,5);
		albumPanel.add(albumLabel, c);
		
		c.gridy = 2;
		albumPanel.add(artistLabel, c);
		
		c.gridy = 3;
		albumPanel.add(yearLabel, c);
		
		tracksContainer = new JPanel(new GridBagLayout());
		c.gridx = 0; c.gridy = 4; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(20,5,5,5);
		JScrollPane scroll = new JScrollPane(tracksContainer);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		albumPanel.add(scroll, c);
		
		c.gridx = 0; c.gridy = 1; c.insets = new Insets(0,0,0,0);
		albumTabPanel.add(albumPanel, c);
		
		albumPanel.setVisible(false);
	}
	
	private void addAlbumsPanel() {
		albumsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		artistImageLabel = new JLabel();
		artistImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		artistImageLabel.setVisible(false);
		artistAlbumsLabel = new UrlLabel();
		artistAlbumsLabel.setFont(FontSingleton.AUDIO_SCROBBLER_BIG_FONT);
		artistAlbumsLabel.setVisible(false);
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(15,5,0,5);
		albumsPanel.add(artistImageLabel, c);
		c.gridx = 0; c.gridy = 1; c.insets = new Insets(5,5,0,5);
		albumsPanel.add(artistAlbumsLabel, c);
		albumsContainer = new JPanel(new GridBagLayout());
		albumsScrollPane = new JScrollPane(albumsContainer);
		//albumsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		albumsScrollPane.setAutoscrolls(false);
		albumsScrollPane.setOpaque(false);
		albumsScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		c.gridy = 2; c.weightx = 1; c.weighty = 1;  c.fill = GridBagConstraints.BOTH; c.insets = new Insets(0,0,0,0);
		albumsPanel.add(albumsScrollPane, c);
		c.gridy = 3; c.weighty = 1; c.insets = new Insets(0,0,0,0);
		artistTabPanel.add(albumsPanel, c);
		
		albumsPanel.setVisible(false);
	}
	
	private void addSimilarArtistsPanel() { 
		similarArtistsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		similarArtistsContainer = new JPanel(new GridBagLayout());
		similarArtistsScrollPane = new JScrollPane(similarArtistsContainer);
		similarArtistsScrollPane.setAutoscrolls(false);
		similarArtistsScrollPane.setOpaque(false);
		similarArtistsScrollPane.setBorder(BorderFactory.createEmptyBorder());
		similarArtistsPanel.add(similarArtistsScrollPane, c);

		c.gridx = 0; c.gridy = 0; c.weighty = 0.3;
		similarTabPanel.add(similarArtistsPanel, c);
		
		similarArtistsPanel.setVisible(false);
	}
	
	private void addLyricsPanel() {
		lyricsLabel = new JLabel();
		lyricsLabel.setFont(FontSingleton.AUDIO_SCROBBLER_BIG_FONT);
		lyricsArtistLabel = new JLabel();
		lyricsArtistLabel.setFont(FontSingleton.GENERAL_FONT);
		lyricsContainer = new JTextArea();
		lyricsContainer.setOpaque(false);
		lyricsContainer.setBorder(BorderFactory.createEmptyBorder());
		lyricsContainer.setEditable(false);
		lyricsContainer.setFont(FontSingleton.GENERAL_FONT);
		JScrollPane scrollPane = new JScrollPane(lyricsContainer);		
		scrollPane.setAutoscrolls(false);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(5,10,0,10);
		lyricsPanel.add(lyricsLabel, c);
		c.gridy = 1; c.insets = new Insets(5,10,10,10);
		lyricsPanel.add(lyricsArtistLabel, c);
		c.gridy = 2; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,5,5,5);
		lyricsPanel.add(scrollPane, c);
	}

	public void clearAlbumsContainer() {
		albumsContainer.removeAll();
		// Add pad to albums container
		JPanel padPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 200; c.weighty = 1; c.fill = GridBagConstraints.HORIZONTAL;
		albumsContainer.add(padPanel, c);
	}
	
	public void clearSimilarArtistsContainer() {
		similarArtistsContainer.removeAll();
		// Add pad to albums container
		JPanel padPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 200; c.weighty = 1; c.fill = GridBagConstraints.HORIZONTAL;
		similarArtistsContainer.add(padPanel, c);
	}
	
	public UrlLabel getAlbumLabel() {
		return albumLabel;
	}

	public UrlLabel getArtistLabel() {
		return artistLabel;
	}

	public JPanel getTracksContainer() {
		return tracksContainer;
	}

	public UrlLabel getArtistAlbumsLabel() {
		return artistAlbumsLabel;
	}

	public JLabel getAlbumCoverLabel() {
		return albumCoverLabel;
	}

	public JPanel getAlbumsContainer() {
		return albumsContainer;
	}

	public JScrollPane getAlbumsScrollPane() {
		return albumsScrollPane;
	}

	public JPanel getSimilarTabPanel() {
		return similarTabPanel;
	}

	public JPanel getAlbumPanel() {
		return albumPanel;
	}

	public JPanel getAlbumsPanel() {
		return albumsPanel;
	}

	public JPanel getSimilarArtistsPanel() {
		return similarArtistsPanel;
	}

	public JPanel getSimilarArtistsContainer() {
		return similarArtistsContainer;
	}

	public JScrollPane getSimilarArtistsScrollPane() {
		return similarArtistsScrollPane;
	}

	public UrlLabel getYearLabel() {
		return yearLabel;
	}

	public JLabel getArtistImageLabel() {
		return artistImageLabel;
	}

	public JTextArea getLyricsContainer() {
		return lyricsContainer;
	}

	public JLabel getLyricsLabel() {
		return lyricsLabel;
	}

	public JLabel getLyricsArtistLabel() {
		return lyricsArtistLabel;
	}

}
