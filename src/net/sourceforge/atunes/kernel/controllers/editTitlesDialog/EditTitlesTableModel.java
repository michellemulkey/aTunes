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

package net.sourceforge.atunes.kernel.controllers.editTitlesDialog;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class EditTitlesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -4440078678648669115L;

	private ArrayList<AudioFile> files;
	private HashMap<AudioFile, String> newValues;
	
	private ArrayList<TableModelListener> listeners;
	
	public EditTitlesTableModel(ArrayList<AudioFile> files) {
		this.files = files;
		this.newValues = new HashMap<AudioFile, String>();
		this.listeners = new ArrayList<TableModelListener>();
	}
	
	public void addListener(TableModelListener listener) {
		listeners.add(listener);
	}
	
	public String getColumnName(int column) {
		if (column == 0)
			return LanguageTool.getString("FILE");
		return LanguageTool.getString("TITLE");
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1;
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		AudioFile file = files.get(rowIndex);
		if (columnIndex == 0)
			return file.getName();
		if (newValues.containsKey(file))
			return newValues.get(file);
		return file.getTitle();
	}
	
	public void setValueAt(String aValue, int rowIndex, int columnIndex) {
		newValues.put(files.get(rowIndex), aValue);
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public void setTitles(ArrayList<String> titles) {
		for (int i = 0; i < files.size(); i++) {
			newValues.put(files.get(i), titles.get(i));
		}

		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public int getColumnCount() {
		return 2;
	}
	
	public int getRowCount() {
		return files.size();
	}
	
	public HashMap<AudioFile, String> getNewValues() {
		return newValues;
	}
}
