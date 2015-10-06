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
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class OSDPanel extends PreferencesPanel {

	private static final long serialVersionUID = 4489293347321979288L;

	private JCheckBox animateOSD;
	private JComboBox osdDuration;
	
	public OSDPanel() {
		super();
		animateOSD = new JCheckBox(LanguageTool.getString("ANIMATE_OSD"));
		animateOSD.setFont(FontSingleton.GENERAL_FONT);
		JLabel label = new JLabel(LanguageTool.getString("OSD_DURATION"));
		label.setFont(FontSingleton.GENERAL_FONT);
		osdDuration = new JComboBox(new Integer[] {2,4,6});
		osdDuration.setFont(FontSingleton.GENERAL_FONT);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTH;
		add(animateOSD, c);
		c.gridy = 1; c.weighty = 1; c.insets = new Insets(0,0,0,0);
		add(label, c);
		c.gridx = 1; c.insets = new Insets(0,0,0,0);
		add(osdDuration, c);
	}
	
	public HashMap<String, Object> getResult() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(LanguageTool.getString("ANIMATE_OSD"), animateOSD.isSelected());
		result.put(LanguageTool.getString("OSD_DURATION"), osdDuration.getSelectedItem());
		return result;
	}
	
	public void setAnimateOSD(boolean animate) {
		animateOSD.setSelected(animate);
	}
	
	public void setOSDDuration(int time) {
		osdDuration.setSelectedItem(time);
	}

}
