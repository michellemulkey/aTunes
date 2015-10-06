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

package net.sourceforge.atunes.kernel.modules.repository.tags.writer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.handlers.ApplicationDataHandler;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v2Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.NonMp3Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;
import net.sourceforge.atunes.kernel.utils.AudioFilePictureUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagConstant;
import org.farng.mp3.TagOptionSingleton;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.AbstractID3v2Frame;
import org.farng.mp3.id3.AbstractID3v2FrameBody;
import org.farng.mp3.id3.FrameBodyAPIC;
import org.farng.mp3.id3.FrameBodyCOMM;
import org.farng.mp3.id3.FrameBodyTALB;
import org.farng.mp3.id3.FrameBodyTCON;
import org.farng.mp3.id3.FrameBodyTIT2;
import org.farng.mp3.id3.FrameBodyTPE1;
import org.farng.mp3.id3.FrameBodyTRCK;
import org.farng.mp3.id3.FrameBodyTYER;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v2_4;
import org.farng.mp3.id3.ID3v2_4Frame;

import entagged.audioformats.AudioFileIO;

public class TagModifier {
	
	private static Logger logger = Logger.getLogger(TagModifier.class);

	public static void setInfo(AudioFile file, Tag tag, File imageFile, boolean writePicture) {
		deleteTags(file);

		if (AudioFile.isMp3File(file)) {
			saveMp3Tag(file, tag, writePicture, imageFile);
		}
		else if (!AudioFile.isMp4File(file)){
			saveNonMp3Tag(file, tag);
		}
	}
	
	public static void setTitles(AudioFile file, String newTitle) {
		if (AudioFile.isMp3File(file)) {
			saveMp3Title(file, newTitle);
		}
		else if (!AudioFile.isMp4File(file)){
			saveNonMp3Title(file, newTitle);
		}
	}
	
	public static void setTrackNumber(AudioFile file, Integer track) {
		if (AudioFile.isMp3File(file)) {
			setMp3TrackNumber(file, track);
		}
		else if (!AudioFile.isMp4File(file)){
			setNonMp3TrackNumber(file, track);
		}
	}
	
	public static void setGenre(AudioFile file, String genre) {
		if (AudioFile.isMp3File(file)) {
			setMp3Genre(file, genre);
		}
		else if (!AudioFile.isMp4File(file)){
			setNonMp3Genre(file, genre);
		}
	}
	
