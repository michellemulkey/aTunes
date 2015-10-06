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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;


/**
 * @author fleax
 *
 */
public abstract class TagReader {
	
	public static final String CHARSET = "ISO-8859-1";
	
	public Tag readFile(AudioFile file) throws IOException {
		return retrieveTag(file);
	}
	
	protected final String getField(byte[] array, int pos, int length) {
		String field;
		try {
			for (int i = pos; i < pos + length; i++)
				if (array[i] == 0)
					array[i] = 32;
			field = new String(array, pos, length, CHARSET);
		}
		catch (UnsupportedEncodingException e) {
			field = new String(array, pos, length);
		}
		field.trim();
		return field;
	}
	
	protected final int get4ByteSynchSafeInteger(byte[] array) {
		return ((array[0] & 0x7F) << 21) + ((array[1] & 0x7F) << 14) + ((array[2] & 0x7F) << 7) + (array[3] & 0x7F);  
	}
	
	protected final int get4BytesInteger(byte[] array) {
		return ((array[0] & 0xFF) << 24) + ((array[1] & 0xFF) << 16) + ((array[2] & 0xFF) << 8) + (array[3] & 0xFF);
	}

	protected final int get3ByteSynchSafeInteger(byte[] array) {
		return ((array[0] & 0x7F) << 14) + ((array[1] & 0x7F) << 7) + (array[2] & 0x7F);  
	}
	
	protected final int get3BytesInteger(byte[] array) {
		return ((array[0] & 0xFF) << 16) + ((array[1] & 0xFF) << 8) + (array[2] & 0xFF);
	}
	
	protected abstract Tag retrieveTag(AudioFile file) throws IOException;
	
}
