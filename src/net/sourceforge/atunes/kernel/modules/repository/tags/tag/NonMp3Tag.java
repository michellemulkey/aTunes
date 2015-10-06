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

package net.sourceforge.atunes.kernel.modules.repository.tags.tag;

import java.util.HashMap;

public class NonMp3Tag extends Tag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6200185803652819029L;
	private int trackNumber;
	
	public NonMp3Tag() {
		
	}
	
	public NonMp3Tag(entagged.audioformats.Tag tag) {
		setAlbum(tag.getFirstAlbum());
		setArtist(tag.getFirstArtist());
		setComment(tag.getFirstComment());
		setGenre(getGenre(tag.getFirstGenre()));
		setTitle(tag.getFirstTitle());
		try {
			setTrackNumber(Integer.parseInt(tag.getFirstTrack()));
		} catch (NumberFormatException e) {
			setTrackNumber(-1);
		}
		try {
			setYear(Integer.parseInt(tag.getFirstYear()));
		} catch (NumberFormatException e) {
			setYear(-1);
		}
	}
	
	@Override
	public Tag getTagFromProperties(HashMap properties) {
		NonMp3Tag oggTag = new NonMp3Tag();
		oggTag.setTitle((String)properties.get("TITLE"));
		oggTag.setArtist((String) properties.get("ARTIST"));
		oggTag.setAlbum((String) properties.get("ALBUM"));
		try {
			oggTag.setYear(Integer.parseInt((String) properties.get("YEAR")));
		} catch (NumberFormatException ex) {
			oggTag.setYear(-1);
		}
		oggTag.setComment((String) properties.get("COMMENT"));
		try {
			oggTag.setGenre(Integer.parseInt((String) properties.get("GENRE")));
		} catch (NumberFormatException e) {
			oggTag.setGenre(-1);
		}
		return oggTag;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}
}
