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
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.LookAndFeelSelector;
import net.sourceforge.atunes.gui.images.themes.ThemePreviewLoader;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class GeneralPanel extends PreferencesPanel {

	private static final long serialVersionUID = -9216216930198145476L;
	private JCheckBox showTitle;
	private JComboBox windowType;
	private JComboBox language;
	private JCheckBox showIconTray;
	private JCheckBox showTrayPlayer;
	JComboBox theme;
	JLabel themePreview;
	
	public GeneralPanel() {
		super();
		showTitle = new JCheckBox(LanguageTool.getString("SHOW_TITLE"));
		showTitle.setFont(FontSingleton.GENERAL_FONT);
		JLabel label = new JLabel(LanguageTool.getString("WINDOW_TYPE"));
		label.setFont(FontSingleton.GENERAL_FONT);
		windowType = new JComboBox(new String[] {LanguageTool.getString("STANDARD_WINDOW"), LanguageTool.getString("MULTIPLE_WINDOW")});
		windowType.setFont(FontSingleton.GENERAL_FONT);
		JLabel label2 = new JLabel(LanguageTool.getString("CHANGE_WINDOW_TYPE_ON_NEXT_START"));
		label2.setFont(FontSingleton.GENERAL_FONT);
		JLabel label3 = new JLabel(LanguageTool.getString("LANGUAGE"));
		label3.setFont(FontSingleton.GENERAL_FONT);
		ArrayList<String> langs = LanguageTool.getLanguages(); 
		language = new JComboBox(langs.toArray(new String[langs.size()]));
		language.setFont(FontSingleton.GENERAL_FONT);
		JLabel label4 = new JLabel(LanguageTool.getString("CHANGE_LANGUAGE_ON_NEXT_START"));
		label4.setFont(FontSingleton.GENERAL_FONT);
		showIconTray = new JCheckBox(LanguageTool.getString("SHOW_TRAY_ICON"));
		showIconTray.setFont(FontSingleton.GENERAL_FONT);
		showTrayPlayer = new JCheckBox(LanguageTool.getString("SHOW_TRAY_PLAYER"));
		showTrayPlayer.setFont(FontSingleton.GENERAL_FONT);
		JLabel label5 = new JLabel(LanguageTool.getString("THEME"));
		label5.setFont(FontSingleton.GENERAL_FONT);
		theme = new JComboBox(LookAndFeelSelector.getListOfThemes());
		theme.setFont(FontSingleton.GENERAL_FONT);
		theme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String t = (String) theme.getSelectedItem();
				themePreview.setIcon(ThemePreviewLoader.getImage(t));
			}
		});
		themePreview = new JLabel(LanguageTool.getString("PREVIEW"));
		themePreview.setFont(FontSingleton.GENERAL_FONT);
		themePreview.setVerticalTextPosition(SwingConstants.TOP);
		themePreview.setHorizontalTextPosition(SwingConstants.CENTER);
		JLabel label6 = new JLabel(LanguageTool.getString("CHANGE_THEME_ON_NEXT_START"));
		label6.setFont(FontSingleton.GENERAL_FONT);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTH; c.insets = new Insets(0,0,5,0);
		add(showTitle, c);
		c.gridy = 1; c.insets = new Insets(0,0,0,0);
		add(label, c); 
		c.gridx = 1; c.insets = new Insets(0,0,0,0);
		add(windowType, c);
		c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.insets = new Insets(5,0,0,0);
		add(label2, c);
		c.gridy = 3; c.gridwidth = 1; c.insets = new Insets(15,0,0,0);
		add(label3, c);
		c.gridx = 1; c.insets = new Insets(15,0,0,0);
		add(language, c);
		c.gridx = 0; c.gridy = 4; c.gridwidth = 2; c.insets = new Insets(5,0,0,0);
		add(label4, c);
		c.gridx = 0; c.gridy = 5; c.gridwidth = 1; c.insets = new Insets(15,0,0,0); 
		add(showIconTray, c);
		c.gridy = 6; c.insets = new Insets(5,0,0,0);
		add(showTrayPlayer, c);
		c.gridy = 7; c.gridwidth = 2; c.insets = new Insets(10,0,0,0);
		add(label5, c);
		c.gridx = 1; 
		add(theme, c);
		c.gridx = 0; c.gridy = 8; c.weighty = 1;
		add(themePreview, c);
		c.gridy = 9; c.weighty = 0;
		add(label6, c);
	}
	
	public HashMap<String, Object> getResult() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(LanguageTool.getString("SHOW_TITLE"), showTitle.isSelected());
		result.put(LanguageTool.getString("WINDOW_TYPE"), windowType.getSelectedItem());
		result.put(LanguageTool.getString("LANGUAGE"), language.getSelectedItem());
		result.put(LanguageTool.getString("SHOW_TRAY_ICON"), showIconTray.isSelected());
		result.put(LanguageTool.getString("SHOW_TRAY_PLAYER"), showTrayPlayer.isSelected());
		result.put(LanguageTool.getString("THEME"), theme.getSelectedItem());
		return result;
	}
	
	public void setShowTitle(boolean show) {
		showTitle.setSelected(show);
	}
	
	public void setWindowType(String type) {
		windowType.setSelectedItem(type);
	}
	
	public void setLanguage(String language) {
		this.language.setSelectedItem(language);
	}
	
	public void setShowIconTray(boolean show) {
		this.showIconTray.setSelected(show);
	}

	public void setShowTrayPlayer(boolean show) {
		this.showTrayPlayer.setSelected(show);
	}
	
	public void setTheme(String t) {
		theme.setSelectedItem(t);
		themePreview.setIcon(ThemePreviewLoader.getImage(t));
	}
}
