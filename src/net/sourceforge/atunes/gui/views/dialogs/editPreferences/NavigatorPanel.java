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

package net.sourceforge.atunes.gui.views.dialogs.editPreferences;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class NavigatorPanel extends PreferencesPanel {

	private static final long serialVersionUID = -4315748284461119970L;
	private JCheckBox showFavorites;
	JCheckBox showAlbumToolTip;
	JComboBox albumToolTipDelay;
	
	public NavigatorPanel() {
		super();
		showFavorites = new JCheckBox(LanguageTool.getString("SHOW_FAVORITES"));
		showFavorites.setFont(FontSingleton.GENERAL_FONT);
		showAlbumToolTip = new JCheckBox(LanguageTool.getString("SHOW_ALBUM_TOOLTIP"));
		showAlbumToolTip.setFont(FontSingleton.GENERAL_FONT);
		final JLabel label = new JLabel(LanguageTool.getString("ALBUM_TOOLTIP_DELAY"));
		label.setFont(FontSingleton.GENERAL_FONT);
		albumToolTipDelay = new JComboBox(new Integer[] {1, 2, 3, 4, 5});
		albumToolTipDelay.setFont(FontSingleton.GENERAL_FONT);
		showAlbumToolTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				label.setEnabled(showAlbumToolTip.isSelected());
				albumToolTipDelay.setEnabled(showAlbumToolTip.isSelected());
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTH;
		add(showFavorites, c);
		c.gridy = 1;
		add(showAlbumToolTip, c);
		c.gridy = 2; c.weighty = 1; c.insets = new Insets(0,30,0,0);
		add(label, c);
		c.gridx = 1; c.insets = new Insets(0,0,0,0);
		add(albumToolTipDelay, c);

	}
	
	public HashMap<String, Object> getResult() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(LanguageTool.getString("SHOW_FAVORITES"), showFavorites.isSelected());
		result.put(LanguageTool.getString("SHOW_ALBUM_TOOLTIP"), showAlbumToolTip.isSelected());
		result.put(LanguageTool.getString("ALBUM_TOOLTIP_DELAY"), albumToolTipDelay.getSelectedItem());
		return result;
	}
	
	public void setShowFavorites(boolean show) {
		showFavorites.setSelected(show);
	}
	
	public void setShowAlbumToolTip(boolean show) {
		showAlbumToolTip.setSelected(show);
	}
	
	public void setAlbumToolTipDelay(int time) {
		albumToolTipDelay.setSelectedItem(time);
	}

}
