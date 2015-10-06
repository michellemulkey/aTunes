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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.utils.language.LanguageTool;


/**
 * @author fleax
 *
 */
public class ProgressDialog extends CustomDialog {
	
	private static final long serialVersionUID = -3071934230042256578L;

	private JLabel pictureLabel;
	private JLabel label;
	private JLabel progressLabel;
	private JLabel totalFilesLabel;
	private JProgressBar progressBar;
	private JLabel folderLabel;
	private JLabel remainingTimeLabel;
	private JButton cancelButton;
	
	public ProgressDialog(JFrame parent) {
		super(parent, 400, 130, false);
		setContent(getContent());
		cancelButton.setVisible(false);
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel(new GridBagLayout());
		pictureLabel = new JLabel(ImageLoader.APP_ICON_TITLE);
		label = new JLabel(LanguageTool.getString("LOADING") + "...");
		Font f = label.getFont().deriveFont(Font.PLAIN);
		label.setFont(f);
		progressLabel = new JLabel();
		progressLabel.setFont(f);
		totalFilesLabel = new JLabel();
		totalFilesLabel.setFont(f);
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		folderLabel = new JLabel(" ");
		folderLabel.setFont(f);
		remainingTimeLabel = new JLabel(" ");
		remainingTimeLabel.setFont(f);
		cancelButton = new CustomButton(null, LanguageTool.getString("CANCEL"));
		cancelButton.setFont(f);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.gridheight = 5; c.insets = new Insets(0,20,0,0);
		panel.add(pictureLabel, c);
		c.gridheight = 1;
		c.gridx = 1; c.gridy = 0; c.weightx = 1; c.fill =  GridBagConstraints.HORIZONTAL; c.insets = new Insets(5,20,0,20);
		c.anchor = GridBagConstraints.WEST;
		panel.add(label, c);
		c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE; c.insets = new Insets(5,0,0,3); c.anchor = GridBagConstraints.EAST;
		panel.add(progressLabel, c);
		c.gridx = 3; c.insets = new Insets(5,0,0,20);
		panel.add(totalFilesLabel, c);
		c.gridx = 1; c.gridy = 1; c.weightx = 1; c.gridwidth = 3; c.fill =  GridBagConstraints.HORIZONTAL; c.insets = new Insets(5,20,5,20);
		c.anchor = GridBagConstraints.CENTER;
		panel.add(progressBar, c);
		c.gridy = 2; c.insets = new Insets(0,20,0,20); c.fill = GridBagConstraints.VERTICAL; c.anchor = GridBagConstraints.WEST;
		panel.add(folderLabel, c);
		c.gridy = 3; c.insets = new Insets(0,20,10,20);
		panel.add(remainingTimeLabel, c);
		c.gridy = 4; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.CENTER;
		panel.add(cancelButton, c);
		return panel;
	}
	
	private void clear() {
		getLabel().setText(LanguageTool.getString("LOADING") + "...");
		getFolderLabel().setText(" ");
		getProgressBar().setValue(0);
		getProgressLabel().setText("");
		getTotalFilesLabel().setText("");
		getProgressBar().setIndeterminate(true);
		getRemainingTimeLabel().setText(" ");
	}
	
	public void setVisible(final boolean b) {
		super.setVisible(b);
		clear();
	}
	
	public static void main(String[] args) {
		new ProgressDialog(null).setVisible(true);
	}

	public JLabel getLabel() {
		return label;
	}

	public JLabel getProgressLabel() {
		return progressLabel;
	}

	public JLabel getTotalFilesLabel() {
		return totalFilesLabel;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JLabel getFolderLabel() {
		return folderLabel;
	}
	
	public void setCancelButtonVisible(boolean visible) {
		cancelButton.setVisible(visible);
	}
	
	public void setCancelButtonEnabled(boolean enabled) {
		cancelButton.setEnabled(enabled);
	}
	
	public JButton getCancelButton() {
		return cancelButton;
	}

	public JLabel getRemainingTimeLabel() {
		return remainingTimeLabel;
	}

}
