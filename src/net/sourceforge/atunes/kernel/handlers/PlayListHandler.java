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

package net.sourceforge.atunes.kernel.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.atunes.gui.model.PlayListTableModel;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.executors.BackgroundExecutor;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.utils.SystemProperties;
import net.sourceforge.atunes.model.player.PlayList;
import net.sourceforge.atunes.model.player.PlayListAlbumComparator;
import net.sourceforge.atunes.model.player.PlayListArtistComparator;
import net.sourceforge.atunes.model.player.PlayListGenreComparator;
import net.sourceforge.atunes.model.player.PlayListListener;
import net.sourceforge.atunes.model.player.PlayListTitleComparator;
import net.sourceforge.atunes.model.player.PlayListTrackComparator;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;


public class PlayListHandler {
	
	static Logger logger = Logger.getLogger(PlayListHandler.class);
	
	private static final String M3U_HEADER = "#EXTM3U";
	private static final String M3U_START_COMMENT = "#";
	
	private PlayList nonFilteredPlayList;
	
	private final PlayListListener playListListener = new PlayListListener() {
		public void selectedSongChanged(AudioFile song) {
			if (Kernel.getInstance().state.isShowSongProperties())
				HandlerProxy.getControllerHandler().getFilePropertiesController().updateValues(song);
			if (Kernel.getInstance().state.isUseAudioScrobbler())
				HandlerProxy.getControllerHandler().getAudioScrobblerController().updatePanel(song);
		}
		public void clear() {
			if (Kernel.getInstance().state.isShowSongProperties())
				HandlerProxy.getControllerHandler().getFilePropertiesController().clear();
			if (Kernel.getInstance().state.isUseAudioScrobbler())
				HandlerProxy.getControllerHandler().getAudioScrobblerController().clear();
		}
	};
	
	public PlayListHandler() {
	}
	
	/**
	 * Edit tags
	 */
	public void editTags() {
		ArrayList<AudioFile> files = HandlerProxy.getControllerHandler().getPlayListController().getSelectedSongs();
		HandlerProxy.getControllerHandler().getEditTagDialogController().editFiles(files);
	}
	
