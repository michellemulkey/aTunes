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

package net.sourceforge.atunes.model.player;

import java.util.Comparator;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


public class PlayListGenreComparator implements Comparator {

	public static PlayListGenreComparator comparator = new PlayListGenreComparator();
	
	public int compare(Object o1, Object o2) {
		AudioFile f1 = (AudioFile) o1;
		AudioFile f2 = (AudioFile) o2;
		if (f1.getGenre().equals(f2.getGenre())) {
			if (f1.getArtist().equals(f2.getArtist())) {
				if (f1.getAlbum().equals(f2.getAlbum()))
					return f1.getTrackNumber().compareTo(f2.getTrackNumber());
				return f1.getAlbum().compareTo(f2.getAlbum());
			}
			return f1.getArtist().compareTo(f2.getArtist());
		}
		return f1.getGenre().compareTo(f2.getGenre());
	}

}
