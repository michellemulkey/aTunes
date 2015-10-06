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

import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class RepositoryPanel extends PreferencesPanel {

	private static final long serialVersionUID = 3331810461314007217L;

	private JComboBox comboBox;
	
	public RepositoryPanel() {
		super();
		JLabel label = new JLabel(LanguageTool.getString("REPOSITORY_REFRESH_TIME"));
		label.setFont(FontSingleton.GENERAL_FONT);
		comboBox = new JComboBox(new Integer[] {0, 5, 10, 15, 30, 60});
		comboBox.setFont(FontSingleton.GENERAL_FONT);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTH;
		add(label, c);
		c.gridx = 1;
		add(comboBox, c);
	}
	
	public HashMap<String, Object> getResult() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(LanguageTool.getString("REPOSITORY_REFRESH_TIME"), comboBox.getSelectedItem());
		return result;
	}
	
	public void setRefreshTime(int time) {
		comboBox.setSelectedItem(time);
	}

}
