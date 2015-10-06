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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.model.PlayListTableModel;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.gui.views.dialogs.FileSelectionDialog;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.repository.Repository;
import net.sourceforge.atunes.kernel.modules.repository.RepositoryAutoRefresher;
import net.sourceforge.atunes.kernel.modules.repository.RepositoryLoader;
import net.sourceforge.atunes.kernel.modules.repository.RepositoryLoaderListener;
import net.sourceforge.atunes.kernel.modules.repository.SongStats;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.model.info.Folder;
import net.sourceforge.atunes.model.info.TreeObject;
import net.sourceforge.atunes.utils.RankList;
import net.sourceforge.atunes.utils.StringUtils;
import net.sourceforge.atunes.utils.TimeUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;


public class RepositoryHandler implements RepositoryLoaderListener {

	private Logger logger = Logger.getLogger(RepositoryHandler.class);
	
	public enum SortType {BY_TRACK, BY_TITLE, BY_FILE}
	
	private Repository repository;
	private Repository tempRepository;
	private Repository deviceRepository;
	private Repository tempDeviceRepository;
	
	private int filesLoaded;
	
	private RepositoryLoader currentLoader;
	
	private RepositoryAutoRefresher repositoryRefresher;
	
	public RepositoryHandler() {
		this.repositoryRefresher = new RepositoryAutoRefresher(this);
	}

	public void notifyFilesInRepository(int totalFiles) {
		HandlerProxy.getVisualHandler().getProgressDialog().getProgressBar().setIndeterminate(false);
		HandlerProxy.getVisualHandler().getProgressDialog().getTotalFilesLabel().setText(" /  " + totalFiles);
		HandlerProxy.getVisualHandler().getProgressDialog().getProgressBar().setMaximum(totalFiles);
	}
	