	public static void deleteTags(AudioFile file) {
		if (AudioFile.isMp3File(file)) {
			try {
				MP3File mp3File = new MP3File(file);
				ID3v1 tag1 = mp3File.getID3v1Tag();
				AbstractID3v2 tag2 = mp3File.getID3v2Tag();
				if (tag1 != null)
					mp3File.delete(tag1);
				if (tag2 != null)
					mp3File.delete(tag2);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.debug(e);
			}
		}
		else if (!AudioFile.isMp4File(file)){
			try {
				AudioFileIO.delete(AudioFileIO.read(file));
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	
	private static void saveMp3Tag(AudioFile file, Tag tag, boolean writePicture, File imageFile) {
		String title = tag.getTitle() != null ? tag.getTitle() : "";
		String album = tag.getAlbum() != null ? tag.getAlbum() : "";
		String artist = tag.getArtist() != null ? tag.getArtist() : ""; 
		String year = Integer.toString(tag.getYear());
		String comment = tag.getComment() != null ? tag.getComment() : "";
		//int genre = tag.getGenre();
		String genreString = tag.getGenre();
		String trackNumber = null;
		if (tag instanceof ID3v2Tag)
			trackNumber = Integer.toString(((ID3v2Tag)tag).getTrackNumber());

		// ID3v2 tag
		ID3v2_4 tag2 = new ID3v2_4();
		AbstractID3v2Frame frame;
		AbstractID3v2FrameBody frameBody;
		
		frameBody = new FrameBodyTIT2((byte) 0, title);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTALB((byte) 0, album);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTPE1((byte) 0, artist);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTYER((byte) 0, year);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTRCK((byte) 0, trackNumber);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTCON((byte) 0, genreString);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyCOMM((byte) 0, "", "", comment);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		try {
			if (writePicture) {
				if (imageFile != null) {
					FileInputStream stream = new FileInputStream(imageFile);
					long fileLength = imageFile.length();
					byte[] data = new byte[(int) fileLength];
					stream.read(data);
					frameBody = new FrameBodyAPIC((byte) 0, "image/" + imageFile.getAbsolutePath().substring(imageFile.getAbsolutePath().length() - 3), (byte) 0, "", data);
					frame = new ID3v2_4Frame(frameBody);
					tag2.setFrame(frame);
				}
				else {
					BufferedImage img = AudioFilePictureUtils.getInsidePictureAsBuffer(file, -1,-1); 
					if (img != null) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(img, "jpeg", baos);
						byte[] data = baos.toByteArray();
						frameBody = new FrameBodyAPIC((byte) 0, "image/jpeg", (byte) 0, "", data);
						frame = new ID3v2_4Frame(frameBody);
						tag2.setFrame(frame);
					}
				}
			}
			
			// Write tags to file
			MP3File mp3File = new MP3File();
			TagOptionSingleton.getInstance().setOriginalSavedAfterAdjustingID3v2Padding(false);
			TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);
			//mp3File.setID3v1Tag(tag1_1);
			mp3File.setID3v2Tag(tag2);
			mp3File.save(file);
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}
	}
	
	private static void saveNonMp3Tag(AudioFile file, Tag tag) {
		String title = tag.getTitle() != null ? tag.getTitle() : "";
		String album = tag.getAlbum() != null ? tag.getAlbum() : "";
		String artist = tag.getArtist() != null ? tag.getArtist() : ""; 
		String year = Integer.toString(tag.getYear());
		String comment = tag.getComment() != null ? tag.getComment() : "";
		String genreString = tag.getGenre();
		int track = ((NonMp3Tag)tag).getTrackNumber();
		
		try {
			entagged.audioformats.AudioFile audioFile = AudioFileIO.read(file);
			audioFile.getTag().setAlbum(album);
			audioFile.getTag().setArtist(artist);
			audioFile.getTag().setComment(comment);
			audioFile.getTag().setGenre(genreString);
			audioFile.getTag().setTitle(title);
			audioFile.getTag().setYear(year);
			audioFile.getTag().setTrack(Integer.toString(track));
			audioFile.commit();
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}
	}
	
	private static void saveMp3Title(AudioFile file, String newTitle) {
		Tag tag = file.getTag();
		
		String title = newTitle;
		String album = tag.getAlbum() != null ? tag.getAlbum() : "";
		String artist = tag.getArtist() != null ? tag.getArtist() : ""; 
		String year = Integer.toString(tag.getYear());
		String comment = tag.getComment() != null ? tag.getComment() : "";
		//int genre = tag.getGenre();
		String genreString = tag.getGenre();
		String trackNumber = null;
		if (tag instanceof ID3v2Tag)
			trackNumber = Integer.toString(((ID3v2Tag)tag).getTrackNumber());
		
		// ID3v2 tag
		ID3v2_4 tag2 = new ID3v2_4();
		AbstractID3v2Frame frame;
		AbstractID3v2FrameBody frameBody;
		
		frameBody = new FrameBodyTIT2((byte) 0, title);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTALB((byte) 0, album);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTPE1((byte) 0, artist);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTYER((byte) 0, year);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTRCK((byte) 0, trackNumber);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTCON((byte) 0, genreString);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyCOMM((byte) 0, "", "", comment);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		try {
			BufferedImage img = AudioFilePictureUtils.getInsidePictureAsBuffer(file, -1,-1); 
			if (img != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "jpeg", baos);
				byte[] data = baos.toByteArray();
				frameBody = new FrameBodyAPIC((byte) 0, "image/jpeg", (byte) 0, "", data);
				frame = new ID3v2_4Frame(frameBody);
				tag2.setFrame(frame);
			}
			
			deleteTags(file);
			
			// Write tags to file
			MP3File mp3File = new MP3File();
			TagOptionSingleton.getInstance().setOriginalSavedAfterAdjustingID3v2Padding(false);
			TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);
			mp3File.setID3v2Tag(tag2);
			mp3File.save(file);
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}

	}

	private static void setMp3TrackNumber(AudioFile file, Integer track) {
		Tag tag = file.getTag();
		if (tag == null)
			tag = new ID3v2Tag();
		
		String title = tag.getTitle() != null ? tag.getTitle() : "";
		String album = tag.getAlbum() != null ? tag.getAlbum() : "";
		String artist = tag.getArtist() != null ? tag.getArtist() : ""; 
		String year = Integer.toString(tag.getYear());
		String comment = tag.getComment() != null ? tag.getComment() : "";
		//int genre = tag.getGenre();
		String genreString = tag.getGenre();
		String trackNumber = track.toString();
		
		// ID3v2 tag
		ID3v2_4 tag2 = new ID3v2_4();
		AbstractID3v2Frame frame;
		AbstractID3v2FrameBody frameBody;
		
		frameBody = new FrameBodyTIT2((byte) 0, title);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTALB((byte) 0, album);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTPE1((byte) 0, artist);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTYER((byte) 0, year);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTRCK((byte) 0, trackNumber);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTCON((byte) 0, genreString);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyCOMM((byte) 0, "", "", comment);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		try {
			BufferedImage img = AudioFilePictureUtils.getInsidePictureAsBuffer(file, -1,-1); 
			if (img != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "jpeg", baos);
				byte[] data = baos.toByteArray();
				frameBody = new FrameBodyAPIC((byte) 0, "image/jpeg", (byte) 0, "", data);
				frame = new ID3v2_4Frame(frameBody);
				tag2.setFrame(frame);
			}
			
			deleteTags(file);
			
			// Write tags to file
			MP3File mp3File = new MP3File();
			TagOptionSingleton.getInstance().setOriginalSavedAfterAdjustingID3v2Padding(false);
			TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);
			mp3File.setID3v2Tag(tag2);
			mp3File.save(file);
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}

	}

	private static void setMp3Genre(AudioFile file, String genre) {
		Tag tag = file.getTag();
		if (tag == null)
			tag = new ID3v2Tag();
		
		String title = tag.getTitle() != null ? tag.getTitle() : "";
		String album = tag.getAlbum() != null ? tag.getAlbum() : "";
		String artist = tag.getArtist() != null ? tag.getArtist() : ""; 
		String year = Integer.toString(tag.getYear());
		String comment = tag.getComment() != null ? tag.getComment() : "";
		//int genre = tag.getGenre();
		String genreString = genre;
		String trackNumber = null;
		if (tag instanceof ID3v2Tag)
			trackNumber = Integer.toString(((ID3v2Tag)tag).getTrackNumber());
		
		// ID3v2 tag
		ID3v2_4 tag2 = new ID3v2_4();
		AbstractID3v2Frame frame;
		AbstractID3v2FrameBody frameBody;
		
		frameBody = new FrameBodyTIT2((byte) 0, title);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTALB((byte) 0, album);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTPE1((byte) 0, artist);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTYER((byte) 0, year);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTRCK((byte) 0, trackNumber);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyTCON((byte) 0, genreString);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		frameBody = new FrameBodyCOMM((byte) 0, "", "", comment);
		frame = new ID3v2_4Frame(frameBody);
		tag2.setFrame(frame);
		try {
			BufferedImage img = AudioFilePictureUtils.getInsidePictureAsBuffer(file, -1,-1); 
			if (img != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "jpeg", baos);
				byte[] data = baos.toByteArray();
				frameBody = new FrameBodyAPIC((byte) 0, "image/jpeg", (byte) 0, "", data);
				frame = new ID3v2_4Frame(frameBody);
				tag2.setFrame(frame);
			}
			
			deleteTags(file);
			
			// Write tags to file
			MP3File mp3File = new MP3File();
			TagOptionSingleton.getInstance().setOriginalSavedAfterAdjustingID3v2Padding(false);
			TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);
			mp3File.setID3v2Tag(tag2);
			mp3File.save(file);
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}

	}

	private static void saveNonMp3Title(AudioFile file, String newTitle) {
		try {
			entagged.audioformats.AudioFile audioFile = AudioFileIO.read(file);
			audioFile.getTag().setTitle(newTitle);
			audioFile.commit();
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}
	}

	private static void setNonMp3TrackNumber(AudioFile file, Integer track) {
		try {
			entagged.audioformats.AudioFile audioFile = AudioFileIO.read(file);
			audioFile.getTag().setTrack(track.toString());
			audioFile.commit();
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}
	}
	
	private static void setNonMp3Genre(AudioFile file, String genre) {
		try {
			entagged.audioformats.AudioFile audioFile = AudioFileIO.read(file);
			audioFile.getTag().setGenre(genre);
			audioFile.commit();
		} catch (Exception e) {
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_EDIT_TAG"));
			logger.error("Could not edit tag");
			logger.debug(e);
		}
	}
	
	public static void refreshAfterTagModify(final ArrayList audioFilesEditing) {
		boolean playListContainsRefreshedFile = false;
		for (int i = 0; i < audioFilesEditing.size(); i++) {
			HandlerProxy.getRepositoryHandler().refreshFile((AudioFile)audioFilesEditing.get(i));
			
			if (HandlerProxy.getPlayerHandler().getCurrentPlayList().contains(audioFilesEditing.get(i))) {
				playListContainsRefreshedFile = true;
				for (int j = 0; j < HandlerProxy.getPlayerHandler().getCurrentPlayList().size(); j++) {
					if (HandlerProxy.getPlayerHandler().getCurrentPlayList().get(j).equals(audioFilesEditing.get(i)))
						HandlerProxy.getControllerHandler().getPlayListController().updatePositionInTable(j);
				}
			}
			
			if (HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile() != null && 
					HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile().equals(audioFilesEditing.get(i))) {
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged((AudioFile)audioFilesEditing.get(i));
			}
		}
		HandlerProxy.getControllerHandler().getNavigationController().notifyReload();
		if (playListContainsRefreshedFile)
			ApplicationDataHandler.persistPlayList(HandlerProxy.getPlayerHandler().getCurrentPlayList());
	}

}
