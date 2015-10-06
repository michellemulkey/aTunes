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

package net.sourceforge.atunes.kernel.modules.audioscrobbler;

import java.awt.Image;
import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class AudioScrobblerAlbumsRunnable implements Runnable {
	
	private AudioScrobblerListener listener;
	private AudioScrobblerService service;
	private AudioFile file;
	
	private boolean interrupted;
	
	protected AudioScrobblerAlbumsRunnable(AudioScrobblerListener listener, AudioScrobblerService service, AudioFile file) {
		this.listener = listener;
		this.service = service;
		this.file = file;
	}
	
	public void run() {
		listener.setLastAlbumRetrieved(null);
		listener.setLastArtistRetrieved(null);
		
		AudioScrobblerAlbum album = null;
		ArrayList<AudioScrobblerAlbum> albums = null;
		if (!interrupted) {
			album = service.getAlbum(file.getArtist(), file.getAlbum());
			if (album != null) {
				listener.setAlbum(album);
				Image image = service.getImage(album);
				listener.setImage(image);
				if (image != null)
					listener.savePicture(image, file);
			}
//			listener.setAlbum(album);
//			if (album == null)
//				interrupted = true;
//			else {
//				Image image = service.getImage(album);
//				listener.setImage(image);
//				if (image != null)
//					listener.savePicture(image, file);
//			}
		}
		if (album != null && !interrupted)
			listener.notifyAlbumRetrieved(file);
		
		try {
			Thread.sleep(1000);  // Wait a second to prevent IP banning
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!interrupted) {
			if (!file.getArtist().equals(LanguageTool.getString("UNKNOWN_ARTIST"))) {
				albums = service.getAlbumList(file.getArtist());
			}
			if (albums == null)
				interrupted = true;
			listener.setAlbums(albums);
		}
		
		if (album == null && albums != null && !interrupted) {
			// Try to find an album which fits 
			AudioScrobblerAlbum auxAlbum = null;
			int i = 0;
			while (!interrupted && auxAlbum == null && i < albums.size()) {
				AudioScrobblerAlbum a = albums.get(i);
				StringTokenizer st = new StringTokenizer(a.getTitle(), " ");
				boolean matches = true;
				while (st.hasMoreTokens() && matches) {
					String t = st.nextToken();
					if (forbiddenToken(t))  { // Ignore album if contains forbidden chars
						matches = false;
						break;
					}
					if (!validToken(t)) // Ignore tokens without alphanumerics
						continue;
					if (!file.getAlbum().toLowerCase().contains(t.toLowerCase()))
						matches = false;
				}
				if (matches)
					auxAlbum = a;
				i++;
			}
			if (!interrupted && auxAlbum != null) {
				auxAlbum = service.getAlbum(auxAlbum.getArtist(), auxAlbum.getTitle());
				if (auxAlbum != null) {
					listener.setAlbum(auxAlbum);
					Image image = service.getImage(auxAlbum);
					listener.setImage(image);
					if (image != null)
						listener.savePicture(image, file);
				}
			}
			if (!interrupted && auxAlbum != null)
				listener.notifyAlbumRetrieved(file);
		}
		
	}

	private boolean forbiddenToken(String t) {
		return t.contains("/");
	}
	
	private boolean validToken(String t) {
		return t.matches("[A-Za-z]+");
		//t.contains("(") || t.contains(")")
	}
	
	protected void interrupt() {
		interrupted = true;
	}

}
