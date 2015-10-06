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

package net.sourceforge.atunes.gui.model;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * 
 * @author fleax
 *
 */
public class FilePropertiesTableModel implements TableModel {

	private ArrayList<String> properties;
	private ArrayList values;
	
	private ArrayList<TableModelListener> listeners;
	
	public FilePropertiesTableModel() {
		listeners = new ArrayList<TableModelListener>();
	}
	
	public void setPropertiesToShow(ArrayList<String> props, ArrayList values) {
		this.properties = props;
		this.values = values;
		
		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);		
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		return String[].class;
	}
	
	public int getColumnCount() {
		return 1;
	}
	
	public String getColumnName(int columnIndex) {
		return "";
	}
	
	public int getRowCount() {
		return properties != null ? properties.size() : 0;
	}
	
	public String[] getValueAt(int rowIndex, int columnIndex) {
		String [] result = new String[2];
		result[0] = properties.get(rowIndex);
		Object obj = values.get(rowIndex);
		if (obj != null)
			result[1] = obj.toString();
		else
			result[1] = "";
		return result;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
}
