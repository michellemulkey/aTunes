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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.WindowConstants;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.modules.repository.Repository;
import net.sourceforge.atunes.kernel.modules.state.ApplicationState;
import net.sourceforge.atunes.kernel.utils.SystemProperties;
import net.sourceforge.atunes.model.player.PlayList;

import org.apache.log4j.Logger;

import com.fleax.ant.BuildNumberReader;

/**
 * This class is responsible of read, write and apply application state, and caches
 */
public class ApplicationDataHandler {

	/** 
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(ApplicationDataHandler.class);
	
	/**
	 * Read state stored.
	 */
	public static boolean readState() {
		try {
			ObjectInputStream ois;
			ois = new ObjectInputStream(new FileInputStream(getPropertiesFile(Kernel.DEBUG)));
			Kernel.getInstance().state = (ApplicationState) ois.readObject();
			ois.close();
			return (Kernel.getInstance().state.getVersion() == BuildNumberReader.getBuildNumber());
		} catch (Exception e) {
			logger.info("Could not read application state");
			return false;
		}
	}
	
	/**
	 * Stores state
	 */
	public static void storeState() {
		Kernel kernel = Kernel.getInstance();

		if (!HandlerProxy.getVisualHandler().isMultipleWindow()) {
			// Set window location on state
			kernel.state.setWindowLocation(HandlerProxy.getVisualHandler().getWindowLocation());
			// Window full maximized
			kernel.state.setMaximized(HandlerProxy.getVisualHandler().isMaximized());
			// Set window size
			kernel.state.setWindowSize(HandlerProxy.getVisualHandler().getWindowSize());
		}
		else {
			// Set window location on state
			kernel.state.setMultipleViewLocation(HandlerProxy.getVisualHandler().getWindowLocation());
			// Set window size
			kernel.state.setMutipleViewSize(HandlerProxy.getVisualHandler().getWindowSize());
		}
		
		kernel.state.setVolume(HandlerProxy.getPlayerHandler().getVolume());
		
		// Now write state
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(getPropertiesFile(Kernel.DEBUG)));
			oos.writeObject(kernel.state);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			logger.error("Error storing application state");
		}
	}
	
	/**
	 * Apply state. Some properties (window maximized, position, etc) are already setted in gui creation
	 *
	 */
	public static void applyState() {
		Kernel kernel = Kernel.getInstance();
		ApplicationState state = kernel.state;
		
		//System tray player
		if (state.isShowTrayPlayer()) {
			HandlerProxy.getSystemTrayHandler().initTrayPlayerIcons();
		}
		
		// System tray
		if (state.isShowSystemTray()) {
			HandlerProxy.getSystemTrayHandler().initTrayIcon();
		}
		else {
			HandlerProxy.getVisualHandler().setFrameDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}
		
		// Show playlist controls
		HandlerProxy.getControllerHandler().getPlayListController().showPlaylistControls(state.isShowPlaylistControls());
		
		// Show OSD
		state.setShowOSD(state.isShowOSD());
		HandlerProxy.getControllerHandler().getMenuController().setShowOSD(state.isShowOSD());
		HandlerProxy.getSystemTrayHandler().setShowOSD(state.isShowOSD());
					
		// Shuffle and repeat
		HandlerProxy.getControllerHandler().getPlayerControlsController().setShuffle(state.isShuffle());
		HandlerProxy.getPlayerHandler().setShuffle(state.isShuffle());
		HandlerProxy.getSystemTrayHandler().setShuffle(state.isShuffle());
		HandlerProxy.getControllerHandler().getPlayerControlsController().setRepeat(state.isRepeat());
		HandlerProxy.getPlayerHandler().setRepeat(state.isRepeat());
		HandlerProxy.getSystemTrayHandler().setRepeat(state.isRepeat());

		// Status Bar
		HandlerProxy.getVisualHandler().showStatusBar(state.isShowStatusBar());
		HandlerProxy.getControllerHandler().getMenuController().setShowStatusBar(state.isShowStatusBar());
		
		// Song properties visible
		HandlerProxy.getVisualHandler().showSongProperties(state.isShowSongProperties(), false);
		
		// Show navigation panel
		HandlerProxy.getVisualHandler().showNavigationPanel(state.isShowNavigationPanel(), false);
		
		// Show AudioScrobbler
		HandlerProxy.getVisualHandler().showAudioScrobblerPanel(state.isUseAudioScrobbler(), false);

		// Show navigation table
		HandlerProxy.getVisualHandler().showNavigationTable(state.isShowNavigationTable());
		HandlerProxy.getControllerHandler().getMenuController().setShowNavigationTable(state.isShowNavigationTable());

		// Navigation Panel View
		HandlerProxy.getControllerHandler().getNavigationController().setNavigationView(state.getNavigationView());
		
		// Sort device by tag
		HandlerProxy.getControllerHandler().getMenuController().setSortDeviceByTag(state.isSortDeviceByTag());
		
		// Show track in play list
		HandlerProxy.getVisualHandler().getPlayListTableModel().setTrackVisible(state.isShowTrackInPlayList());
		
		// Show artist in play list
		HandlerProxy.getVisualHandler().getPlayListTableModel().setArtistVisible(state.isShowArtistInPlayList());
		
		// Show album in play list
		HandlerProxy.getVisualHandler().getPlayListTableModel().setAlbumVisible(state.isShowAlbumInPlayList());
		
		// Show genre in play list
		HandlerProxy.getVisualHandler().getPlayListTableModel().setGenreVisible(state.isShowGenreInPlayList());
		
		// Set volume
		HandlerProxy.getVisualHandler().setVolume(state.getVolume());
		HandlerProxy.getPlayerHandler().setVolume(state.getVolume());
	}
	
	/**
	 * Gets file where state is stored 
	 * @param useWorkDir
	 * @return
	 */
	private static String getPropertiesFile(boolean useWorkDir) {
		return SystemProperties.getUserConfigFolder(useWorkDir) + '/' + Constants.PROPERTIES_FILE;
	}
	
	/**
	 * Reads repository cache
	 * @return
	 */
	protected static Repository retrieveRepositoryCache() {
		Repository result;
		try {
			FileInputStream fis = new FileInputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.CACHE_REPOSITORY_NAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			logger.info("Reading repository cache");
			long t0 = System.currentTimeMillis();
			result = (Repository) ois.readObject();
			ois.close();
			long t1 = System.currentTimeMillis();
			logger.info("Reading repository cache done (" + (t1-t0)/1000.0 + " seconds)");
		} catch (Exception e) {
			logger.info("No repository info found");
			result = null;
		}
		return result;
	}
	
	/**
	 * Stores repository cache
	 * @param repository
	 */
	protected static void persistRepositoryCache(Repository repository) {
		try {
			FileOutputStream fout = new FileOutputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.CACHE_REPOSITORY_NAME);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			logger.info("Storing repository information...");
			long t0 = System.currentTimeMillis();
			oos.writeObject(repository);
			oos.close();
			long t1 = System.currentTimeMillis();
			logger.info("DONE (" + (t1-t0)/1000.0 + " seconds)");
		} catch (Exception e) {
			logger.error("Could not write repository");
			logger.debug(e);
		}
	}
	
	/**
	 * Returns a string list of saved repositories, younger first
	 * @return list of saved repositories or null
	 */
	public static ArrayList<String> getSavedRepositoriesList() {
		ArrayList<Repository> reps = null;
		try {
			// Read previous repositories
			FileInputStream fis = new FileInputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.CACHE_SAVED_REPOSITORIES_NAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			reps= (ArrayList<Repository>) ois.readObject();
			ois.close();
		} catch (Exception e) {
		}
		
		if (reps != null) {
			ArrayList<String> res = new ArrayList<String>();
			for (Repository r: reps) {
				if (r.getPathFile().exists())
					res.add(r.getPath());
			}
			Collections.reverse(res);
			return res;
		}
		return null;
	}
	
	/** 
	 * Returns a repository by path
	 */
	public static Repository  getSavedRepository(String s) {
		ArrayList<Repository> reps = null;
		try {
			// Read previous repositories
			FileInputStream fis = new FileInputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.CACHE_SAVED_REPOSITORIES_NAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			reps = (ArrayList<Repository>) ois.readObject();
			ois.close();
		} catch (Exception e) {
		}
		
		if (reps != null) {
			for (Repository r: reps) {
				if (r.getPath().equals(s))
					return r;
			}
		}
		return null;
	}
	
	
	/**
	 * Stores a repository for future reuse
	 * @param repository
	 */
	protected static void persistRepositoryForFuture(Repository repository) {
		ArrayList<Repository> repositories = null;
		try {
			// Read previous repositories
			FileInputStream fis = new FileInputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.CACHE_SAVED_REPOSITORIES_NAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			repositories = (ArrayList<Repository>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			logger.info("No saved repositories");
		}
		
		if (repositories == null)
			repositories = new ArrayList<Repository>();
		else {
			//Check repository has not been saved before. If saved, delete old
			Repository oldRep = null;
			for (Repository r: repositories) {
				if (r.getPathFile().equals(repository.getPathFile())) {
					oldRep = r;
					break;
				}
			}
			if (oldRep != null)
				repositories.remove(oldRep);
			
			// Check if list exceeds MAX_RECENT_REPOSITORIES. If does, delete first element
			if (repositories.size() == Constants.MAX_RECENT_REPOSITORIES)
				repositories.remove(0);
		}

		repositories.add(repository);
		
		try {
			FileOutputStream fout = new FileOutputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.CACHE_SAVED_REPOSITORIES_NAME);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			logger.info("Storing repository information...");
			long t0 = System.currentTimeMillis();
			oos.writeObject(repositories);
			oos.close();
			long t1 = System.currentTimeMillis();
			logger.info("DONE (" + (t1-t0)/1000.0 + " seconds)");
		} catch (Exception e) {
			logger.error("Could not write repository");
			logger.debug(e);
		}
	}
	
	/**
	 * Reads play list cache
	 * @return
	 */
	protected static PlayList retrievePlayListCache() {
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.LAST_PLAYLIST_FILE));
			PlayList obj = (PlayList) stream.readObject();
			logger.info("Play list loaded (" + obj.size() + " songs)");
			return obj;
		} catch (Exception e) {
			return new PlayList();
		}
	}
	
	/**
	 * Stores play list
	 * @param p
	 */
	public static void persistPlayList(PlayList p) {
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(SystemProperties.getUserConfigFolder(Kernel.DEBUG) + '/' + Constants.LAST_PLAYLIST_FILE));
			stream.writeObject(p);
			stream.close();
			logger.info("Play list saved");
		} catch (Exception e) {
			logger.error("Could not persist playlist");
			logger.debug(e);
		}
	}
}