	public void notifyFileLoaded() {
		this.filesLoaded++;
		final int aux = this.filesLoaded;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					HandlerProxy.getVisualHandler().getProgressDialog().getProgressLabel().setText(Integer.toString(aux));
					HandlerProxy.getVisualHandler().getProgressDialog().getProgressBar().setValue(aux);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyCurrentPath(final String dir) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					HandlerProxy.getVisualHandler().getProgressDialog().getFolderLabel().setText(dir);
				}
			});
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
	}
	
	public void notifyRemainingTime(final long millis) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					HandlerProxy.getVisualHandler().getProgressDialog().getRemainingTimeLabel().setText(LanguageTool.getString("REMAINING_TIME") + ":   " + TimeUtils.milliseconds2String(millis));
				}
			});
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public String getRepositoryPath() {
		return repository != null ? repository.getPath() : "";
	}
	
	public void readRepository() {
		// Try to read repository cache. If fails or not exists, should be selected again
		Repository rep = ApplicationDataHandler.retrieveRepositoryCache();
		if (rep != null) {
			if (!rep.getPathFile().exists()) {
				HandlerProxy.getVisualHandler().showMessage(LanguageTool.getString("REPOSITORY_NOT_FOUND") + ": " + rep.getPath());
				selectRepository();
			}
			else {
				tempRepository = rep;
				notifyFinishRepositoryRead(null);
			}
		}
		else {
			HandlerProxy.getVisualHandler().showRepositorySelectionInfoDialog();
			selectRepository();
		}
	}
	
	public void selectRepository() {
		FileSelectionDialog dialog = HandlerProxy.getVisualHandler().getFileSelectionDialog(true);
		dialog.setTitle(LanguageTool.getString("SELECT_REPOSITORY"));
		dialog.startDialog();
		if (!dialog.isCanceled()) {
			File dir = dialog.getSelectedDir();
			
			// Save previous repository
			if (repository != null && !repository.getPathFile().equals(dir)) {
				logger.info("Saving previous repository");
				ApplicationDataHandler.persistRepositoryForFuture(repository);
			}
			
			HandlerProxy.getVisualHandler().getProgressDialog().setVisible(true);
			HandlerProxy.getRepositoryHandler().retrieve(dir);
		}
	}
	
	public void changeToRepository(String s) {
		// Save previous
		if (repository != null) {
			logger.info("Saving previous repository");
			ApplicationDataHandler.persistRepositoryForFuture(repository);
		}
		
		Repository rep = ApplicationDataHandler.getSavedRepository(s);
		repository = rep;
		HandlerProxy.getPlayListHandler().clearList();
		notifyFinishRepositoryRead();
	}
	
	public boolean retrieve(File path) {
		filesLoaded = 0;
		try {
			if (path == null || !path.exists()) {
				repository = null;
				return false;
			}
			readRepository(path);
			return true;
		} catch (Exception e) {
			repository = null;
			logger.error(e.getMessage());
			logger.debug(e);
			return false;
		}
	}
	
	public void retrieveDevice(File path) {
		logger.info("Reading device mounted on " + path);
		currentLoader = new RepositoryLoader(path, false, true);
		currentLoader.addRepositoryLoaderListener(this);
		currentLoader.start();
	}
	
	private void readRepository(File path) {
		HandlerProxy.getVisualHandler().getProgressDialog().setCancelButtonVisible(true);
		HandlerProxy.getVisualHandler().getProgressDialog().setCancelButtonEnabled(true);
		currentLoader = new RepositoryLoader(path, false, false);
		currentLoader.addRepositoryLoaderListener(this);
		currentLoader.start();
	}

	/**
	 * Connect device
	 *
	 */
	public void connectDevice() {
		FileSelectionDialog dialog = HandlerProxy.getVisualHandler().getFileSelectionDialog(true);
		dialog.setTitle(LanguageTool.getString("SELECT_DEVICE"));
		dialog.startDialog();
		if (!dialog.isCanceled()) {
			File dir = dialog.getSelectedDir();
			HandlerProxy.getVisualHandler().showIconOnStatusBar(ImageLoader.DEVICE);
			HandlerProxy.getVisualHandler().showProgressBar(true);
			HandlerProxy.getRepositoryHandler().retrieveDevice(dir);
		}
	}
	
	private void refresh() {
		logger.info("Refreshing repository");
		filesLoaded = 0;
		currentLoader = new RepositoryLoader(repository.getPathFile(), true, false);
		currentLoader.addRepositoryLoaderListener(this);
		currentLoader.start();
	}
	
	public void addAndRefresh(ArrayList<File> files, File folder) {
		RepositoryLoader.addToRepository(repository, files, folder);
		
		// Persist
		ApplicationDataHandler.persistRepositoryCache(repository);
		
		HandlerProxy.getVisualHandler().showProgressBar(false);
		HandlerProxy.getVisualHandler().showRepositorySongNumber(getSongs().size(), getRepositoryTotalSize(), repository.getTotalDurationInSeconds());
		if (HandlerProxy.getControllerHandler().getNavigationController() != null)
			HandlerProxy.getControllerHandler().getNavigationController().notifyReload();
		logger.info("Repository refresh done");
	}

	public void refreshDevice() {
		HandlerProxy.getVisualHandler().showProgressBar(true);
		logger.info("Refreshing device");
		currentLoader = new RepositoryLoader(deviceRepository.getPathFile(), true, true);
		currentLoader.addRepositoryLoaderListener(this);
		currentLoader.start();
	}
	
	public void refreshFile(AudioFile file) {
		RepositoryLoader.refreshFile(repository, file);
	}
	
	public void refreshRepository() {
		if (!repositoryIsNull()) {
			HandlerProxy.getVisualHandler().setCenterStatusBarText(LanguageTool.getString("REFRESHING") + "...");
			HandlerProxy.getVisualHandler().showProgressBar(true);
			refresh();
		}
	}
	
	/**
	 * Disconnect device
	 *
	 */
	public void disconnectDevice() {
		HandlerProxy.getVisualHandler().showIconOnStatusBar(null);

		ArrayList<Integer> songsToRemove = new ArrayList<Integer>();
		for (int i = 0; i < HandlerProxy.getPlayerHandler().getCurrentPlayList().size(); i++) {
			AudioFile song = HandlerProxy.getPlayerHandler().getCurrentPlayList().get(i);
			if (song.getPath().startsWith(HandlerProxy.getRepositoryHandler().getDeviceRepository().getPath()))
				songsToRemove.add(i);
		}
		int[] indexes = new int[songsToRemove.size()];
		for (int i = 0; i < songsToRemove.size(); i++)
			indexes[i] = songsToRemove.get(i);
		
		if (indexes.length > 0) {
			PlayListTable table = HandlerProxy.getVisualHandler().getPlayListTable();
			((PlayListTableModel)table.getModel()).removeSongs(indexes);
			HandlerProxy.getVisualHandler().getPlayListPanel().getPlayListTable().getSelectionModel().setSelectionInterval(-1,-1);
			HandlerProxy.getPlayListHandler().removeSongs(indexes);
		}

		deviceRepository = null;
		notifyFinishDeviceRefresh(null);
		logger.info("Device disconnected");
		HandlerProxy.getControllerHandler().getMenuController().setDeviceConnected(false);
	}

	public void notifyFinishDeviceRead(RepositoryLoader loader) {
		deviceRepository = loader.getRepository();
		logger.info("Device read");
		notifyDeviceReload();
	}
	
	private void notifyFinishRepositoryRead() {
		HandlerProxy.getControllerHandler().getNavigationController().notifyReload();
		HandlerProxy.getVisualHandler().showRepositorySongNumber(getSongs().size(), getRepositoryTotalSize(), repository.getTotalDurationInSeconds());
	}
	
	public void notifyFinishRepositoryRead(RepositoryLoader loader) {
		if (loader != null)
			tempRepository = loader.getRepository();
		HandlerProxy.getVisualHandler().getProgressDialog().setCancelButtonEnabled(false);
		HandlerProxy.getVisualHandler().getProgressDialog().getLabel().setText(LanguageTool.getString("STORING_REPOSITORY_INFORMATION"));
		HandlerProxy.getVisualHandler().getProgressDialog().getProgressLabel().setText("");
		HandlerProxy.getVisualHandler().getProgressDialog().getTotalFilesLabel().setText("");
		HandlerProxy.getVisualHandler().getProgressDialog().getFolderLabel().setText(" ");
		
		// Set new repository
		repository = tempRepository;
		
		HandlerProxy.getVisualHandler().getProgressDialog().setVisible(false);
		HandlerProxy.getVisualHandler().getProgressDialog().setCancelButtonVisible(false);
		HandlerProxy.getVisualHandler().getProgressDialog().setCancelButtonEnabled(true);

		notifyFinishRepositoryRead();
	}
	
	public void notifyFinishRepositoryRefresh(RepositoryLoader loader) {
		tempRepository = loader.getRepository();
		// Save favorites
		HashMap<String, Album> albums = repository.getFavorites().getFavoriteAlbums();
		HashMap<String, Artist> artists = repository.getFavorites().getFavoriteArtists();
		HashMap<String, AudioFile> songs = repository.getFavorites().getFavoriteSongs();
		tempRepository.getFavorites().setFavoriteAlbums(albums);
		tempRepository.getFavorites().setFavoriteArtists(artists);
		tempRepository.getFavorites().setFavoriteSongs(songs);
		
		// Save stats
		HashMap<String, SongStats> songsStats = repository.getStats().getSongsStats();
		RankList songsRanking = repository.getStats().getSongsRanking();
		RankList albumsRanking = repository.getStats().getAlbumsRanking();
		RankList artistsRanking = repository.getStats().getArtistsRanking();
		int totalPlays = repository.getStats().getTotalPlays();
		int differentSongsPlayed = repository.getStats().getDifferentSongsPlayed();
		tempRepository.getStats().setSongsStats(songsStats);
		tempRepository.getStats().setSongsRanking(songsRanking);
		tempRepository.getStats().setAlbumsRanking(albumsRanking);
		tempRepository.getStats().setArtistsRanking(artistsRanking);
		tempRepository.getStats().setTotalPlays(totalPlays);
		tempRepository.getStats().setDifferentSongsPlayed(differentSongsPlayed);
		
		// Set new repository
		repository = tempRepository;

		// Persist
		ApplicationDataHandler.persistRepositoryCache(repository);
		
		HandlerProxy.getVisualHandler().showProgressBar(false);
		HandlerProxy.getVisualHandler().showRepositorySongNumber(getSongs().size(), getRepositoryTotalSize(), repository.getTotalDurationInSeconds());
		if (HandlerProxy.getControllerHandler().getNavigationController() != null)
			HandlerProxy.getControllerHandler().getNavigationController().notifyReload();
		logger.info("Repository refresh done");
	}
	
	public void notifyFinishDeviceRefresh(RepositoryLoader loader) {
		if (loader != null) 
			tempDeviceRepository = loader.getRepository();
		deviceRepository = tempDeviceRepository;
		tempDeviceRepository = null;
		notifyDeviceReload();
	}
	
	public void notifyCancel() {
		currentLoader.interruptLoad();
		HandlerProxy.getVisualHandler().getProgressDialog().setVisible(false);
		HandlerProxy.getVisualHandler().getProgressDialog().setCancelButtonVisible(false);
		HandlerProxy.getVisualHandler().getProgressDialog().setCancelButtonEnabled(true);
	}
	
	public void finish() {
		repositoryRefresher.interrupt();
		ApplicationDataHandler.persistRepositoryCache(repository);
	}
		
	public HashMap<String, Artist> getArtistAndAlbumStructure() {
		if (repository != null)
			return repository.getStructure().getTreeStructure();
		return new HashMap<String, Artist>();
	}
	
	public HashMap<String, Artist> getDeviceArtistAndAlbumStructure() {
		if (deviceRepository != null)
			return deviceRepository.getStructure().getTreeStructure();
		return new HashMap<String, Artist>();
	}
	
	public HashMap<String, Folder> getFolderStructure() {
		if (repository != null)
			return repository.getStructure().getFolderStructure();
		return new HashMap<String, Folder>();
	}

	public void addExternalPictureForAlbum(String artistName, String albumName, File picture) {
		if (repository != null) {
			Artist artist = repository.getStructure().getTreeStructure().get(artistName);
			Album album = artist.getAlbum(albumName);
			ArrayList<AudioFile> songs = album.getSongs();
			for (AudioFile f: songs) {
				f.addExternalPicture(picture);
			}
		}
	}
	
	public HashMap<String, Folder> getDeviceFolderStructure() {
		if (deviceRepository != null)
			return deviceRepository.getStructure().getFolderStructure();
		return new HashMap<String, Folder>();
	}
	
	public ArrayList<AudioFile> getSongs() {
		if (repository != null)
			return repository.getFilesList();
		return new ArrayList<AudioFile>();
	}
	
	public ArrayList<AudioFile> getDeviceSongs() {
		if (deviceRepository != null)
			return deviceRepository.getFilesList();
		return new ArrayList<AudioFile>();
	}
	
	public ArrayList<AudioFile> sort(ArrayList<AudioFile> songs, SortType type) {
		AudioFile[] array = songs.toArray(new AudioFile[songs.size()]);
		
		if (type == SortType.BY_TRACK) {
			Arrays.sort(array, new Comparator<AudioFile>() {
				public int compare(AudioFile a1, AudioFile a2) {
					int c1 = a1.getArtist().compareTo(a2.getArtist());
					if (c1 != 0)
						return c1;
					
					int c2 = a1.getAlbum().compareTo(a2.getAlbum());
					if (c2 != 0)
						return c2;

					return a1.getTrackNumber().compareTo(a2.getTrackNumber());
					
//					if (a1.getArtist().equals(a2.getArtist())) {
//						if (a1.getAlbum().equals(a2.getAlbum()))
//							return a1.getTrackNumber().compareTo(a2.getTrackNumber());
//						return a1.getAlbum().compareTo(a2.getAlbum());
//					}
//					return a1.getArtist().compareTo(a2.getArtist());					
				}
			});
		}
		else if (type == SortType.BY_TITLE) {
			Arrays.sort(array, new Comparator<AudioFile>() {
				public int compare(AudioFile a0, AudioFile a1) {
					return a0.getTitleOrFileName().compareTo(a1.getTitleOrFileName());
				}
			});
		}
		else { // Sort songs by file name
			Arrays.sort(array);
		}

		ArrayList<AudioFile> songsArray = new ArrayList<AudioFile>();
		for (int i = 0; i < array.length; i++)
			songsArray.add(array[i]);
		
		return songsArray;
	}
	
	public AudioFile getFileIfLoaded(String fileName) {
		return repository == null ? null : repository.getFile(fileName);
	}
	
	public void addFavoriteSongs(ArrayList<AudioFile> songs) {
		HashMap<String, AudioFile> favSongs = repository.getFavorites().getFavoriteSongs();
		for (int i = 0; i < songs.size(); i++) {
			AudioFile f = songs.get(i);
			favSongs.put(f.getAbsolutePath(), f);
		}
		// Update playlist to add favorite icon
		HandlerProxy.getVisualHandler().getPlayListTableModel().refresh();
		HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
		
		// Update favorite tree
		HandlerProxy.getControllerHandler().getNavigationController().refreshFavoriteTree();
		
		// Update file properties panel
		HandlerProxy.getControllerHandler().getFilePropertiesController().refreshFavoriteIcons();
	}
	
	public void addFavoriteAlbums(ArrayList<AudioFile> songs) {
		HashMap<String, Artist> structure = repository.getStructure().getTreeStructure();
		HashMap<String, Album> favAlbums = repository.getFavorites().getFavoriteAlbums();
		for (int i = 0; i < songs.size(); i++) {
			AudioFile f = songs.get(i);
			Artist artist = structure.get(f.getArtist());
			Album album = artist.getAlbum(f.getAlbum());
			favAlbums.put(album.getName(), album);
		}
		// Update playlist to add favorite icon
		HandlerProxy.getVisualHandler().getPlayListTableModel().refresh();
		HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();

		// Update favorite tree
		HandlerProxy.getControllerHandler().getNavigationController().refreshFavoriteTree();
		
		// Update file properties panel
		HandlerProxy.getControllerHandler().getFilePropertiesController().refreshFavoriteIcons();
	}
	
	public void addFavoriteArtists(ArrayList<AudioFile> songs) {
		HashMap<String, Artist> structure = repository.getStructure().getTreeStructure();
		HashMap<String, Artist> favArtists = repository.getFavorites().getFavoriteArtists();
		for (int i = 0; i < songs.size(); i++) {
			AudioFile f = songs.get(i);
			Artist artist = structure.get(f.getArtist());
			favArtists.put(artist.getName(), artist);
		}
		// Update playlist to add favorite icon
		HandlerProxy.getVisualHandler().getPlayListTableModel().refresh();
		HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();

		// Update favorite tree
		HandlerProxy.getControllerHandler().getNavigationController().refreshFavoriteTree();
		
		// Update file properties panel
		HandlerProxy.getControllerHandler().getFilePropertiesController().refreshFavoriteIcons();
	}
	
	public HashMap<String, Artist> getFavoriteArtistsInfo() {
		if (repository != null)
			return repository.getFavorites().getFavoriteArtists();
		return new HashMap<String, Artist>();
	}
	
	public HashMap<String, Album> getFavoriteAlbumsInfo() {
		if (repository != null)
			return repository.getFavorites().getFavoriteAlbums();
		return new HashMap<String, Album>();
	}
	
	public HashMap<String, AudioFile> getFavoriteSongsInfo() {
		if (repository != null)
			return repository.getFavorites().getFavoriteSongs();
		return new HashMap<String, AudioFile>();
	}
	
	public ArrayList<AudioFile> getSongsForArtists(HashMap<String, Artist> artists) {
		ArrayList<AudioFile> result = new ArrayList<AudioFile>();
		for (Iterator<String> it = artists.keySet().iterator(); it.hasNext(); ) {
			Artist a = artists.get(it.next());
			result.addAll(a.getSongs());
		}
		return result;
	}

	public ArrayList<AudioFile> getSongsForAlbums(HashMap<String, Album> albums) {
		ArrayList<AudioFile> result = new ArrayList<AudioFile>();
		for (Iterator<String> it = albums.keySet().iterator(); it.hasNext(); ) {
			Album a = albums.get(it.next());
			result.addAll(a.getSongs());
		}
		return result;
	}

	public void removeFromFavorites(TreeObject obj) {
		if (obj instanceof Artist)
			repository.getFavorites().getFavoriteArtists().remove(obj.toString());
		else
			repository.getFavorites().getFavoriteAlbums().remove(obj.toString());
		
		// Update playlist to remove favorite icon
		HandlerProxy.getVisualHandler().getPlayListTableModel().refresh();
		HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
		
		// Update favorites tree
		HandlerProxy.getControllerHandler().getNavigationController().refreshFavoriteTree();
		
		// Update file properties panel
		HandlerProxy.getControllerHandler().getFilePropertiesController().refreshFavoriteIcons();
	}
	
	public void removeSongFromFavorites(AudioFile file) {
		repository.getFavorites().getFavoriteSongs().remove(file.getAbsolutePath());

		// Update playlist to remove favorite icon
		HandlerProxy.getVisualHandler().getPlayListTableModel().refresh();
		HandlerProxy.getControllerHandler().getPlayListController().adjustColumnsWidth();
		
		// Update favorites tree
		HandlerProxy.getControllerHandler().getNavigationController().refreshFavoriteTree();
		
		// Update file properties panel
		HandlerProxy.getControllerHandler().getFilePropertiesController().refreshFavoriteIcons();
	}
	
	public boolean repositoryIsNull() {
		return repository == null; 
	}
	
	public ArrayList<AudioFile> getFavoriteSongs() {
		if (repository != null)
			return repository.getFavorites().getAllFavoriteSongs();
		return new ArrayList<AudioFile>();
	}

	public Repository getDeviceRepository() {
		return deviceRepository;
	}
	
	public long getRepositoryTotalSize() {
		return repository != null ? repository.getTotalSizeInBytes() : 0;
	}
	
	public void setSongStatistics(AudioFile song) {
		if (repository != null) {
			RepositoryLoader.fillStats(repository, song);
		}
	}
	
	public SongStats getSongStatistics(AudioFile song) {
		if (repository != null) {
			return repository.getStats().getStatsForFile(song);
		}
		return null;
	}
	
	public Integer getArtistTimesPlayed(AudioFile song) {
		if (song != null) {
			if (repository != null && repository.getStats().getArtistsRanking().getCount(song.getArtist()) != null) {
				return repository.getStats().getArtistsRanking().getCount(song.getArtist());
			}
			return 0;
		}
		return null;
	}
	
	public Integer getAlbumTimesPlayed(AudioFile song) {
		if (song != null) {
			if (repository != null && repository.getStats().getAlbumsRanking().getCount(song.getAlbum()) != null) {
				return repository.getStats().getAlbumsRanking().getCount(song.getAlbum());
			}
			return 0;
		}
		return null;
	}
	
	public String getSongsPlayed() {
		if (repository != null) {
			int totalPlays = repository.getStats().getDifferentSongsPlayed();
			int total = repository.countFiles();
			float perCent = (float) totalPlays / (float) total * 100;
			return totalPlays + " / " + total + " (" + StringUtils.toString(perCent, 2) + "%)"; 
		}
		return "0 / 0 (0%)"; 
	}
	
	public int getDifferentSongsPlayed() {
		if (repository != null) {
			return repository.getStats().getDifferentSongsPlayed();
		}
		return 0;
	}
	
	public HashMap<String, Integer> getArtistMostPlayed() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		if (repository != null && repository.getStats().getArtistsRanking().size() > 0) {
			String firstArtist = (String) repository.getStats().getArtistsRanking().getNFirstElements(1).get(0);
			Integer count = repository.getStats().getArtistsRanking().getNFirstElementCounts(1).get(0); 
			result.put(firstArtist, count);
		}
		else
			result.put(null, 0);
		return result;
	}

	public HashMap<String, Integer> getAlbumMostPlayed() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		if (repository != null && repository.getStats().getAlbumsRanking().size() > 0) {
			String firstAlbum = (String) repository.getStats().getAlbumsRanking().getNFirstElements(1).get(0);
			Integer count = repository.getStats().getAlbumsRanking().getNFirstElementCounts(1).get(0);
			result.put(firstAlbum, count);
		}
		else
			result.put(null, 0);
		return result;
	}
	
	public HashMap<AudioFile, Integer> getSongMostPlayed() {
		HashMap<AudioFile, Integer> result = new HashMap<AudioFile, Integer>();
		if (repository != null && repository.getStats().getSongsRanking().size() > 0) {
			AudioFile firstSong = (AudioFile) repository.getStats().getSongsRanking().getNFirstElements(1).get(0);
			Integer count = repository.getStats().getSongsRanking().getNFirstElementCounts(1).get(0); 
			result.put(firstSong, count);
		}
		else
			result.put(null, 0);
		return result;
	} 
	
	public ArrayList<Object[]> getMostPlayedArtists(int n) {
		if (repository != null) {
			ArrayList<Object[]> result = new ArrayList<Object[]>();
			ArrayList artists = repository.getStats().getArtistsRanking().getNFirstElements(n);
			ArrayList count = repository.getStats().getArtistsRanking().getNFirstElementCounts(n);
			if (artists != null)
				for (int i = 0; i < artists.size(); i++) {
					Object[] obj = new Object[2];
					obj[0] = artists.get(i);
					obj[1] = count.get(i);
					result.add(obj);
				}
			return result;
		}
		return null;
	}
	
	public ArrayList<Object[]> getMostPlayedAlbums(int n) {
		if (repository != null) {
			ArrayList<Object[]> result = new ArrayList<Object[]>();
			ArrayList albums = repository.getStats().getAlbumsRanking().getNFirstElements(n);
			ArrayList count = repository.getStats().getAlbumsRanking().getNFirstElementCounts(n);
			if (albums != null)
				for (int i = 0; i < albums.size(); i++) {
					Object[] obj = new Object[2];
					obj[0] = albums.get(i);
					obj[1] = count.get(i);
					result.add(obj);
				}
			return result;
		}
		return null;
	}

	public ArrayList<Object[]> getMostPlayedSongs(int n) {
		if (repository != null) {
			ArrayList<Object[]> result = new ArrayList<Object[]>();
			ArrayList songs = repository.getStats().getSongsRanking().getNFirstElements(n);
			ArrayList count = repository.getStats().getSongsRanking().getNFirstElementCounts(n);
			if (songs != null)
				for (int i = 0; i < songs.size(); i++) {
					Object[] obj = new Object[2];
					AudioFile song = (AudioFile) songs.get(i);
					obj[0] = song.getTitleOrFileName() + " (" + song.getArtist() + ')';
					obj[1] = count.get(i);
					result.add(obj);
				}
			return result;
		}
		return null;
	}
	
	public int getTotalSongsPlayed() {
		return repository != null ? repository.getStats().getTotalPlays() : -1;
	}
	
	private void notifyDeviceReload() {
		if (HandlerProxy.getControllerHandler().getNavigationController() != null) {
			HandlerProxy.getVisualHandler().showProgressBar(false);
			HandlerProxy.getControllerHandler().getNavigationController().notifyDeviceReload();
			HandlerProxy.getControllerHandler().getMenuController().setDeviceConnected(true);
		}
	}
}
