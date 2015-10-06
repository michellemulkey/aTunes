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

package net.sourceforge.atunes.kernel.controllers.audioScrobbler;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.views.controls.UrlLabel;
import net.sourceforge.atunes.gui.views.controls.UrlTextArea;
import net.sourceforge.atunes.gui.views.panels.AudioScrobblerPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.model.PanelController;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerAlbum;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerArtist;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerTrack;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.ImageUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class AudioScrobblerController extends PanelController {

	private AudioFile currentFile;
	AudioScrobblerPanel panel;
	
	public AudioScrobblerController(AudioScrobblerPanel panel) {
		super(panel);
		this.panel = panel;
	}
	
	protected void addBindings() {}
	protected void addStateBindings() {}
	protected void notifyReload() {}
	
	public void clear() {
		currentFile = null;
		
		panel.getTracksContainer().removeAll();
		panel.clearAlbumsContainer();
		panel.getArtistImageLabel().setVisible(false);
		panel.clearSimilarArtistsContainer();
		panel.getAlbumPanel().setVisible(false);
		panel.getAlbumsPanel().setVisible(false);
		panel.getSimilarArtistsPanel().setVisible(false);
		panel.getLyricsContainer().setText("");
		panel.getLyricsLabel().setText("");
		panel.getLyricsArtistLabel().setText("");
	}
	
	public void updatePanel(AudioFile file) {
		if (Kernel.getInstance().state.isUseAudioScrobbler()) {
			currentFile = file;
			HandlerProxy.getAudioScrobblerServiceHandler().retrieveInfo(file);
		}
	}
	
	public void notifyFinishGetAlbumInfo(final AudioScrobblerAlbum album, final Image img) {
		panel.getArtistLabel().setText(album != null ? album.getArtist() : currentFile.getArtist());
		panel.getAlbumLabel().setText(album != null ? album.getTitle() : LanguageTool.getString("UNKNOWN_ALBUM"));
		panel.getYearLabel().setText(album != null ? album.getYear() : "");
		if (album != null) {
			panel.getArtistLabel().setUrl(album.getArtistUrl());
			panel.getAlbumLabel().setUrl(album.getUrl());
			if (album.getYear() != null)
				panel.getYearLabel().setUrl("http://en.wikipedia.org/wiki/" + album.getYear());
		}
		if (img != null)
			panel.getAlbumCoverLabel().setIcon(new ImageIcon(img));
		panel.getAlbumCoverLabel().setVisible(img != null);

		if (album != null) {
			GridBagConstraints c = new GridBagConstraints();
			int i = 0;
			c.gridx = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
			for (AudioScrobblerTrack t: album.getTracks()) {
				UrlLabel l = new UrlLabel((i+1) + ". " + t.getTitle());
				l.setUrl(t.getUrl());
				l.setFont(FontSingleton.GENERAL_FONT);
				c.gridy = i;
				panel.getTracksContainer().add(l, c);
				i++;
			}
			c.gridy = i; c.weighty = 1; c.fill = GridBagConstraints.VERTICAL;
			JPanel padPanel = new JPanel();
			padPanel.setOpaque(false);
			panel.getTracksContainer().add(padPanel, c);
		}
		panel.getAlbumPanel().setVisible(true);
		panel.validate();
		panel.repaint();
	}

	public void notifyFinishGetAlbumsInfo(final AudioScrobblerAlbum album, final Image cover) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					panel.getArtistAlbumsLabel().setText(album.getArtist());
					panel.getArtistAlbumsLabel().setVisible(true);
					panel.getArtistAlbumsLabel().setUrl(album.getUrl());
					panel.getAlbumsPanel().setVisible(true);
					fillAlbumsList(album, cover);
				}
			});
		} catch (Exception e) {
		}
	}
	
	public void notifyArtistImage(final Image img) {
		panel.getArtistImageLabel().setIcon(new ImageIcon(img));
		panel.getArtistImageLabel().setVisible(true);
	}
	
	public void notifyFinishGetSimilarArtist(final AudioScrobblerArtist artist, final Image img) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					panel.getSimilarArtistsPanel().setVisible(true);
					fillSimilarArtistsList(artist, img);
				}
			});
		} catch (Exception e) {
		}
	}
	
	public void setLyrics(String title, String artist, String lyrics) {
		panel.getLyricsLabel().setText(title);
		panel.getLyricsArtistLabel().setText(artist);
		panel.getLyricsContainer().setText(lyrics);
	}
	
	void fillAlbumsList(AudioScrobblerAlbum album, Image cover) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.weightx = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(5,5,5,5);
		c.gridy = panel.getAlbumsContainer().getComponentCount();
		panel.getAlbumsContainer().add(getPanelForAlbum(album, cover), c);
		panel.getAlbumsContainer().validate();
		panel.getAlbumsContainer().repaint();
	}

	void fillSimilarArtistsList(AudioScrobblerArtist artist, Image img) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(5,5,5,5);
		UrlLabel label = new UrlLabel(artist.getName() + (artist.getMatch().equals("") ? "" : " (" + artist.getMatch() + "%)"));
		label.setUrl(artist.getUrl());
		label.setIcon(new ImageIcon(img));
		label.setFont(FontSingleton.GENERAL_FONT);
		c.gridy = panel.getSimilarArtistsContainer().getComponentCount();
		panel.getSimilarArtistsContainer().add(label, c);
		panel.getSimilarArtistsContainer().validate();
		panel.getSimilarArtistsContainer().repaint();
	}
	
	private JPanel getPanelForAlbum(AudioScrobblerAlbum album, Image cover) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);

		JLabel img = new JLabel();
		img.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		BufferedImage resizedImage = ImageUtils.scaleImage(cover, Constants.AUDIO_SCROBBLER_IMAGE_WIDTH, Constants.AUDIO_SCROBBLER_IMAGE_HEIGHT);
		ImageIcon image = resizedImage != null ? new ImageIcon(resizedImage) : null;
		img.setIcon(image);
		
		UrlTextArea name = new UrlTextArea(album.getTitle());
		name.setFont(FontSingleton.GENERAL_FONT);
		name.setUrl(album.getUrl());
		JLabel year = new JLabel(album.getYear());
		year.setFont(FontSingleton.GENERAL_FONT);
		GridBagConstraints c = new GridBagConstraints(); 
		c.gridx = 0; c.gridy = 0; c.gridheight = 3; c.insets = new Insets(0,0,0,5); c.weightx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTH;
		panel.add(img, c);
		c.gridx = 1; c.weightx = 1; c.gridheight = 1; c.insets = new Insets(0,0,0,20);
		panel.add(name, c); 
		c.gridy = 1;
		panel.add(year, c);
		return panel;
	}
	
}
