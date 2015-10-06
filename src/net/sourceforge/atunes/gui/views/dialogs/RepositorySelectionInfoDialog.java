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

package net.sourceforge.atunes.gui.views.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class RepositorySelectionInfoDialog extends CustomDialog {

	private static final long serialVersionUID = 4369595555397951445L;

	public RepositorySelectionInfoDialog(JFrame owner) {
		super(owner, 400, 250, true);
		setTitle(LanguageTool.getString("REPOSITORY_SELECTION_INFO"));
		setContent();
	}
	
	private void setContent() {
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel icon = new JLabel(ImageLoader.APP_ICON_TITLE);
		JTextArea text = new JTextArea(LanguageTool.getString("REPOSITORY_SELECTION_INFO_TEXT"));
		text.setOpaque(false);
		text.setEditable(false);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		text.setBorder(BorderFactory.createEmptyBorder());
		CustomButton button = new CustomButton(null, LanguageTool.getString("OK"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0;  c.anchor = GridBagConstraints.NORTH; c.insets = new Insets(10,10,10,10);
		panel.add(icon, c);
		c.gridx = 1; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		panel.add(text, c);
		c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.CENTER; new Insets(0,0,0,0);
		panel.add(button, c);
		setContent(panel);
	}
}
