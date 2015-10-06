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

package net.sourceforge.atunes.utils;

import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


public class CollectionUtils {

	public static HashMap<AudioFile, AudioFile> vector2HashMap(ArrayList<AudioFile> v) {
		HashMap<AudioFile, AudioFile> result = new HashMap<AudioFile, AudioFile>();
		for (int i = 0; i < v.size(); i++) {
			result.put(v.get(i), v.get(i));
		}
		return result;
	}

}
