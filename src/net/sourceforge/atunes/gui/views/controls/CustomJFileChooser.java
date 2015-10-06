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

package net.sourceforge.atunes.gui.views.controls;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author fleax
 *
 */
public class CustomJFileChooser extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4713483251093570020L;
	JTextField textField;
	private JButton button;
	String result;
	int fileChooserType;
	
	public CustomJFileChooser(int length, int type) {
		super(new GridBagLayout());
		fileChooserType = type;
		textField = new JTextField(length);
		button = new JButton("...");
		button.setSize(19,19);
		button.setPreferredSize(button.getSize());
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(textField.getText());
				chooser.setFileSelectionMode(fileChooserType);
				if (chooser.showDialog(new JPanel().getRootPane(), null) == JFileChooser.APPROVE_OPTION) {
					result = chooser.getSelectedFile().getAbsolutePath();
					textField.setText(result);
				}
				else
					result = null;
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(textField, c);
		c.gridx = 1;
		add(button, c);
	}
	
	public String getResult() {
		result = textField.getText();
		return result;
	}
	
	public void setText(String text) {
		textField.setText(text);
		result = text;
	}


}
