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

package net.sourceforge.atunes.gui;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class ToolBar extends JToolBar {
	
	private static final long serialVersionUID = 3146746580753998589L;
	private JButton selectRepository;
	private JButton refreshRepository;
	private JButton preferences;
	private JToggleButton showNavigator;
	private JToggleButton showFileProperties;
	private JToggleButton showAudioScrobbler;
	private JButton stats;
	private JButton ripCD;
	
	public ToolBar() {
		super();
		setFloatable(false);
		setButtons();
	}
	
	private void setButtons() {
		selectRepository = new JButton(ImageLoader.FOLDER);
		selectRepository.setToolTipText(LanguageTool.getString("SELECT_REPOSITORY"));
		add(selectRepository);
		refreshRepository = new JButton(ImageLoader.REFRESH);
		refreshRepository.setToolTipText(LanguageTool.getString("REFRESH_REPOSITORY"));
		add(refreshRepository);
		addSeparator();
		preferences = new JButton(ImageLoader.PREFS);
		preferences.setToolTipText(LanguageTool.getString("PREFERENCES"));
		add(preferences);
		addSeparator();
		showNavigator = new JToggleButton(ImageLoader.NAVIGATE);
		showNavigator.setToolTipText(LanguageTool.getString("SHOW_NAVIGATION_PANEL"));
		add(showNavigator);
		showFileProperties = new JToggleButton(ImageLoader.INFO);
		showFileProperties.setToolTipText(LanguageTool.getString("SHOW_SONG_PROPERTIES"));
		add(showFileProperties);
		showAudioScrobbler = new JToggleButton(ImageLoader.NETWORK_LITTLE);
		showAudioScrobbler.setToolTipText(LanguageTool.getString("SHOW_AUDIOSCROBBLER"));
		add(showAudioScrobbler);
		addSeparator();
		stats = new JButton(ImageLoader.STATS);
		stats.setToolTipText(LanguageTool.getString("STATS"));
		add(stats);
		addSeparator();
		ripCD = new JButton(ImageLoader.CD_AUDIO_TINY);
		ripCD.setToolTipText(LanguageTool.getString("RIP_CD"));
		add(ripCD);
	}

	public JButton getPreferences() {
		return preferences;
	}

	public JButton getRefreshRepository() {
		return refreshRepository;
	}

	public JButton getRipCD() {
		return ripCD;
	}

	public JButton getSelectRepository() {
		return selectRepository;
	}

	public JToggleButton getShowFileProperties() {
		return showFileProperties;
	}

	public JToggleButton getShowNavigator() {
		return showNavigator;
	}

	public JButton getStats() {
		return stats;
	}

	public JToggleButton getShowAudioScrobbler() {
		return showAudioScrobbler;
	}

}