	/**
	 * Save play list
	 */
	public void savePlaylist() {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = getPlaylistFileFilter();
		if (HandlerProxy.getVisualHandler().showSaveDialog(fileChooser, filter) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.getName().toUpperCase().endsWith("M3U"))
				file = new File(file.getAbsolutePath() + ".m3u");
			if (!file.exists() || (file.exists() && HandlerProxy.getVisualHandler().showConfirmationDialog(LanguageTool.getString("OVERWRITE_FILE"), LanguageTool.getString("INFO")) == JOptionPane.OK_OPTION)) {
				write(HandlerProxy.getPlayerHandler().getCurrentPlayList(), file.getAbsolutePath());
			}
		}
	}
	
	public void loadPlaylist() {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = getPlaylistFileFilter();
		if (HandlerProxy.getVisualHandler().showOpenDialog(fileChooser, filter) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file.exists()) {
				ArrayList<String> filesToLoad = read(file);
				HandlerProxy.getVisualHandler().getProgressDialog().setVisible(true);
				BackgroundExecutor.loadPlayList(filesToLoad);
			} else
				HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("FILE_NOT_FOUND"));
		}
	}
	
	public void getLastPlayList() {
		PlayList lastPlayList = ApplicationDataHandler.retrievePlayListCache();
		if (lastPlayList.size() > 0) {
			if (lastPlayList.get(0).exists()) {
				HandlerProxy.getPlayerHandler().setCurrentPlayList(lastPlayList);
				HandlerProxy.getControllerHandler().getPlayListController().addSongsToPlayList(lastPlayList, lastPlayList.getNextFile());
				HandlerProxy.getVisualHandler().showPlaylistSongNumber(HandlerProxy.getPlayerHandler().getCurrentPlayList().size());
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(lastPlayList.getCurrentFile());

				// Apply shuffle / repeat state on controls
				HandlerProxy.getControllerHandler().getPlayerControlsController().setRepeat(lastPlayList.isRepeatMode());
				HandlerProxy.getControllerHandler().getPlayerControlsController().setShuffle(lastPlayList.isShuffleMode());
			}
		}
		HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
	}
	
	public Runnable getLoadPlayListProcess(final ArrayList<String> files) {
		return new Runnable() {
			public void run() {
				ArrayList<AudioFile> songsLoaded = new ArrayList<AudioFile>();
				for (String fileName : files) {
					AudioFile file = HandlerProxy.getRepositoryHandler().getFileIfLoaded(fileName);
					if (file == null)
						file = new AudioFile(fileName);
					songsLoaded.add(file);
				}
				if (songsLoaded.size() >= 1) {
					if (HandlerProxy.getPlayerHandler().getCurrentPlayList().isEmpty())
						getPlayListListener().selectedSongChanged(songsLoaded.get(0));
					addToPlayList(songsLoaded);
				}
				HandlerProxy.getVisualHandler().getProgressDialog().setVisible(false);
			}
		};
	}

	
	public static final FileFilter getPlaylistFileFilter() {
		return new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory() || file.getName().toUpperCase().endsWith("M3U");
			}
			public String getDescription() {
				return LanguageTool.getString("PLAYLIST");
			}
		};
	}
	
	private void sortPlayList(Comparator comp) {
		AudioFile currentFile = HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile();
		PlayList currentPlaylist = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		Collections.sort(currentPlaylist, comp);
		int pos = currentPlaylist.indexOf(currentFile);
		HandlerProxy.getVisualHandler().getPlayListTableModel().removeSongs();
		setPlayList(currentPlaylist);
		currentPlaylist.setNextFile(pos);
		HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(pos);
	}
	
	public void sortPlaylistByTrack() {
		sortPlayList(PlayListTrackComparator.comparator);
	}
	
	public void sortPlaylistByTitle() {
		sortPlayList(PlayListTitleComparator.comparator);
	}
	
	public void sortPlaylistByArtist() {
		sortPlayList(PlayListArtistComparator.comparator);
	}
	
	public void sortPlaylistByAlbum() {
		sortPlayList(PlayListAlbumComparator.comparator);
	}

	public void sortPlaylistByGenre() {
		sortPlayList(PlayListGenreComparator.comparator);
	}
	
	
	private static ArrayList<String> read(File file) {
		try{
			ArrayList<String> result = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith(M3U_START_COMMENT))
					result.add(line);
			}
			br.close();
			return result;
		} catch (IOException e) {
			return null;
		}
	}
	
	private static boolean write(PlayList playlist, String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists())
				file.delete();
			FileWriter writer = new FileWriter(file);
			writer.append(M3U_HEADER + SystemProperties.lineTerminator);
			for (Iterator it = playlist.iterator(); it.hasNext(); ) {
				AudioFile f = (AudioFile) it.next();
				writer.append(f.getAbsolutePath() + SystemProperties.lineTerminator);
			}
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static ArrayList<AudioFile> getFilesFromList(File file) {
		ArrayList list = read(file);
		ArrayList<AudioFile> result = new ArrayList<AudioFile>();
		for (int i = 0; i < list.size(); i++) {
			String fileName = (String)list.get(i);
			AudioFile f = HandlerProxy.getRepositoryHandler().getFileIfLoaded(fileName);
			if (f == null)
				f = new AudioFile(fileName);
			result.add(f);
		}
		return result;
	}

	public PlayListListener getPlayListListener() {
		return playListListener;
	}
	
	
	
	public void setFilter(String filter) {
		if (filter == null) {
			if (nonFilteredPlayList != null) {
				setPlayListAfterFiltering(nonFilteredPlayList);
				nonFilteredPlayList = null;
			}
			HandlerProxy.getVisualHandler().showFilter(false);
		}
		else {
			if (nonFilteredPlayList == null)
				nonFilteredPlayList = (PlayList) HandlerProxy.getPlayerHandler().getCurrentPlayList().clone();
			
			// Filter
			PlayList newPlayList = new PlayList();
			boolean filterArtist = HandlerProxy.getVisualHandler().getPlayListTableModel().isArtistVisible();
			boolean filterAlbum = HandlerProxy.getVisualHandler().getPlayListTableModel().isAlbumVisible();
			
			filter = filter.toLowerCase();
			
			for (AudioFile f: nonFilteredPlayList) {
				if (f.getTitleOrFileName().toLowerCase().contains(filter) || (filterArtist && f.getArtist().toLowerCase().contains(filter)) || (filterAlbum && f.getAlbum().toLowerCase().contains(filter)))
					newPlayList.add(f);
			}
			
			setPlayListAfterFiltering(newPlayList);
		}
	}
	
	private void setPlayListAfterFiltering(PlayList playList) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		
		if (playList.size() > currentPlayList.size()) { // Removing filter
			AudioFile selectedSong = currentPlayList.getCurrentFile();
			int index = playList.indexOf(selectedSong);
			((PlayListTableModel)table.getModel()).removeSongs();
			for (int i = 0; i < playList.size(); i++) {
				((PlayListTableModel)table.getModel()).addSong(playList.get(i));
				currentPlayList.add(playList.get(i));
			}
			HandlerProxy.getPlayerHandler().setCurrentPlayList(playList);
			HandlerProxy.getPlayerHandler().getCurrentPlayList().setNextFile(index != -1 ? index : 0);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(index != -1 ? index : 0);
			if (index == -1)
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(currentPlayList.get(0));
		}
		else {
			// Remove from table 
			ArrayList<Integer> rowsToRemove = new ArrayList<Integer>();
			for (int i = 0; i < currentPlayList.size(); i++) {
				AudioFile f = currentPlayList.get(i);
				if (!playList.contains(f)) {
					rowsToRemove.add(i);
				}
			}
			int[] rowsToRemoveArray = new int[rowsToRemove.size()];
			for (int i = 0; i < rowsToRemove.size(); i++)
				rowsToRemoveArray[i] = rowsToRemove.get(i);
			
			((PlayListTableModel)table.getModel()).removeSongs(rowsToRemoveArray);
			removeSongs(rowsToRemoveArray);
		}
		HandlerProxy.getVisualHandler().showPlaylistSongNumber(currentPlayList.size());
	}
	
	public void moveToTop(int[] rows) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		for (int i = 0; i < rows.length; i++) {
			AudioFile aux = currentPlayList.get(rows[i]);
			currentPlayList.remove(rows[i]);
			currentPlayList.add(i, aux);
		}
		if (rows[0] > currentPlayList.getNextFile()) {
			currentPlayList.setNextFile(currentPlayList.getNextFile() + rows.length);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		} else if (rows[0] <= currentPlayList.getNextFile() && currentPlayList.getNextFile() <= rows[rows.length-1]) {
			currentPlayList.setNextFile(currentPlayList.getNextFile() - rows[0]);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		}
	}
	
	public void moveUp(int[] rows) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		for (int i = 0; i < rows.length; i++) {
			AudioFile aux = currentPlayList.get(rows[i]);
			currentPlayList.remove(rows[i]);
			currentPlayList.add(rows[i]-1, aux);
		}
		if (rows[0] -1 == currentPlayList.getNextFile()) {
			currentPlayList.setNextFile(currentPlayList.getNextFile() + rows.length);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		} else if (rows[0] <= currentPlayList.getNextFile() && currentPlayList.getNextFile() <= rows[rows.length-1]) {
			currentPlayList.setNextFile(currentPlayList.getNextFile() - 1);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		}
	}

	public void moveDown(int[] rows) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		for (int i = rows.length-1; i >= 0; i--) {
			AudioFile aux = currentPlayList.get(rows[i]);
			currentPlayList.remove(rows[i]);
			currentPlayList.add(rows[i]+1, aux);
		}
		if (rows[rows.length-1] +1 == currentPlayList.getNextFile()) {
			currentPlayList.setNextFile(currentPlayList.getNextFile() - rows.length);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		} else if (rows[0] <= currentPlayList.getNextFile() && currentPlayList.getNextFile() <= rows[rows.length-1]){
			currentPlayList.setNextFile(currentPlayList.getNextFile() + 1);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		}
	}
	
	public void moveToBottom(int[] rows) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		int j = 0;
		for (int i = rows.length-1; i >= 0; i--) {
			AudioFile aux = currentPlayList.get(rows[i]);
			currentPlayList.remove(rows[i]);
			currentPlayList.add(currentPlayList.size() - j++, aux);
		}
		if (rows[rows.length-1] < currentPlayList.getNextFile()) {
			currentPlayList.setNextFile(currentPlayList.getNextFile() - rows.length);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		} else if (rows[0] <= currentPlayList.getNextFile() && currentPlayList.getNextFile() <= rows[rows.length-1]) {
			currentPlayList.setNextFile(currentPlayList.getNextFile() + currentPlayList.size() - rows[rows.length-1] - 1);
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
		}
	}

	public void removeSongs() {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		if (!currentPlayList.isEmpty()) {
			currentPlayList.clear();
			HandlerProxy.getPlayerHandler().stop();
			currentPlayList.setNextFile(0);
			HandlerProxy.getControllerHandler().getPlayListControlsController().enableSaveButton(false);
			HandlerProxy.getControllerHandler().getMenuController().enableSavePlaylist(false);
			HandlerProxy.getVisualHandler().showPlaylistSongNumber(currentPlayList.size());
			logger.info("Play list clear");
		}
	}
	
	public void removeSongs(int[] rows) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		AudioFile playingSong = currentPlayList.getCurrentFile();
		boolean hasToBeRemoved = false;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == currentPlayList.getNextFile())
				hasToBeRemoved = true;
		}
		for (int i = rows.length - 1; i >= 0; i--) {
			currentPlayList.remove(rows[i]);
		}
		
		if (hasToBeRemoved) {
			HandlerProxy.getPlayerHandler().stop();
			if (currentPlayList.isEmpty()) {
				HandlerProxy.getPlayListHandler().getPlayListListener().clear();
			} else {
				currentPlayList.setNextFile(0);
				HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(currentPlayList.getCurrentFile());
			}
		} else {
			currentPlayList.setNextFile(currentPlayList.indexOf(playingSong));
			HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(currentPlayList.getNextFile());
			HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(currentPlayList.getCurrentFile());
		}
		
		if (currentPlayList.isEmpty()) {
			HandlerProxy.getControllerHandler().getPlayListControlsController().enableSaveButton(false);
			HandlerProxy.getControllerHandler().getMenuController().enableSavePlaylist(false);
		}
		HandlerProxy.getVisualHandler().showPlaylistSongNumber(currentPlayList.size());
		logger.info(rows.length + " songs removed from play list");
	}

	public void addToPlayList(ArrayList<AudioFile> files) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		if (files != null && files.size() >= 1) {
			if (isFiltered()) {
				HandlerProxy.getControllerHandler().getPlayListFilterController().emptyFilter();
				setFilter(null);
			}
			
			if (currentPlayList.isEmpty()) {
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(files.get(0));
			}
			currentPlayList.addAll(files);
			if (currentPlayList.size() == files.size()) {
				HandlerProxy.getControllerHandler().getPlayListController().setSelectedSong(0);
				currentPlayList.setNextFile(0);
			}
			HandlerProxy.getControllerHandler().getPlayListController().addSongsToPlayList(files, -1);
			HandlerProxy.getVisualHandler().showPlaylistSongNumber(currentPlayList.size());
			logger.info(files.size() + " songs added to play list");
		}
	}
	
	public boolean isFiltered() {
		return nonFilteredPlayList != null;
	}
	
	private void setPlayList(ArrayList<AudioFile> files) {
		PlayList currentPlayList = HandlerProxy.getPlayerHandler().getCurrentPlayList();
		if (files != null && files.size() >= 1) {
			if (currentPlayList.isEmpty()) {
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(files.get(0));
			}
			HandlerProxy.getControllerHandler().getPlayListController().addSongsToPlayList(files, -1);
			HandlerProxy.getVisualHandler().showPlaylistSongNumber(currentPlayList.size());
			logger.info(files.size() + " songs setted as play list");
		}
	}
	
	public void finish() {
		HandlerProxy.getControllerHandler().getPlayListController().finish();
		ApplicationDataHandler.persistPlayList(HandlerProxy.getPlayerHandler().getCurrentPlayList());
	}
	
	public void clearList() {
		setFilter(null);
		PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
		((PlayListTableModel)table.getModel()).removeSongs();
		table.getSelectionModel().setSelectionInterval(-1,-1);
		removeSongs();
		playListListener.clear();
		HandlerProxy.getControllerHandler().getPlayListFilterController().emptyFilter();
	}

}
