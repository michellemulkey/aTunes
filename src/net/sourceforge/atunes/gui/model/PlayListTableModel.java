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
import java.util.HashMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.language.LanguageTool;


/**
 * @author fleax
 *
 */
public class PlayListTableModel implements TableModel {
	
	private ArrayList<AudioFile> songs;
	private ArrayList<TableModelListener> listeners;
	private boolean trackVisible = true;
	private boolean artistVisible = true;
	private boolean albumVisible = true;
	private boolean genreVisible = true;

	private enum PlayListColumn {FAVORITE, TRACK, TITLE, ARTIST, ALBUM, GENRE, DURATION}
	private static PlayListColumn[] headers = {PlayListColumn.FAVORITE, PlayListColumn.TRACK, PlayListColumn.TITLE, PlayListColumn.ARTIST, PlayListColumn.ALBUM, PlayListColumn.GENRE, PlayListColumn.DURATION};
	private PlayListColumn[] currentHeaders;
	private static HashMap<PlayListColumn, Class> classes;
	
	static {
		classes = new HashMap<PlayListColumn, Class>();
		classes.put(PlayListColumn.FAVORITE, Integer.class);
		classes.put(PlayListColumn.TRACK, String.class);
		classes.put(PlayListColumn.TITLE, AudioFile.class);
		classes.put(PlayListColumn.ARTIST, String.class);
		classes.put(PlayListColumn.ALBUM, String.class);
		classes.put(PlayListColumn.GENRE, String.class);
		classes.put(PlayListColumn.DURATION, Long.class);
	}
	
	private static HashMap<PlayListColumn, String> columnNames;
	
	static {
		columnNames = new HashMap<PlayListColumn, String>();
		columnNames.put(PlayListColumn.FAVORITE, "");
		columnNames.put(PlayListColumn.TRACK, LanguageTool.getString("TRACK"));
		columnNames.put(PlayListColumn.TITLE, LanguageTool.getString("TITLE"));
		columnNames.put(PlayListColumn.ARTIST, LanguageTool.getString("ARTIST"));
		columnNames.put(PlayListColumn.ALBUM, LanguageTool.getString("ALBUM"));
		columnNames.put(PlayListColumn.GENRE, LanguageTool.getString("GENRE"));
		columnNames.put(PlayListColumn.DURATION, LanguageTool.getString("DURATION"));
	}
	
	public PlayListTableModel() {
		songs = new ArrayList<AudioFile>();
		listeners = new ArrayList<TableModelListener>();
		setCurrentHeaders();
	}
	
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	private void setCurrentHeaders() {
		int columns = getColumnCount();
		if (columns == 0)
			return;
		
		currentHeaders = new PlayListColumn[columns];

		currentHeaders[0] = PlayListColumn.FAVORITE;
		int c = 1;
		if (trackVisible)
			currentHeaders[c++] = PlayListColumn.TRACK;
		
		currentHeaders[c++] = PlayListColumn.TITLE;

		if (artistVisible) 
			currentHeaders[c++] = PlayListColumn.ARTIST;
		if (albumVisible)
			currentHeaders[c++] = PlayListColumn.ALBUM;
		if (genreVisible)
			currentHeaders[c++] = PlayListColumn.GENRE;
		currentHeaders[c] = PlayListColumn.DURATION;
	}
	
	private PlayListColumn getColumn(int colIndex) {
		return currentHeaders[colIndex];
	}
	
	public Class<?> getColumnClass(int colIndex) {
		return classes.get(getColumn(colIndex));
	}

	public int getColumnCount() {
		return headers.length - (trackVisible ? 0 : 1) - (artistVisible ? 0 : 1) - (albumVisible ? 0 : 1) - (genreVisible ? 0 : 1);
	}
	
	public String getColumnName(int colIndex) {
		return columnNames.get(getColumn(colIndex));
	}

	public int getRowCount() {
		return songs.size();
	}

	public Object getValueAt(int rowIndex, int colIndex) {
		AudioFile file = songs.get(rowIndex);
		
		PlayListColumn c = getColumn(colIndex);

		if (c == PlayListColumn.FAVORITE)
			return HandlerProxy.getRepositoryHandler().getFavoriteSongs().contains(file) ? 1 : 0;
		else if (c == PlayListColumn.TRACK) {
			int track = file.getTrackNumber();
			return track != 0 ? track : "";
		}
		else if (c == PlayListColumn.TITLE)
			return file;
		else if (c == PlayListColumn.ARTIST)
			return file.getArtist();
		else if (c == PlayListColumn.ALBUM)
			return file.getAlbum();
		else if (c == PlayListColumn.GENRE)
			return file.getGenre();
		else
			return file.getDuration();
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
	
	public void addSong(AudioFile song) {
		songs.add(song);
		
		TableModelEvent event;
		event = new TableModelEvent(this, this.getRowCount()-1, this.getRowCount()-1,
			        TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public void removeSongs(int[] rows) {
		for (int i = rows.length - 1; i >= 0; i--) {
			songs.remove(rows[i]);
		}
		
		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}

	public void removeSongs() {
		songs.clear();
		
		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public void moveToTop(int[] rows) {
		for (int i = 0; i < rows.length;  i++) {
			AudioFile aux = songs.get(rows[i]);
			songs.remove(rows[i]);
			songs.add(i, aux);
		}
		
		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public void moveUp(int[] rows) {
		for (int i = 0; i < rows.length;  i++) {
			AudioFile aux = songs.get(rows[i]);
			songs.remove(rows[i]);
			songs.add(rows[i]-1, aux);
		}
		
		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}

	public void moveDown(int[] rows) {
		for (int i = rows.length-1; i >= 0;  i--) {
			AudioFile aux = songs.get(rows[i]);
			songs.remove(rows[i]);
			songs.add(rows[i]+1, aux);
		}
		
		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}

	public void moveToBottom(int[] rows) {
		int j = 0;
		for (int i = rows.length-1; i >= 0;  i--) {
			AudioFile aux = songs.get(rows[i]);
			songs.remove(rows[i]);
			songs.add(songs.size()-j++, aux);
		}
		
		TableModelEvent event;
		event = new TableModelEvent(this, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}

	public void refresh() {
		TableModelEvent event;
		event = new TableModelEvent(this, -1, -1,
			        TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public void refresh(int pos) {
		TableModelEvent event;
		event = new TableModelEvent(this, pos, pos,
			        TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).tableChanged(event);
	}
	
	public AudioFile getFileAt(int pos) {
		return songs.get(pos);
	}

	public void setTrackVisible(boolean trackVisible) {
		this.trackVisible = trackVisible;
		setCurrentHeaders();
		refresh();
	}
	
	public void setArtistVisible(boolean artistVisible) {
		this.artistVisible = artistVisible;
		setCurrentHeaders();
		refresh();
	}
	
	public void setAlbumVisible(boolean albumVisible) {
		this.albumVisible = albumVisible;
		setCurrentHeaders();
		refresh();
	}
	
	public boolean isEmpty() {
		return songs.isEmpty();
	}

	public boolean isAlbumVisible() {
		return albumVisible;
	}

	public boolean isArtistVisible() {
		return artistVisible;
	}

	public void setGenreVisible(boolean genreVisible) {
		this.genreVisible = genreVisible;
		setCurrentHeaders();
		refresh();
	}

	public boolean isGenreVisible() {
		return genreVisible;
	}

	public boolean isTrackVisible() {
		return trackVisible;
	}
	
}
