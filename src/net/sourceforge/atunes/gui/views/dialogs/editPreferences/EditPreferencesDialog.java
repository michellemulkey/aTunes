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

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class EditPreferencesDialog extends CustomDialog {

	private static final long serialVersionUID = -4759149194433605946L;

	private JButton ok;
	private JButton cancel;
	
	private JPanel options;
	private JList list;
	
	public EditPreferencesDialog(JFrame owner) {
		super(owner, 750, 550, true);
		setContent(getContent());
		setUndecorated(false);
	}
	
	private JPanel getContent() {
		JPanel container = new JPanel(new GridBagLayout());
		container.setOpaque(false);
		list = new JList();
		JScrollPane scrollPane = new JScrollPane(list);
		//scrollPane.setPreferredSize(new Dimension(170,100));
		options = new JPanel();
		ok = new CustomButton(null, LanguageTool.getString("OK"));
		cancel = new CustomButton(null, LanguageTool.getString("CANCEL"));
		JPanel auxPanel = new JPanel();
		auxPanel.add(ok);
		auxPanel.add(cancel);
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 0.4; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,10,0,5);
		container.add(scrollPane, c);
		c.gridx = 1; c.gridy = 0; c.weightx = 0.6; c.insets = new Insets(10,5,0,10);
		container.add(options, c);
		c.gridx = 1; c.gridy = 1; c.weightx = 0; c.weighty = 0; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.EAST; c.insets = new Insets(10,0,10,10);
		container.add(auxPanel, c);
		
		return container;
	}
	
	public void showPanel(int index) {
		((CardLayout)options.getLayout()).show(options, Integer.toString(index));
	}
	
	public static void main(String[] args) {
		new EditPreferencesDialog(null).setVisible(true);
	}

	public void setListModel(DefaultListModel listModel) {
		list.setModel(listModel);
	}
	
	public void setPanels(PreferencesPanel[] panels) {
		options.setLayout(new CardLayout());
		for (int i = 0; i < panels.length; i++)
			options.add(Integer.toString(i), panels[i]);
	}

	public JList getList() {
		return list;
	}

	public JButton getCancel() {
		return cancel;
	}

	public JButton getOk() {
		return ok;
	}
	
	
	
}
