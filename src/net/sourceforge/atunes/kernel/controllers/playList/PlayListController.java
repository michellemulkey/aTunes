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

package net.sourceforge.atunes.kernel.controllers.playList;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import net.sourceforge.atunes.gui.model.PlayListTableModel;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListColumnClickedListener;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.gui.views.panels.PlayListPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.model.PanelController;
import net.sourceforge.atunes.kernel.executors.BackgroundExecutor;
import net.sourceforge.atunes.kernel.modules.regexp.RegexpUtils;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class PlayListController extends PanelController implements PlayListColumnClickedListener {

	public PlayListController(PlayListPanel panel) {
		super(panel);
		addBindings();
		addStateBindings();
	}
	
	public void finish() {
	}
	
	protected void addBindings() {
		final PlayListTable table = ((PlayListPanel) panelControlled).getPlayListTable();
		table.addPlayListColumnClickedListener(this);
		
		table.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				adjustColumnsWidth();
			}
		});
		
		// Keys
		table.getDeleteItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		table.getPlayItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
		table.getInfoItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		table.getSaveItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		table.getLoadItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		table.getClearItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		table.getTopItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		table.getDownItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
		table.getUpItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
		table.getBottomItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		table.getFavoriteSong().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		table.getFavoriteAlbum().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		table.getFavoriteArtist().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		// End Keys
		
		PlayListListener listener = new PlayListListener(table, this);
		
		table.getPlayItem().addActionListener(listener);
		table.getEditTagItem().addActionListener(listener);
		table.getAutoSetTrackNumberItem().addActionListener(listener);
		table.getAutoSetGenreItem().addActionListener(listener);
		table.getSaveItem().addActionListener(listener);
		table.getLoadItem().addActionListener(listener);
		table.getFilterItem().addActionListener(listener);
		table.getTopItem().addActionListener(listener);
		table.getUpItem().addActionListener(listener);
		table.getDownItem().addActionListener(listener);
		table.getBottomItem().addActionListener(listener);
		table.getDeleteItem().addActionListener(listener);
		table.getInfoItem().addActionListener(listener);
		table.getClearItem().addActionListener(listener);
		table.addMouseListener(listener);
			
		table.getSelectionModel().addListSelectionListener(listener);
		table.getFavoriteSong().addActionListener(listener);
		table.getFavoriteAlbum().addActionListener(listener);
		table.getFavoriteArtist().addActionListener(listener);
		table.getArtistItem().addActionListener(listener);
		table.getAlbumItem().addActionListener(listener);
		table.getShowControls().addActionListener(listener);
	}
	
	protected void addStateBindings() {
		disablePlayListItems(true);
	}
	
	protected void notifyReload() {}
	
	public void columnClicked(String column) {
		if (column.equals(LanguageTool.getString("TRACK"))) { 
			HandlerProxy.getPlayListHandler().sortPlaylistByTrack();
		}
		else if (column.equals(LanguageTool.getString("TITLE"))) { 
			HandlerProxy.getPlayListHandler().sortPlaylistByTitle();
		}
		else if (column.equals(LanguageTool.getString("ARTIST"))) 
			HandlerProxy.getPlayListHandler().sortPlaylistByArtist();
		else if (column.equals(LanguageTool.getString("ALBUM")))
			HandlerProxy.getPlayListHandler().sortPlaylistByAlbum();
		else if (column.equals(LanguageTool.getString("GENRE")))
			HandlerProxy.getPlayListHandler().sortPlaylistByGenre();
			}
	
	public void updatePositionInTable(int pos) {
		((PlayListTableModel)((PlayListPanel) panelControlled).getPlayListTable().getModel()).refresh(pos);
	}
	
	public void addSongsToPlayList(ArrayList songs, int selectedSong) {
		if (selectedSong == -1)
			selectedSong = ((PlayListPanel) panelControlled).getPlayListTable().getPlayingSong();
		
		for (int i = 0; i < songs.size(); i++) 
			((PlayListTableModel)((PlayListPanel) panelControlled).getPlayListTable().getModel()).addSong((AudioFile)songs.get(i));
		
		setSelectedSongOnTable(selectedSong);
		
		HandlerProxy.getControllerHandler().getPlayListControlsController().enableSaveButton(true);
		HandlerProxy.getControllerHandler().getMenuController().enableSavePlaylist(true);
	}
	
	public void setSelectedSong(int song) {
		setSelectedSongOnTable(song);
	}
	
	private void setSelectedSongOnTable(int song) {
		PlayListTable table = ((PlayListPanel) panelControlled).getPlayListTable();
		Rectangle visibleRect = table.getVisibleRect();
		visibleRect.y = song * 16 - visibleRect.height / 2;
		table.scrollRectToVisible(visibleRect);
		table.setPlayingSong(song);
		
		adjustColumnsWidth();
	}
	
	public void adjustColumnsWidth() {
		PlayListTable table = ((PlayListPanel) panelControlled).getPlayListTable();
		
		boolean showTrack = ((PlayListTableModel)table.getModel()).isTrackVisible();
		boolean showGenre = ((PlayListTableModel)table.getModel()).isGenreVisible();
		
		if (table.getColumnCount() < 2)
			return;
		
		int tableWidth = table.getWidth();
		int favoriteColumnWidth = 18;
		int trackColumnWidth = 30;
		int durationColumnWidth = 50;
		int nonDurationWidth = tableWidth - favoriteColumnWidth - trackColumnWidth - durationColumnWidth;
		int nonDurationColumns = table.getColumnCount() - 3 - (showGenre ? 1 : 0);
		int genreColumnWidth = 100;

		table.getColumnModel().getColumn(0).setMinWidth(favoriteColumnWidth);
		table.getColumnModel().getColumn(0).setMaxWidth(favoriteColumnWidth);

		if (showTrack) {
			table.getColumnModel().getColumn(1).setMinWidth(trackColumnWidth);
			table.getColumnModel().getColumn(1).setMaxWidth(trackColumnWidth);
		}

		if (showGenre) {
			table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - 2).setMinWidth(genreColumnWidth);
			table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - 2).setMaxWidth(genreColumnWidth);
		}

		table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - 1).setMinWidth(durationColumnWidth);
		table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - 1).setMaxWidth(durationColumnWidth);

		for (int i = (showGenre ? 2 : 1); i < nonDurationColumns; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(nonDurationWidth / nonDurationColumns);
		}
		table.invalidate();
		HandlerProxy.getVisualHandler().repaint();
	}
	
	public void playSelectedSong() {
		int song = ((PlayListPanel)panelControlled).getPlayListTable().getSelectedRow();
		HandlerProxy.getPlayerHandler().setPlayListPositionToPlay(song);
		HandlerProxy.getPlayerHandler().play(false);
	}
	
	public void setTrackNumber() {
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		int[] rows = table.getSelectedRows();
		ArrayList<AudioFile> files = new ArrayList<AudioFile>();
		for (int i = 0; i < rows.length; i++) {
			files.add(((PlayListTableModel)table.getModel()).getFileAt(rows[i]));
		}
		HashMap<AudioFile, Integer> filesAndTracks = RegexpUtils.getFilesAndTrackNumbers(files);
		if (!filesAndTracks.isEmpty())
			BackgroundExecutor.changeTrackNumbers(filesAndTracks);
	}
	
	public void setGenre() {
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		int[] rows = table.getSelectedRows();
		ArrayList<AudioFile> files = new ArrayList<AudioFile>();
		for (int i = 0; i < rows.length; i++) {
			files.add(((PlayListTableModel)table.getModel()).getFileAt(rows[i]));
		}
		HashMap<AudioFile, String> filesAndGenres = HandlerProxy.getAudioScrobblerServiceHandler().getGenresForFiles(files);
		if (!filesAndGenres.isEmpty())
			BackgroundExecutor.changeGenres(filesAndGenres);
	}
	
	public void moveToTop() {
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		int[] rows = table.getSelectedRows();
		if (rows.length > 0 && rows[0] > 0) {
			((PlayListTableModel)table.getModel()).moveToTop(rows);
			HandlerProxy.getPlayListHandler().moveToTop(rows);
			((PlayListPanel) panelControlled).getPlayListTable().getSelectionModel().setSelectionInterval(0,rows.length-1);
		}
		// Solves bug 1663521
		adjustColumnsWidth();
	}
	
	public void moveUp() {
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		int[] rows = table.getSelectedRows();
		if (rows.length > 0 && rows[0] > 0) {
			((PlayListTableModel)table.getModel()).moveUp(rows);
			HandlerProxy.getPlayListHandler().moveUp(rows);
			((PlayListPanel) panelControlled).getPlayListTable().getSelectionModel().setSelectionInterval(rows[0]-1,rows[rows.length-1]-1);
		}
		// Solves bug 1663521
		adjustColumnsWidth();
	}
	
	public void moveDown() {
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		int[] rows = table.getSelectedRows();
		if (rows.length > 0 && rows[rows.length-1] < table.getRowCount()-1) {
			((PlayListTableModel)table.getModel()).moveDown(rows);
			HandlerProxy.getPlayListHandler().moveDown(rows);
			((PlayListPanel) panelControlled).getPlayListTable().getSelectionModel().setSelectionInterval(rows[0]+1,rows[rows.length-1]+1);
		}
		// Solves bug 1663521
		adjustColumnsWidth();
	}

	public void moveToBottom() {
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		int[] rows = table.getSelectedRows();
		if (rows.length > 0 && rows[rows.length-1] < table.getRowCount()-1) {
			((PlayListTableModel)table.getModel()).moveToBottom(rows);
			HandlerProxy.getPlayListHandler().moveToBottom(rows);
			((PlayListPanel) panelControlled).getPlayListTable().getSelectionModel().setSelectionInterval(((PlayListPanel) panelControlled).getPlayListTable().getRowCount()-rows.length,((PlayListPanel) panelControlled).getPlayListTable().getRowCount()-1);
		}
		// Solves bug 1663521
		adjustColumnsWidth();
	}
	
	public void deleteSelection() {
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		int[] rows = table.getSelectedRows();
		if (rows.length > 0) {
			((PlayListTableModel)table.getModel()).removeSongs(rows);
			((PlayListPanel) panelControlled).getPlayListTable().getSelectionModel().setSelectionInterval(-1,-1);
			HandlerProxy.getPlayListHandler().removeSongs(rows);
		}
	}
		
	public void setAsFavoriteSongs(ArrayList<AudioFile> songs) {
		HandlerProxy.getRepositoryHandler().addFavoriteSongs(songs);
	}
	
	public void setAsFavoriteAlbums(ArrayList<AudioFile> songs) {
		HandlerProxy.getRepositoryHandler().addFavoriteAlbums(songs);
	}
	
	public void setAsFavoriteArtists(ArrayList<AudioFile> songs) {
		HandlerProxy.getRepositoryHandler().addFavoriteArtists(songs);
	}

	public void disablePlayListItems(boolean disable) {
		PlayListTable table = ((PlayListPanel) panelControlled).getPlayListTable();
		table.getPlayItem().setEnabled(!disable);
		table.getInfoItem().setEnabled(!disable);
		table.getDeleteItem().setEnabled(!disable);
		table.getClearItem().setEnabled(true);
		table.getTopItem().setEnabled(!disable);
		table.getUpItem().setEnabled(!disable);
		table.getDownItem().setEnabled(!disable);
		table.getBottomItem().setEnabled(!disable);
		table.getFavoriteSong().setEnabled(!disable);
		table.getFavoriteAlbum().setEnabled(!disable);
		table.getFavoriteArtist().setEnabled(!disable);
	}
	
	private ArrayList<AudioFile> getSelectedSongs(JTable table) {
		int[] selectedRows = table.getSelectedRows();
		ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
		for (int i = 0; i < selectedRows.length; i++) {
			AudioFile file = ((PlayListTableModel)table.getModel()).getFileAt(selectedRows[i]);
			songs.add(file);
		}
		return songs;
	}
	
	public ArrayList<AudioFile> getSelectedSongs() {
		return getSelectedSongs(HandlerProxy.getVisualHandler().getPlayListTable());
	}
	
	public JScrollPane getMainPlayListScrollPane() {
		return ((PlayListPanel) panelControlled).getPlayListTableScroll();
	}
	
	public PlayListTable getMainPlayListTable() {
		return ((PlayListPanel) panelControlled).getPlayListTable();
	}
		
	public void setArtistAsPlaylist() {
		String artist = HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile().getArtist();
		HashMap<String, Artist> structure = HandlerProxy.getRepositoryHandler().getArtistAndAlbumStructure();
		if (structure.containsKey(artist)) {
			Artist a = structure.get(artist);
			HandlerProxy.getPlayListHandler().clearList();
			HandlerProxy.getPlayListHandler().addToPlayList(a.getSongs());
		}
	}
	
	public void setAlbumAsPlaylist() {
		String artist = HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile().getArtist();
		String album = HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile().getAlbum();
		HashMap<String, Artist> structure = HandlerProxy.getRepositoryHandler().getArtistAndAlbumStructure();
		if (structure.containsKey(artist)) {
			Artist a = structure.get(artist);
			Album al = a.getAlbum(album);
			if (al != null) {
				HandlerProxy.getPlayListHandler().clearList();
				HandlerProxy.getPlayListHandler().addToPlayList(al.getSongs());
			}
		}
	}
	
	public void showPlaylistControls(boolean show) {
		Kernel.getInstance().state.setShowPlaylistControls(show);
		getMainPlayListTable().getShowControls().setSelected(show);
		HandlerProxy.getControllerHandler().getMenuController().setShowPlaylistControls(show);
		((PlayListPanel)panelControlled).getPlayListControls().setVisible(show);
	}
}
