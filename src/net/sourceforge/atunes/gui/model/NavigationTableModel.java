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

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationControllerViews;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.language.LanguageTool;


/**
 * @author fleax
 *
 */
public class NavigationTableModel implements TableModel {
	
	private NavigationController controller;
	
	private ArrayList<AudioFile> songs;
	
	private ArrayList<TableModelListener> listeners;
	
	public NavigationTableModel(NavigationController controller) {
		this.controller = controller;
		songs = new ArrayList<AudioFile>();
		listeners = new ArrayList<TableModelListener>();
	}
	
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return Integer.class;
		else if (columnIndex == 1)
			return String.class;
		else
			return Long.class;
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int columnIndex) {
		int view = controller.getState().getNavigationView();
		if (columnIndex == 0)
			return "";
		else if (columnIndex == 2)
			return LanguageTool.getString("DURATION");
		else if (view == NavigationControllerViews.TAG_VIEW || view == NavigationControllerViews.FAVORITE_VIEW || (view == NavigationControllerViews.DEVICE_VIEW && Kernel.getInstance().state.isSortDeviceByTag()))
			return LanguageTool.getString("TITLE");
		else
			return LanguageTool.getString("FILE");
	}

	public int getRowCount() {
		return songs.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		AudioFile f = songs.get(rowIndex);
		if (columnIndex == 0)
			return Kernel.getInstance().state.SHOW_FAVORITES_IN_NAVIGATOR && HandlerProxy.getRepositoryHandler().getFavoriteSongs().contains(f) ? 1 : 0;
		else if (columnIndex == 1) {
			if (controller.getState().getNavigationView() == NavigationControllerViews.TAG_VIEW || controller.getState().getNavigationView() == NavigationControllerViews.FAVORITE_VIEW || Kernel.getInstance().state.isSortDeviceByTag())
				return f.getTitleOrFileName();
			return f.getName();
		}
		else
			return f.getDuration();
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
	
	public void setSongs(ArrayList<AudioFile> songs) {
		this.songs = songs;
		TableModelEvent event = new TableModelEvent(this, -1, this.getRowCount()-1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public AudioFile getSongAt(int row) {
		return songs.get(row);
	}
	
	public ArrayList<AudioFile> getSongsAt(int[] rows) {
		ArrayList<AudioFile> result = new ArrayList<AudioFile>();
		for (int i = 0; i < rows.length; i++) {
			result.add(getSongAt(rows[i]));
		}
		return result;
	}
	
	public ArrayList<AudioFile> getSongs() {
		return songs;
	}

}
