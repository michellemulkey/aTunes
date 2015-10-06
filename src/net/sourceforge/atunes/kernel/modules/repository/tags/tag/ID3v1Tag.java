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
public class ID3v1Tag extends Tag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7448694019362396607L;

	public ID3v1Tag() {
		super();
	}
	
	public Tag getTagFromProperties(HashMap properties) {
		ID3v1Tag id3Tag = new ID3v1Tag();
		id3Tag.setTitle((String)properties.get("TITLE"));
		id3Tag.setArtist((String)properties.get("ARTIST"));
		id3Tag.setAlbum((String)properties.get("ALBUM"));
		try {
			id3Tag.setYear(Integer.parseInt((String)properties.get("YEAR")));
		} catch (NumberFormatException ex) {
			id3Tag.setYear(-1);
		}
		id3Tag.setComment((String)properties.get("COMMENT"));
		try {
			id3Tag.setGenre(Integer.parseInt((String)properties.get("GENRE")));
		} catch (NumberFormatException ex) {
			id3Tag.setGenre(-1);
		}
		return id3Tag;
	}
}
