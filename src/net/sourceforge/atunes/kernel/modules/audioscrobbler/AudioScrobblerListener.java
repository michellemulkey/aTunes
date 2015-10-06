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

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;

public interface AudioScrobblerListener {

	public void notifyAlbumRetrieved(AudioFile file);
	public void notifyCoverRetrieved(AudioScrobblerAlbum album, Image cover);
	public void notifyArtistImage(Image img);
	public void notifyFinishGetSimilarArtist(AudioScrobblerArtist a, Image img);
	public ArrayList<AudioScrobblerAlbum> getAlbums();
	public void setAlbum(AudioScrobblerAlbum album);
	public void setAlbums(ArrayList<AudioScrobblerAlbum> album);
	public void setImage(Image img);
	public void savePicture(Image img, AudioFile file);
	public void setLastAlbumRetrieved(String album);
	public void setLastArtistRetrieved(String artist);
}
