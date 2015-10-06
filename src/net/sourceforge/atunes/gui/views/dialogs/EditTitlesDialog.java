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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class EditTitlesDialog extends CustomDialog {
	
	private static final long serialVersionUID = -7937735545263913179L;

	private JTable table;
	private JButton retrieveFromAmazon;
	private JButton okButton;
	private JButton cancelButton;
	
	public EditTitlesDialog(JFrame owner) {
		super(owner, 500, 400, true);
		setContent(getContent());
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		
		table = new JTable();
		table.setOpaque(false);
		JScrollPane scrollPane = new JScrollPane(table);
		retrieveFromAmazon = new CustomButton(null, LanguageTool.getString("GET_TITLES_FROM_AMAZON"));
		okButton = new CustomButton(null, LanguageTool.getString("OK"));
		cancelButton = new CustomButton(null, LanguageTool.getString("CANCEL"));

		GridBagConstraints c = new GridBagConstraints();

		JPanel auxPanel = new JPanel(new GridBagLayout());
		auxPanel.setOpaque(false);
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.WEST;
		auxPanel.add(retrieveFromAmazon, c);
		c.gridx = 1; c.weightx = 0; c.anchor = GridBagConstraints.CENTER; c.insets = new Insets(0,0,0,10);
		auxPanel.add(okButton, c);
		c.gridx = 2; c.insets = new Insets(0,0,0,0);
		auxPanel.add(cancelButton, c); 
		
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,10,10,10);
		panel.add(scrollPane, c);
		
		c.gridy = 1; c.weighty = 0; c.insets = new Insets(0,20,10,20);
		panel.add(auxPanel, c);
		
		return panel;
	}
	
	public static void main(String[] args) {
		new EditTitlesDialog(null).setVisible(true);
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getOkButton() {
		return okButton;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getRetrieveFromAmazon() {
		return retrieveFromAmazon;
	}

}
