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
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.model.CDInfo;
import net.sourceforge.atunes.utils.language.LanguageTool;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class RipCdDialog extends CustomDialog {

	private static final long serialVersionUID = 1987727841297807350L;

	private JTable table;
	JTextField artistTextField;
	JTextField albumTextField;
	private JButton amazonButton;
	JComboBox format;
	JComboBox quality;
	JTextField folderName;
	private JButton ok;
	private JButton cancel;
	
	boolean canceled;
	
	String artist;
	String album;
	String folder;
	
	private CdInfoTableModel tableModel;
	
	static String[] MP3_QUALITY = {"128", "160", "192", "256", "320"};
	static String[] OGG_QUALITY = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	
	static String DEFAULT_OGG_QUALITY = "4";
	
	public RipCdDialog(JFrame owner) {
		super(owner, 400, 400, true);
		setContent(getContent());
	}
	
	private JPanel getContent() {
		Font f = FontSingleton.GENERAL_FONT;
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		tableModel = new CdInfoTableModel();
		table = new JTable(tableModel);
		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(2).setMaxWidth(50);
		JCheckBox checkBox = new JCheckBox();
		table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(checkBox));
		
		JScrollPane scrollPane = new JScrollPane(table);
		JLabel artistLabel = new JLabel(LanguageTool.getString("ARTIST"));
		artistLabel.setFont(f);
		artistTextField = new JTextField();
		JLabel albumLabel = new JLabel(LanguageTool.getString("ALBUM"));
		albumLabel.setFont(f);
		albumTextField = new JTextField();
		amazonButton = new CustomButton(null, LanguageTool.getString("GET_TITLES_FROM_AMAZON"));
		amazonButton.setFont(f);
		amazonButton.setEnabled(false);
		JLabel formatLabel = new JLabel(LanguageTool.getString("ENCODE_TO"));
		formatLabel.setFont(f);
		format = new JComboBox(new String[] {"OGG", "MP3"});
		format.setFont(f);
		JLabel qualityLabel = new JLabel(LanguageTool.getString("QUALITY"));
		qualityLabel.setFont(f);
		quality = new JComboBox(new String[] {});
		quality.setFont(f);
		JLabel dir = new JLabel(LanguageTool.getString("FOLDER"));
		dir.setFont(f);
		folderName = new JTextField();
		ok = new CustomButton(null, LanguageTool.getString("OK"));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				artist = artistTextField.getText();
				album = albumTextField.getText();
				folder = folderName.getText();
				setVisible(false);
			}
		});
		cancel = new CustomButton(null, LanguageTool.getString("CANCEL"));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = true;
				setVisible(false);
			}
		});
		format.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (format.getSelectedItem().equals("MP3"))
					quality.setModel(new DefaultComboBoxModel(MP3_QUALITY));
				else {
					quality.setModel(new DefaultComboBoxModel(OGG_QUALITY));
					quality.setSelectedItem(DEFAULT_OGG_QUALITY);
				}
			}
		});
		
		
		JPanel auxPanel = new JPanel();
		auxPanel.setOpaque(false);
		auxPanel.add(ok);
		auxPanel.add(cancel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;  c.gridwidth = 4; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(20,20,10,20);
		panel.add(scrollPane, c);
		c.gridy = 1; c.gridwidth = 1; c.weightx = 0; c.weighty = 0; c.fill = GridBagConstraints.NONE; c.insets = new Insets(0,20,5,20); c.anchor = GridBagConstraints.WEST;
		panel.add(artistLabel, c);
		c.gridx = 1; c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(artistTextField, c);
		c.gridx = 0; c.gridwidth = 1; c.gridy = 2; c.fill = GridBagConstraints.NONE;
		panel.add(albumLabel, c);
		c.gridx = 1; c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(albumTextField, c);
		c.gridy = 3;
		panel.add(amazonButton, c);
		c.gridx = 0; c.gridy = 4; c.fill = GridBagConstraints.NONE; c.gridwidth = 1;
		panel.add(formatLabel, c);
		c.gridx = 1; c.weightx = 0.3; c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(format, c);
		c.gridx = 2;  c.gridwidth = 1;
		panel.add(qualityLabel, c);
		c.gridx = 3;
		panel.add(quality, c);
		c.gridx = 0; c.gridy = 5; c.fill = GridBagConstraints.NONE;
		panel.add(dir, c);
		c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.gridwidth = 3;
		panel.add(folderName, c);
		c.gridx = 0; c.gridy = 6; c.gridwidth = 4; c.anchor = GridBagConstraints.CENTER;
		panel.add(auxPanel, c);
		
		return panel;
	}
	
	public void setTableData(CDInfo cdInfo) {
		tableModel.setCdInfo(cdInfo);
		tableModel.fireTableDataChanged();
	}
	
	public void updateTrackNames(ArrayList<String> names) {
		tableModel.setTrackNames(names);
		tableModel.fireTableDataChanged();
	}
	
	public static void main(String[] args) {
		RipCdDialog dialog = new RipCdDialog(null);
		dialog.setVisible(true);
	}

	public boolean isCanceled() {
		return canceled;
	}

	public String getAlbum() {
		return album;
	}

	public String getArtist() {
		return artist;
	}
	
	public ArrayList<Integer> getTracksSelected() {
		ArrayList<Boolean> tracks = tableModel.getTracksSelected();
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < tracks.size(); i++) {
			if (tracks.get(i))
				result.add(i+1);
		}
		return result;
	}
		
	public String getFormat() {
		return (String) format.getSelectedItem();
	}
	
	public String getQuality() {
		return (String) quality.getSelectedItem();
	}
	
	private class CdInfoTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -7577681531593039707L;
		
		private CDInfo cdInfo;
		private ArrayList<String> trackNames;
		
		private ArrayList<Boolean> tracksSelected;
		
		public CdInfoTableModel() {
		}
		
		public String getColumnName(int column) {
			return "";
		}
		public int getColumnCount() {
			return 3;
		}
		public int getRowCount() {
			return cdInfo != null ? cdInfo.getTracks() : 0;
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0)
				return tracksSelected.get(rowIndex);
			else if (columnIndex == 1) {
				if (trackNames == null || rowIndex > trackNames.size()-1)
					return LanguageTool.getString("TRACK") + ' ' + (rowIndex+1);
				if (rowIndex < trackNames.size())
					return trackNames.get(rowIndex);
				return LanguageTool.getString("TRACK") + ' ' + (rowIndex+1);
			}
			else
				return cdInfo.getDurations().get(rowIndex);
		}
		public void setCdInfo(CDInfo cdInfo) {
			if (cdInfo != null) {
				this.cdInfo = cdInfo;
				if (tracksSelected == null)
					tracksSelected = new ArrayList<Boolean>();
				tracksSelected.clear();
				for (int i = 0; i < cdInfo.getTracks(); i++) {
					tracksSelected.add(true);
				}
			}
		}
		
		public Class<?> getColumnClass(int columnIndex) {
			return columnIndex == 0 ? Boolean.class : String.class;
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 0;
		}
		
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			tracksSelected.remove(rowIndex);
			tracksSelected.add(rowIndex, (Boolean) aValue);
		}

		public ArrayList<Boolean> getTracksSelected() {
			return tracksSelected;
		}

		public void setTrackNames(ArrayList<String> trackNames) {
			this.trackNames = trackNames;
		}
	}

	public JButton getAmazonButton() {
		return amazonButton;
	}

	public JTextField getAlbumTextField() {
		return albumTextField;
	}

	public JTextField getArtistTextField() {
		return artistTextField;
	}

	public JButton getCancel() {
		return cancel;
	}

	public JButton getOk() {
		return ok;
	}

	public String getFolder() {
		return folder;
	}
	
	public void showCdInfo(CDInfo cdInfo) {
		artistTextField.setText("");
		albumTextField.setText("");
		folderName.setText("");
		amazonButton.setEnabled(false);
		format.setSelectedIndex(0);
		artist = null;
		album = null;
		folder = null;
		setTableData(cdInfo);
		updateTrackNames(null);
		setVisible(true);
	}

	public JTextField getFolderName() {
		return folderName;
	}
}
