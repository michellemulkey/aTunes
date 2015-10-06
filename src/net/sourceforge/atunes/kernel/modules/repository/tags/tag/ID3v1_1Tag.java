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

/**
 * @author fleax
 *
 */
public class ID3v1_1Tag extends ID3v1Tag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -151197606616707234L;
	private int trackNumber;
	
	public ID3v1_1Tag() {
		super();
	}
	
	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}
	
	public Tag getTagFromProperties(HashMap properties) {
		ID3v1Tag id3v1Tag = (ID3v1Tag) super.getTagFromProperties(properties);
		
		ID3v1_1Tag id3Tag = new ID3v1_1Tag();
		id3Tag.setAlbum(id3v1Tag.getAlbum());
		id3Tag.setArtist(id3v1Tag.getArtist());
		id3Tag.setComment(id3v1Tag.getComment());
		id3Tag.setGenre(id3v1Tag.getGenre());
		id3Tag.setTitle(id3v1Tag.getTitle());
		id3Tag.setYear(id3v1Tag.getYear());
		try {
			id3Tag.setTrackNumber(Integer.parseInt((String)properties.get("TRACK")));	
		} catch (NumberFormatException e) {
		}
		return id3Tag;
	}
}
