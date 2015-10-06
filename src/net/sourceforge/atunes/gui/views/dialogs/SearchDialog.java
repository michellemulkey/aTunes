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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.kernel.modules.search.SearchFactory;
import net.sourceforge.atunes.model.search.Search;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class SearchDialog extends CustomDialog {

	private static final long serialVersionUID = 89888215541058798L;

	Search result;
	boolean setAsDefault;
	JCheckBox setAsDefaultCheckBox;
	
	public SearchDialog(JFrame owner) {
		super(owner, 300, 300, true);
		setContent();
	}
	
	private void setContent() {
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel icon = new JLabel(ImageLoader.NETWORK);
		JLabel text = new JLabel(LanguageTool.getString("SEARCH_AT") + "...");
		text.setFont(text.getFont().deriveFont(Font.PLAIN));
		final JList list = new JList(SearchFactory.getSearches().toArray());
		list.setSelectedIndex(0);
		list.setOpaque(false);
//		list.setCellRenderer(new ListCellRenderer() {
//			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//				Search s = (Search) value;
//				
//				JPanel auxPanel = new JPanel(new GridBagLayout());
//				auxPanel.setBorder(BorderFactory.createEmptyBorder());
//				
//				JLabel label = new JLabel(s.getName());
//				label.setHorizontalAlignment(JLabel.LEFT);
//
//				Font font = label.getFont();
//				label.setFont(font.deriveFont(Font.PLAIN));
//				
//				if (isSelected) {
//					label.setForeground(ColorDefinitions.TREE_AND_TABLE_SELECTED_FOREGROUND_COLOR);
//				}
//				else {
//					auxPanel.setOpaque(false);
//				}
//				
//				if (!isSelected) label.setOpaque(false);
//				
//				GridBagConstraints c = new GridBagConstraints();
//				c.gridx = 0; c.gridy = 0; c.insets = new Insets(0,5,0,5); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
//				auxPanel.add(label, c);
//				return auxPanel;
//			}
//		});

		setAsDefaultCheckBox = new JCheckBox(LanguageTool.getString("SET_AS_DEFAULT"));
		setAsDefaultCheckBox.setOpaque(false);
		setAsDefaultCheckBox.setFont(setAsDefaultCheckBox.getFont().deriveFont(Font.PLAIN));
		setAsDefaultCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(list);
		CustomButton okButton = new CustomButton(null, LanguageTool.getString("OK"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = (Search) list.getSelectedValue();
				setAsDefault = setAsDefaultCheckBox.isSelected();
				setVisible(false);
			}
		});
		CustomButton cancelButton = new CustomButton(null, LanguageTool.getString("CANCEL"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = null;
				setVisible(false);
			}
		});
		
		JPanel auxPanel = new JPanel();
		auxPanel.setOpaque(false);
		auxPanel.add(okButton);
		auxPanel.add(cancelButton);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0;  c.insets = new Insets(15,15,0,0);
		panel.add(icon, c);
		c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		panel.add(text, c);
		c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,15,0,15);
		panel.add(scrollPane, c);
		c.gridy = 2; c.weightx = 0; c.insets = new Insets(0,0,0,0);
		panel.add(setAsDefaultCheckBox, c);
		c.gridy = 3; c.weightx = 1;
		panel.add(auxPanel, c);
		
		setContent(panel);
	}
	
	public void setVisible(boolean b) {
		setAsDefaultCheckBox.setSelected(false);
		super.setVisible(b);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SearchDialog(null).setVisible(true);
	}

	public Search getResult() {
		return result;
	}

	public boolean isSetAsDefault() {
		return setAsDefault;
	}

	public void setSetAsDefaultVisible(boolean setAsDefaultVisible) {
		setAsDefaultCheckBox.setVisible(setAsDefaultVisible);
	}
}
