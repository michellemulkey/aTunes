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

package net.sourceforge.atunes.kernel.modules.repository.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.modules.repository.tags.reader.TagDetector;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v1_1Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v2Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.NonMp3Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;

import entagged.audioformats.AudioFileIO;


/**
 * @author fleax
 *
 */
public class AudioFile extends File {
	
	private static Logger logger = Logger.getLogger(AudioFile.class); 
	
	private static final long serialVersionUID = 7907490762633634241L;

	public static final String MP3_FORMAT = "mp3";
	public static final String OGG_FORMAT = "ogg";
	public static final String MP4_FORMAT_1 = "m4a";
	public static final String MP4_FORMAT_2 = "mp4";
	public static final String MP4_FORMAT_3 = "aac";
	public static final String WAV_FORMAT = "wav";
	public static final String WMA_FORMAT = "wma";
	
	private Tag tag;
	private ArrayList externalPictures;
	
	private long duration;
	private int bitrate;
	private int frequency;
	
	public AudioFile(String fileName) {
		super(fileName);
		introspectTags();
		readAudioProperties(this);
	}
	
	private void introspectTags() {
		TagDetector.getTags(this);
	}
	
	public int getExternalPicturesCount() {
		return externalPictures != null ? externalPictures.size() : 0;
	}

	public void setExternalPictures(ArrayList externalPictures) {
		this.externalPictures = externalPictures;
	}
	
	public void addExternalPicture(File picture) {
		if (externalPictures != null && !externalPictures.contains(picture))
			externalPictures.add(0, picture);
	}
	
	public void refreshTag() {
		deleteTags();
		introspectTags();
	}
	
	public String toString() {
		return getName();
	}
	
	private void deleteTags() {
		tag = null;
	}
	
	public String getNameWithoutExtension() {
		if (getName().indexOf('.') != -1)
			return getName().substring(0, getName().lastIndexOf('.'));
		return getName();
	}
	
	public String getTitleOrFileName() {
		String title;
		if (tag != null) {
			if (tag.getTitle() == null || tag.getTitle().equals(""))
				title = getNameWithoutExtension();
			else
				title = tag.getTitle();
		}
		else
			title = getNameWithoutExtension();
		return title;
	}

	public String getTitle() {
		String title;
		if (tag != null) {
			if (tag.getTitle() == null || tag.getTitle().equals(""))
				title = "";
			else
				title = tag.getTitle();
		}
		else
			title = "";
		return title;
	}
	
	public String getArtist() {
		String artist;
		if (tag != null) {
			if (tag.getArtist() == null || tag.getArtist().equals(""))
				artist = LanguageTool.getString("UNKNOWN_ARTIST");
			else
				artist = tag.getArtist();
		}
		else
			artist = LanguageTool.getString("UNKNOWN_ARTIST");
		return artist;
	}
	
	public String getAlbum() {
		String album;
		if (tag != null) {
			if (tag.getAlbum() == null || tag.getAlbum().equals(""))
				album = LanguageTool.getString("UNKNOWN_ALBUM");
			else
				album = tag.getAlbum();
		}
		else
			album = LanguageTool.getString("UNKNOWN_ALBUM");
		return album;
	}
	
	public Integer getTrackNumber() {
		if (tag != null) {
			if (tag instanceof ID3v2Tag)
				return ((ID3v2Tag)tag).getTrackNumber();
			else if (tag instanceof ID3v1_1Tag)
				return ((ID3v1_1Tag)tag).getTrackNumber();
			else if (tag instanceof NonMp3Tag)
				return ((NonMp3Tag)tag).getTrackNumber();
			else
				return 0;
		}
		return 0;
	}
	
	public String getGenre() {
		if (tag != null && tag.getGenre() != null) {
			return tag.getGenre();
		}
		return "";
	}
	
	public final boolean hasInternalPicture() {
		return (tag instanceof ID3v2Tag) && ((ID3v2Tag)tag).getPictureBegin() != 0 && ((ID3v2Tag)tag).getPictureLength() != 0;
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public static boolean isValidAudioFile(File file) {
		return isMp3File(file) || isOggFile(file) || isMp4File(file) || isWavFile(file) || isWmaFile(file);
	}
	
	public static boolean isMp3File(File file) {
		return file.getAbsolutePath().toLowerCase().endsWith(MP3_FORMAT);
	}
	
	public static boolean isOggFile(File file) {
		return file.getAbsolutePath().toLowerCase().endsWith(OGG_FORMAT);
	}
	
	public static boolean isMp4File(File file) {
		return file.getAbsolutePath().toLowerCase().endsWith(MP4_FORMAT_1) || 
			   file.getAbsolutePath().toLowerCase().endsWith(MP4_FORMAT_2) ||
			   file.getAbsolutePath().toLowerCase().endsWith(MP4_FORMAT_3);
	}
	
	public static boolean isWavFile(File file) {
		return file.getAbsolutePath().toLowerCase().endsWith(WAV_FORMAT);
	}
	
	public static boolean isWmaFile(File file) {
		return file.getAbsolutePath().toLowerCase().endsWith(WMA_FORMAT);
	}
	
	
	public static Tag getNewTag(AudioFile file, HashMap properties) {
		if (isMp3File(file))
			return new ID3v2Tag().getTagFromProperties(properties);
		return new NonMp3Tag().getTagFromProperties(properties);
	}

	public ArrayList getExternalPictures() {
		return externalPictures;
	}
		
	private static void readAudioProperties(AudioFile file) {
		// Mp4 audio properties not supported
		if (isMp4File(file))
			return;
		
		try {
			entagged.audioformats.AudioFile f = AudioFileIO.read(file);
			file.duration = f.getLength();
			file.bitrate = f.getBitrate();
			file.frequency = f.getSamplingRate();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public long getDuration() {
		return duration;
	}

	public int getBitrate() {
		return bitrate;
	}

	public int getFrequency() {
		return frequency;
	}
}
