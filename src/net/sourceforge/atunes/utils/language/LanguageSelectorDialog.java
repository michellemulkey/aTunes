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

package net.sourceforge.atunes.utils.language;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.CustomButton;


public class LanguageSelectorDialog extends JDialog {

	private static final long serialVersionUID = 8846024391499257859L;

	String selection;
	JList list;
	
	private static JFrame f = getFrame(); 
	
	public LanguageSelectorDialog(String[] languages) {
		super(f, "Select Language", true);
		setSize(250,250);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		list = new JList(languages);
		list.setFont(list.getFont().deriveFont(Font.PLAIN));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(10,10,10,10); c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		panel.add(list, c);

		JButton okButton = new CustomButton(null, "Ok");
		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		okButton.setFont(f);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selection = (String) list.getSelectedValue();
				setVisible(false);
			}
		});
		c.gridx = 0; c.gridy = 1; c.weightx = 0; c.weighty = 0; c.fill = GridBagConstraints.NONE;
		panel.add(okButton, c);
		
		list.setSelectedIndex(0);
		add(panel);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private static JFrame getFrame() {
		JFrame f = new JFrame();
		f.setIconImage(ImageLoader.LANGUAGE.getImage());
		return f;
	}
	
	public String getSelection() {
		return selection;
	}
	
	public static void main(String[] args) {
		new LanguageSelectorDialog(new String[] {"ES", "SP"});
	}
}
