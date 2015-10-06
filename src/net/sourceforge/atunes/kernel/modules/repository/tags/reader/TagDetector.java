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

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v1Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v1_1Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v2Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.NonMp3Tag;

import org.apache.log4j.Logger;

import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;

/**
 * @author fleax
 *
 */
public class TagDetector {

	private static Logger logger = Logger.getLogger(TagDetector.class);
	
	private static ID3v2TagReader v2Reader = new ID3v2TagReader();
	private static ID3v1_1TagReader v1_1Reader = new ID3v1_1TagReader();
	private static ID3v1TagReader v1Reader = new ID3v1TagReader();
	
	public static void getTags(AudioFile file) {
		// Mp4 tag not supported
		if (AudioFile.isMp4File(file))
			return;
		
		try {
			if (AudioFile.isMp3File(file)) { // MP3
				ID3v2Tag v2Tag = (ID3v2Tag) v2Reader.readFile(file);
				if (v2Tag != null)
					file.setTag(v2Tag);
				else {
					ID3v1_1Tag v1_1Tag = (ID3v1_1Tag) v1_1Reader.readFile(file);
					if (v1_1Tag != null)
						file.setTag(v1_1Tag);
					else {
						ID3v1Tag v1Tag = (ID3v1Tag) v1Reader.readFile(file);
						if (v1Tag != null)
							file.setTag(v1Tag);
					}
				}
			}
			else { // Non Mp3
				entagged.audioformats.AudioFile f = AudioFileIO.read(file);
				Tag t = f.getTag();
				NonMp3Tag tag = new NonMp3Tag(t);
				file.setTag(tag);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.debug(e);
		}
	}
}
