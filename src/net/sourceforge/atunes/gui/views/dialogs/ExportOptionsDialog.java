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
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.gui.views.controls.CustomJFileChooser;
import net.sourceforge.atunes.utils.language.LanguageTool;


/**
 * @author fleax
 *
 */
public class ExportOptionsDialog extends CustomDialog {

	private static final long serialVersionUID = 4403091324599627762L;

	private JRadioButton allTypeRadioButton;
	private JRadioButton selectionTypeRadioButton;
	private JRadioButton favoritesTypeRadioButton;
	
	private CustomJFileChooser exportLocation;  
	
	private JRadioButton flatStructureRadioButton;
	private JRadioButton artistStructureRadioButton;
	private JRadioButton fullStructureRadioButton;
	
	private JRadioButton noChangeFileNamesRadioButton;
	private JRadioButton customFileNamesRadioButton;
	private JTextField customFileNameFormatTextField;
	private JLabel formatInfoLabel;
	
	private JButton exportButton;
	private JButton cancelButton;
	
	public ExportOptionsDialog(JFrame parent) {
		super(parent, 500, 350, true);
		setContent(getContent());
	}
	
	private JPanel getContent() {
		JPanel container = new JPanel(new GridBagLayout());
		container.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		// Export Type
		JPanel exportTypePanel = new JPanel(new GridLayout(3,1));
		allTypeRadioButton = new JRadioButton(LanguageTool.getString("ALL"));
		selectionTypeRadioButton = new JRadioButton(LanguageTool.getString("CURRENT_SELECTION"));
		favoritesTypeRadioButton = new JRadioButton(LanguageTool.getString("FAVORITES"));
		ButtonGroup group = new ButtonGroup();
		group.add(allTypeRadioButton);
		group.add(selectionTypeRadioButton);
		group.add(favoritesTypeRadioButton);
		exportTypePanel.add(allTypeRadioButton);
		exportTypePanel.add(selectionTypeRadioButton);
		exportTypePanel.add(favoritesTypeRadioButton);
		allTypeRadioButton.setSelected(true);
		
		// Export location
		JPanel exportLocationPanel = new JPanel();
		exportLocationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), LanguageTool.getString("LOCATION")));
		exportLocation = new CustomJFileChooser(30, JFileChooser.DIRECTORIES_ONLY);
		exportLocationPanel.add(exportLocation);
		
		// Directory Structure
		JPanel exportStructurePanel = new JPanel(new GridLayout(3,1));
		exportStructurePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), LanguageTool.getString("STRUCTURE")));
		flatStructureRadioButton = new JRadioButton(LanguageTool.getString("FLAT"));
		artistStructureRadioButton = new JRadioButton(LanguageTool.getString("ARTIST"));
		fullStructureRadioButton = new JRadioButton(LanguageTool.getString("ARTIST") + " / " + LanguageTool.getString("ALBUM"));
		ButtonGroup group2 = new ButtonGroup();
		group2.add(flatStructureRadioButton);
		group2.add(artistStructureRadioButton);
		group2.add(fullStructureRadioButton);
		exportStructurePanel.add(flatStructureRadioButton);
		exportStructurePanel.add(artistStructureRadioButton);
		exportStructurePanel.add(fullStructureRadioButton);
		fullStructureRadioButton.setSelected(true);

		// File Names
		JPanel exportFileNamesPanel = new JPanel(new GridBagLayout());
		exportFileNamesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), LanguageTool.getString("FILE_NAME")));
		noChangeFileNamesRadioButton = new JRadioButton(LanguageTool.getString("NO_CHANGE"));
		customFileNamesRadioButton = new JRadioButton(LanguageTool.getString("CUSTOM") + "...");
		customFileNameFormatTextField = new JTextField(10);
		formatInfoLabel = new JLabel("%T= " + LanguageTool.getString("TITLE") + "           %A= " + LanguageTool.getString("ARTIST") + "            %L= " + LanguageTool.getString("ALBUM"));
		ButtonGroup group3 = new ButtonGroup();
		group3.add(noChangeFileNamesRadioButton);
		group3.add(customFileNamesRadioButton);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		exportFileNamesPanel.add(noChangeFileNamesRadioButton, c);
		c.gridx = 0; c.gridy = 1; c.gridwidth = 2;
		exportFileNamesPanel.add(customFileNamesRadioButton, c);
		c.gridx = 2; c.gridy = 1; c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		exportFileNamesPanel.add(customFileNameFormatTextField, c);
		c.gridx = 2; c.gridy = 2; c.gridwidth = 5;
		exportFileNamesPanel.add(formatInfoLabel, c);
		noChangeFileNamesRadioButton.setSelected(true);
		
		// Buttons
		JPanel buttons = new JPanel(new GridLayout(1,2));
		buttons.setOpaque(false);
		exportButton = new CustomButton(null, LanguageTool.getString("EXPORT"));
		cancelButton = new CustomButton(null, LanguageTool.getString("CANCEL"));
		buttons.add(exportButton);
		buttons.add(cancelButton);
		
		c.gridx = 0;   c.gridy = 0; c.gridwidth = 2;
		c.weightx = 1; c.weighty = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(exportLocationPanel, c);

		c.gridx = 0; c.gridy = 1; c.gridwidth = 1;
		c.weightx = 0.5; c.weighty = 0.6;
		c.fill = GridBagConstraints.BOTH;
		container.add(exportTypePanel, c);
		
		c.gridx = 1; c.gridy = 1; c.gridwidth = 1;
		c.weightx = 0.5; c.weighty = 0.6;
		c.fill = GridBagConstraints.BOTH;
		container.add(exportStructurePanel, c);
		
		c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
		c.weightx = 1; c.weighty = 0.2;
		c.fill = GridBagConstraints.BOTH;
		container.add(exportFileNamesPanel, c);
		
		c.gridx = 1; c.gridy = 3; c.gridwidth = 1;
		c.weightx = 0; c.weighty = 0;
		c.insets = new Insets(10,0,0,0);
		container.add(buttons, c);
		
		return container;
	}

	public JRadioButton getAllTypeRadioButton() {
		return allTypeRadioButton;
	}

	public JRadioButton getArtistStructureRadioButton() {
		return artistStructureRadioButton;
	}

	public JRadioButton getSelectionTypeRadioButton() {
		return selectionTypeRadioButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JTextField getCustomFileNameFormatTextField() {
		return customFileNameFormatTextField;
	}

	public JRadioButton getCustomFileNamesRadioButton() {
		return customFileNamesRadioButton;
	}

	public JButton getExportButton() {
		return exportButton;
	}

	public CustomJFileChooser getExportLocation() {
		return exportLocation;
	}

	public JRadioButton getFlatStructureRadioButton() {
		return flatStructureRadioButton;
	}

	public JLabel getFormatInfoLabel() {
		return formatInfoLabel;
	}

	public JRadioButton getFullStructureRadioButton() {
		return fullStructureRadioButton;
	}

	public JRadioButton getNoChangeFileNamesRadioButton() {
		return noChangeFileNamesRadioButton;
	}
	
	public static void main(String [] args) {
		new ExportOptionsDialog(null).setVisible(true);
	}

	public JRadioButton getFavoritesTypeRadioButton() {
		return favoritesTypeRadioButton;
	}
}
