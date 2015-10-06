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

public class AudioScrobblerCoversRunnable implements Runnable {
	
	private AudioScrobblerListener listener;
	private AudioScrobblerService service;
	private ArrayList<AudioScrobblerAlbum> albums;
	
	private boolean interrupted;
	
	public AudioScrobblerCoversRunnable(AudioScrobblerListener listener, AudioScrobblerService service, ArrayList<AudioScrobblerAlbum> albums) {
		this.listener = listener;
		this.service = service;
		this.albums = albums;
	}

	public void run() {
		if (albums != null)
			for (int i = 0; i < albums.size(); i++) {
				Image img = null;
				if (!interrupted) {
					img = service.getSmallImage(albums.get(i));
				}

				if (!interrupted)
					listener.notifyCoverRetrieved(albums.get(i), img);
			}
	}
	
	protected void interrupt() {
		interrupted = true;
	}
}
