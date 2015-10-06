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

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerAlbum;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerArtist;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerListener;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerRunnable;
import net.sourceforge.atunes.kernel.modules.audioscrobbler.AudioScrobblerService;
import net.sourceforge.atunes.kernel.modules.lyrics.LyricsService;
import net.sourceforge.atunes.kernel.modules.network.NetworkUtils;
import net.sourceforge.atunes.kernel.modules.proxy.Proxy;
import net.sourceforge.atunes.kernel.modules.proxy.ProxyBean;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.utils.PictureExporter;
import net.sourceforge.atunes.kernel.utils.SystemProperties;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;

public class AudioScrobblerServiceHandler implements AudioScrobblerListener {

	Logger logger = Logger.getLogger(AudioScrobblerServiceHandler.class);
	
	private AudioScrobblerService service;
	
	private AudioScrobblerAlbum album;
	private Image image;
	private ArrayList<AudioScrobblerAlbum> albums;
	
	private AudioScrobblerRunnable currentWorker;
	
	private String lastAlbumRetrieved;
	private String lastArtistRetrieved;
	
	public AudioScrobblerServiceHandler() {
		updateService(Kernel.getInstance().state.getProxy());
	}

	public void updateService(ProxyBean proxy) {
		cancelIfWorking();
		Proxy p = null;
		try {
			if (proxy != null) {
				p = NetworkUtils.getProxy(proxy);
			}
			service = new AudioScrobblerService(p);
		} catch (Exception e) {
			logger.error(e);
			service = null;
		}
		lastAlbumRetrieved = null;
		lastArtistRetrieved = null;
	}
	
	private void cancelIfWorking() {
		if (currentWorker != null) {
			currentWorker.interrupt();
			currentWorker = null;
			logger.debug("Suspended Thread");
		}
	}
	
	public void retrieveInfo(final AudioFile file) {
		if (file == null)
			return;
		
		//if (file.getArtist().equals(LanguageTool.getString("UNKNOWN_ARTIST")))
		
		Runnable lyricsRunnable = new Runnable() {
			public void run() {
				try {
					if (file.getTitle().trim().equals("") || file.getArtist().equals(LanguageTool.getString("UNKNOWN_ARTIST")))
						return;
					String lyrics = LyricsService.getLyrics(NetworkUtils.getProxy(Kernel.getInstance().state.getProxy()), file.getArtist(), file.getTitle());
					HandlerProxy.getControllerHandler().getAudioScrobblerController().setLyrics(file.getTitle(), file.getArtist(), lyrics);
				} catch (UnknownHostException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		};
		
		Thread lyricsThread = new Thread(lyricsRunnable);
		lyricsThread.start();

		if (file.getArtist().equals(lastArtistRetrieved) && file.getAlbum().equals(lastAlbumRetrieved)) {
			HandlerProxy.getControllerHandler().getAudioScrobblerController().notifyFinishGetAlbumInfo(album, image);
			return;
		}
		cancelIfWorking();
		HandlerProxy.getControllerHandler().getAudioScrobblerController().clear();

		currentWorker = new AudioScrobblerRunnable(this, service, file);
		
		Thread t = new Thread(currentWorker);
		t.start();
	}

	public void savePicture(Image image, AudioFile file) {
		if (image != null && Kernel.getInstance().state.SAVE_PICTURE_FROM_AUDIO_SCROBBLER) { // save image in folder of file
			String imageFileName = file.getParentFile().getAbsolutePath() + SystemProperties.fileSeparator + 
								   file.getArtist() + '_' + file.getAlbum() + "_Cover.jpg";
			
			File imageFile = new File(imageFileName);
			if (!imageFile.exists()) {
				// Save picture
				try {
					PictureExporter.savePicture(image, imageFileName);
					// Add picture to songs of album
					HandlerProxy.getRepositoryHandler().addExternalPictureForAlbum(file.getArtist(), file.getAlbum(), imageFile);
					
					// Update file properties panel
					HandlerProxy.getControllerHandler().getFilePropertiesController().refreshPicture();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}

	}
	
	public void notifyAlbumRetrieved(AudioFile file) {
		HandlerProxy.getControllerHandler().getAudioScrobblerController().notifyFinishGetAlbumInfo(album, image);
		lastAlbumRetrieved = file.getAlbum();
		lastArtistRetrieved = file.getArtist();
	}
	
	public void notifyCoverRetrieved(AudioScrobblerAlbum album, Image cover) {
		HandlerProxy.getControllerHandler().getAudioScrobblerController().notifyFinishGetAlbumsInfo(album, cover);
	}
	
	public void notifyArtistImage(Image img) {
		HandlerProxy.getControllerHandler().getAudioScrobblerController().notifyArtistImage(img);
	}
	
	public void notifyFinishGetSimilarArtist(AudioScrobblerArtist a, Image img) {
		HandlerProxy.getControllerHandler().getAudioScrobblerController().notifyFinishGetSimilarArtist(a, img);
	}
	
	public ArrayList<String> getTrackNamesForAlbum(String artistName, String albumName) {
		AudioScrobblerAlbum album = service.getAlbum(artistName, albumName);
		if (album == null)
			return null;
		logger.info("Received track names for artist " + artistName + " album " + albumName);
		ArrayList<String> tracks = new ArrayList<String>();
		for (int i = 0; i < album.getTracks().size(); i++) {
			tracks.add(album.getTracks().get(i).getTitle());
		}
		return tracks;
	}
	
	public HashMap<AudioFile, String> getGenresForFiles(ArrayList<AudioFile> files) {
		HashMap<AudioFile, String> result = new HashMap<AudioFile, String>();
		
		HashMap<String, String> tagCache = new HashMap<String, String>();
		
		for (AudioFile f : files) {
			String tag = null;
			if (tagCache.containsKey(f.getArtist()))
				tag = tagCache.get(f.getArtist());
			else {
				tag = service.getArtistTopTag(f.getArtist());
				tagCache.put(f.getArtist(), tag);
			}
			result.put(f, tag);
		}
		
		return result;
	}

	public String getLastAlbumRetrieved() {
		return lastAlbumRetrieved;
	}

	public String getLastArtistRetrieved() {
		return lastArtistRetrieved;
	}

	public AudioScrobblerAlbum getAlbum() {
		return album;
	}

	public void setAlbum(AudioScrobblerAlbum album) {
		this.album = album;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	public void setAlbums(ArrayList<AudioScrobblerAlbum> albums) {
		this.albums = albums;
	}

	public ArrayList<AudioScrobblerAlbum> getAlbums() {
		return albums;
	}

	public void setLastAlbumRetrieved(String lastAlbumRetrieved) {
		this.lastAlbumRetrieved = lastAlbumRetrieved;
	}

	public void setLastArtistRetrieved(String lastArtistRetrieved) {
		this.lastArtistRetrieved = lastArtistRetrieved;
	}
}
