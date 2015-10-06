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
import java.util.HashMap;

import javax.swing.JCheckBox;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class PlayerPanel extends PreferencesPanel {

	private static final long serialVersionUID = 4489293347321979288L;

	private JCheckBox showSpectrum;
	
	public PlayerPanel() {
		super();
		showSpectrum = new JCheckBox(LanguageTool.getString("SHOW_SPECTRUM"));
		showSpectrum.setFont(FontSingleton.GENERAL_FONT);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTH;
		add(showSpectrum, c);
	}
	
	public HashMap<String, Object> getResult() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(LanguageTool.getString("SHOW_SPECTRUM"), showSpectrum.isSelected());
		return result;
	}
	
	public void setShowSpectrum(boolean show) {
		showSpectrum.setSelected(show);
	}
}
