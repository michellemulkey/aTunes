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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class EditTagDialog extends CustomDialog {

	private static final long serialVersionUID = 3395292301087643037L;

	private JLabel titleLabel;
	private JTextField titleTextField;
	private JLabel albumLabel;
	private JTextField albumTextField;
	private JLabel artistLabel;
	private JTextField artistTextField;
	private JLabel yearLabel;
	private JTextField yearTextField;
	private JLabel genreLabel;
	private JComboBox genreComboBox;
	private JLabel commentLabel;
	private JTextArea commentTextArea;
	private JLabel trackNumberLabel;
	private JTextField trackNumberTextField;
	private JButton pictureButton;
	private JCheckBox removePictureCheckBox;
	
	private JButton okButton;
	private JButton cancelButton;
	
	public EditTagDialog(JFrame owner) {
		super(owner, 450, 300, true);
		setContent(getContent());
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel(new GridBagLayout());
		titleLabel = new JLabel(LanguageTool.getString("TITLE"));
		titleLabel.setFont(FontSingleton.GENERAL_FONT);
		titleTextField = new JTextField();
		albumLabel = new JLabel(LanguageTool.getString("ALBUM"));
		albumLabel.setFont(FontSingleton.GENERAL_FONT);
		albumTextField = new JTextField();
		artistLabel = new JLabel(LanguageTool.getString("ARTIST"));
		artistLabel.setFont(FontSingleton.GENERAL_FONT);
		artistTextField = new JTextField();
		yearLabel = new JLabel(LanguageTool.getString("YEAR"));
		yearLabel.setFont(FontSingleton.GENERAL_FONT);
		yearTextField = new JTextField();
		genreLabel = new JLabel(LanguageTool.getString("GENRE"));
		genreLabel.setFont(FontSingleton.GENERAL_FONT);
		genreComboBox = new JComboBox();
		genreComboBox.setFont(genreComboBox.getFont().deriveFont(Font.PLAIN));
		commentLabel = new JLabel(LanguageTool.getString("COMMENT"));
		commentLabel.setFont(FontSingleton.GENERAL_FONT);
		commentTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		trackNumberLabel = new JLabel(LanguageTool.getString("TRACK"));
		trackNumberLabel.setFont(FontSingleton.GENERAL_FONT);
		trackNumberTextField = new JTextField();
		removePictureCheckBox = new JCheckBox(LanguageTool.getString("PICTURE"));
		removePictureCheckBox.setFont(FontSingleton.GENERAL_FONT);
		removePictureCheckBox.setOpaque(false);
		pictureButton = new CustomButton(null, null);
		pictureButton.setPreferredSize(new Dimension(100,100));
		
		okButton = new CustomButton(null, LanguageTool.getString("OK"));
		cancelButton = new CustomButton(null, LanguageTool.getString("CANCEL"));
		
		GridBagConstraints c = new GridBagConstraints();  c.insets = new Insets(10,10,2,10);
		c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.fill = GridBagConstraints.NONE; c.weightx = 0; c.anchor = GridBagConstraints.NORTHWEST;
		panel.add(titleLabel, c);
		
		c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
		panel.add(titleTextField, c); 
		
		c.gridx = 2; c.weightx = 0;
		panel.add(removePictureCheckBox, c);
		
		c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.NONE; c.weightx = 0;  c.insets = new Insets(2,10,2,10);
		panel.add(artistLabel, c);
		
		c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
		panel.add(artistTextField, c);
		
		c.gridx = 2; c.weightx = 0.4; c.fill = GridBagConstraints.BOTH; c.gridheight = 5;
		panel.add(pictureButton, c);
		
		c.gridx = 0; c.gridy = 2; c.fill = GridBagConstraints.NONE; c.weightx = 0; c.gridheight = 1;
		panel.add(albumLabel, c);
		
		c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
		panel.add(albumTextField, c);
		
		c.gridx = 0; c.gridy = 3; c.fill = GridBagConstraints.NONE; c.weightx = 0;
		panel.add(yearLabel, c);
		
		c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
		panel.add(yearTextField, c);
		
		c.gridx = 0; c.gridy = 4;  c.fill = GridBagConstraints.NONE; c.weightx = 0;
		panel.add(trackNumberLabel, c);
		
		c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
		panel.add(trackNumberTextField, c);
		
		c.gridx = 0; c.gridy = 5; c.fill = GridBagConstraints.NONE; c.weightx = 0;
		panel.add(genreLabel, c);
		
		c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
		panel.add(genreComboBox, c);
		
		c.gridx = 0; c.gridy = 6; c.fill = GridBagConstraints.NONE; c.weightx = 0;
		panel.add(commentLabel, c);
		
		c.gridx = 1; c.fill = GridBagConstraints.BOTH; c.weightx = 1; c.weighty = 1; c.gridwidth = 2;
		panel.add(scrollPane, c);

		JPanel auxPanel = new JPanel();
		auxPanel.setOpaque(false);
		auxPanel.add(okButton);
		auxPanel.add(cancelButton);
		
		c.gridx = 0; c.gridy = 7; c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1; c.weighty = 0;
		panel.add(auxPanel, c);
		
		return panel;
	}
	
	public static void main(String[] args) {
		new EditTagDialog(null).setVisible(true);
	}

	public JLabel getAlbumLabel() {
		return albumLabel;
	}

	public JTextField getAlbumTextField() {
		return albumTextField;
	}

	public JLabel getArtistLabel() {
		return artistLabel;
	}

	public JTextField getArtistTextField() {
		return artistTextField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JLabel getCommentLabel() {
		return commentLabel;
	}

	public JTextArea getCommentTextArea() {
		return commentTextArea;
	}

	public JComboBox getGenreComboBox() {
		return genreComboBox;
	}

	public JLabel getGenreLabel() {
		return genreLabel;
	}

	public JButton getOkButton() {
		return okButton;
	}

	public JLabel getTitleLabel() {
		return titleLabel;
	}

	public JTextField getTitleTextField() {
		return titleTextField;
	}

	public JLabel getTrackNumberLabel() {
		return trackNumberLabel;
	}

	public JTextField getTrackNumberTextField() {
		return trackNumberTextField;
	}

	public JLabel getYearLabel() {
		return yearLabel;
	}

	public JTextField getYearTextField() {
		return yearTextField;
	}

	public JButton getPictureButton() {
		return pictureButton;
	}

	public JCheckBox getRemovePictureCheckBox() {
		return removePictureCheckBox;
	}
}
