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

package net.sourceforge.atunes.model.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


/**
 * @author fleax
 *
 */
public class Artist implements Serializable, TreeObject {

	private static final long serialVersionUID = -7981636660798555640L;

	private String name;
	private HashMap<String, Album> albums;
	
	public Artist(String name) {
		this.name = name;
		albums = new HashMap<String, Album>();
	}
	
	public void addAlbum(Album album) {
		albums.put(album.getName(), album);
	}
	
	public Album getAlbum(String albumName) {
		return albums.get(albumName);
	}
	
	public ArrayList<AudioFile> getSongs() {
		ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
		for (Iterator<String> it = albums.keySet().iterator(); it.hasNext(); ) {
			Album album = albums.get(it.next());
			songs.addAll(album.getSongs());
		}
		return songs;
	}
	
	public HashMap<String, Album> getAlbums() {
		return albums;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public void removeAlbum(Album alb) {
		albums.remove(alb.getName());
	}
}
