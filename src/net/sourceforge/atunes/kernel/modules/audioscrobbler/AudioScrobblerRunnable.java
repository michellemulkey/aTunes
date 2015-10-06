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

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;

public class AudioScrobblerRunnable implements Runnable {

	private boolean interrupted;
	private AudioScrobblerAlbumsRunnable albumsRunnable;
	private AudioScrobblerCoversRunnable coversRunnable;
	private AudioScrobblerSimilarArtistsRunnable artistsRunnable;
	
	private AudioScrobblerListener listener;
	private AudioScrobblerService service;
	private AudioFile file;
	
	public AudioScrobblerRunnable(AudioScrobblerListener listener, AudioScrobblerService service, AudioFile file) {
		this.listener = listener;
		this.service = service;
		this.file = file;
	}

	public void run() {
		albumsRunnable = new AudioScrobblerAlbumsRunnable(listener, service, file);
		Thread albumsInfoThread = new Thread(albumsRunnable);
		albumsInfoThread.start();
		try {
			albumsInfoThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		coversRunnable = new AudioScrobblerCoversRunnable(listener, service, listener.getAlbums());
		Thread coversThread = new Thread(coversRunnable);
		coversThread.start();

		artistsRunnable = new AudioScrobblerSimilarArtistsRunnable(listener, service, file.getArtist());
		Thread artistsThread = new Thread(artistsRunnable);
		artistsThread.start();

		try {
			if (!interrupted) coversThread.join();
			if (!interrupted) artistsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void interrupt() {
		interrupted = true;
		if (albumsRunnable != null)
			albumsRunnable.interrupt();
		if (coversRunnable != null)
			coversRunnable.interrupt();
		if (artistsRunnable != null)
			artistsRunnable.interrupt();
	}
}
