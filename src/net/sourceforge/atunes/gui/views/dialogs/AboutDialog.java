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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.gui.views.controls.UrlLabel;
import net.sourceforge.atunes.utils.language.LanguageTool;

import com.fleax.ant.BuildNumberReader;

public class AboutDialog extends CustomDialog {

	private static final long serialVersionUID = 8666235475424750562L;

	private String licenseText = "Copyright (C) 2006  Alex Aranda (Fleax) alex.aranda@gmail.com\n\n" +
								 "This program is free software; you can redistribute it and/or " + 
								 "modify it under the terms of the GNU General Public License " + 
								 "as published by the Free Software Foundation; either version 2 " +
								 "of the License, or (at your option) any later version.\n\n" + 
								 "This program is distributed in the hope that it will be useful, " + 
								 "but WITHOUT ANY WARRANTY; without even the implied warranty of" +
								 "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the " +
								 "GNU General PublicLicense for more details.\n\n" + 
								 "You should have received a copy of the GNU General Public License " +
								 "along with this program; if not, write to the\n\nFree Software " +
								 "Foundation, Inc.\n51 Franklin Street, Fifth Floor\nBoston, MA\n02110-1301, USA";
	
	public AboutDialog(JFrame owner) {
		super(owner, 500, 600, true);
		setContent(getContent());
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel title = new JLabel(Constants.APP_NAME + ' ' + Constants.APP_VERSION_NUMBER);
		title.setFont(FontSingleton.GENERAL_FONT.deriveFont(20f));
		JLabel description = new JLabel(Constants.APP_DESCRIPTION);
		description.setFont(FontSingleton.GENERAL_FONT);
		
		JTextArea license = new JTextArea(licenseText);
		license.setEditable(false);
		license.setOpaque(false);
		license.setLineWrap(true);
		license.setWrapStyleWord(true);
		license.setBorder(BorderFactory.createEmptyBorder());
		
		UrlLabel url = new UrlLabel(Constants.APP_WEB);
		url.setUrl(Constants.APP_WEB);
		url.setFont(FontSingleton.GENERAL_FONT);
		UrlLabel url2 = new UrlLabel(Constants.APP_SOURCEFORGE_WEB);
		url2.setUrl(Constants.APP_SOURCEFORGE_WEB);
		url2.setFont(FontSingleton.GENERAL_FONT);
		
		JTable propertiesTable = new JTable(new AboutDialogTableModel());
		JScrollPane scrollPane = new JScrollPane(propertiesTable);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		JButton close = new CustomButton(null, LanguageTool.getString("CLOSE"));
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weighty = 0.1;
		panel.add(title, c);
		c.gridy = 1; c.weighty = 0.1; c.anchor = GridBagConstraints.NORTH;
		panel.add(description, c);
		c.gridy = 2; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(0,20,0,20);
		panel.add(license, c);
		c.gridy = 3; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.CENTER;
		panel.add(url, c);
		c.gridy = 4;
		panel.add(url2, c);
		c.gridy = 5; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,20,10,20);
		panel.add(scrollPane, c);
		c.gridy = 6; c.fill = GridBagConstraints.NONE; c.weighty = 0; c.insets = new Insets(0,20,10,20);
		panel.add(close, c);
		
		return panel;
	}
	
	private class AboutDialogTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1786557125033788184L;
		private ArrayList<String[]> valuesToShow;
		
		AboutDialogTableModel() {
			valuesToShow = new ArrayList<String[]>();
			valuesToShow.add(new String[] {"Version", Constants.APP_VERSION_NUMBER});
			valuesToShow.add(new String[] {"Build Number", Integer.toString(BuildNumberReader.getBuildNumber())});
			valuesToShow.add(new String[] {"Build Date", new SimpleDateFormat("dd/MM/yyyy").format(BuildNumberReader.getBuildDate())});
			valuesToShow.add(new String[] {"Java Virtual Machine", System.getProperty("java.vm.version")});
			valuesToShow.add(new String[] {"OS", System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ')'});
		}
		
		public int getColumnCount() {
			return 2;
		}
		
		public int getRowCount() {
			return valuesToShow.size();
		}
		
		public String getValueAt(int rowIndex, int columnIndex) {
			return valuesToShow.get(rowIndex)[columnIndex];
		}
		
		public String getColumnName(int column) {
			return column == 0 ? "Property" : "Value";
		}
	}
}
