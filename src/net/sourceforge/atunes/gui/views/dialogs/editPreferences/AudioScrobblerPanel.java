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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JCheckBox;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class AudioScrobblerPanel extends PreferencesPanel {

	private static final long serialVersionUID = -9216216930198145476L;
	
	JCheckBox activateAudioScrobbler;
	JCheckBox savePictures;
	
	public AudioScrobblerPanel() {
		super();
		activateAudioScrobbler = new JCheckBox(LanguageTool.getString("ACTIVATE_AUDIO_SCROBBLER"));
		activateAudioScrobbler.setFont(FontSingleton.GENERAL_FONT);
		savePictures = new JCheckBox(LanguageTool.getString("SAVE_PICTURES_TO_AUDIO_FOLDERS"));
		savePictures.setFont(FontSingleton.GENERAL_FONT);
		activateAudioScrobbler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				savePictures.setEnabled(activateAudioScrobbler.isSelected());
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.NORTH;
		add(activateAudioScrobbler, c);
		c.gridy = 1; c.weighty = 1;
		add(savePictures, c);
	}
	
	public HashMap<String, Object> getResult() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(LanguageTool.getString("ACTIVATE_AUDIO_SCROBBLER"), activateAudioScrobbler.isSelected());
		result.put(LanguageTool.getString("SAVE_PICTURES_TO_AUDIO_FOLDERS"), savePictures.isSelected());
		return result;
	}

	public void setActivateAudioScrobbler(boolean activate) {
		activateAudioScrobbler.setSelected(activate);
		savePictures.setEnabled(activate);
	}
	
	public void setSavePictures(boolean save) {
		savePictures.setSelected(save);
	}
}
