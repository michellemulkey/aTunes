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

package net.sourceforge.atunes.kernel.modules.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;


public class RepositoryFavorites implements Serializable {

	private static final long serialVersionUID = -8232826600148930512L;

	private HashMap<String, AudioFile> favoriteSongs;
	private HashMap<String, Album> favoriteAlbums;
	private HashMap<String, Artist> favoriteArtists;
	
	protected RepositoryFavorites() {
		favoriteSongs = new HashMap<String, AudioFile>();
		favoriteAlbums = new HashMap<String, Album>();
		favoriteArtists = new HashMap<String, Artist>();
	}

	public HashMap<String, Album> getFavoriteAlbums() {
		return favoriteAlbums;
	}

	public HashMap<String, Artist> getFavoriteArtists() {
		return favoriteArtists;
	}

	public HashMap<String, AudioFile> getFavoriteSongs() {
		return favoriteSongs;
	}
	
	public ArrayList<AudioFile> getAllFavoriteSongs() {
		ArrayList<AudioFile> result = new ArrayList<AudioFile>();
		for (Iterator<Artist> it = favoriteArtists.values().iterator(); it.hasNext(); ) {
			result.addAll(it.next().getSongs());
		}
		for (Iterator<Album> it = favoriteAlbums.values().iterator(); it.hasNext(); ) {
			result.addAll(it.next().getSongs());
		}
		result.addAll(favoriteSongs.values());
		return result;
	}

	public void setFavoriteAlbums(HashMap<String, Album> favoriteAlbums) {
		this.favoriteAlbums = favoriteAlbums;
	}

	public void setFavoriteArtists(HashMap<String, Artist> favoriteArtists) {
		this.favoriteArtists = favoriteArtists;
	}

	public void setFavoriteSongs(HashMap<String, AudioFile> favoriteSongs) {
		this.favoriteSongs = favoriteSongs;
	}
}
