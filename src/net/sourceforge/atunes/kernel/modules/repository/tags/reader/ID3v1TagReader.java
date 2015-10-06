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

package net.sourceforge.atunes.kernel.modules.repository.tags.reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v1Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;



/**
 * @author fleax
 *
 */
public class ID3v1TagReader extends TagReader {

	protected Tag retrieveTag(AudioFile file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(stream);
		long fileLength = file.length();
		byte[] tagBytes = new byte[128];
		bis.skip(fileLength - 128);
		bis.read(tagBytes);
		ID3v1Tag result = null;
		String tagHeader = new String(tagBytes, 0, 3);
		if (tagHeader.equalsIgnoreCase("TAG")) {
			ID3v1Tag tag = new ID3v1Tag();
			
			String title = getField(tagBytes, 3, 30);
			String artist = getField(tagBytes, 33, 30);
			String album = getField(tagBytes, 63, 30);
			int year = 0;
			try {
				year = Integer.valueOf(getField(tagBytes, 93, 4)).intValue();
			} catch (NumberFormatException e) {
			}
			
			String comment = getField(tagBytes, 97, 30);
			int genre = tagBytes[127];
			
			tag.setTitle(title);
			tag.setArtist(artist);
			tag.setAlbum(album);
			tag.setYear(year);
			tag.setComment(comment);
			tag.setGenre(genre);
			
			result = tag;
		}
		bis.close();
		return result;
	}
	
	protected Tag retrieveTagAtEnd() {
		return null;
	}
}
