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

package net.sourceforge.atunes.kernel.modules.cdripper;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.Cdda2wav;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.NoCdListener;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.model.CDInfo;
import net.sourceforge.atunes.kernel.modules.cdripper.encoders.Encoder;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;


public class CdRipper {

	private Logger logger = Logger.getLogger(CdRipper.class);
	
	private Cdda2wav cddawav;
	private Encoder encoder;
	private ProgressListener listener;
	
	private boolean interrupted;
	
	private String artist;
	private String album;
	
	private String fileNamePattern;
	
	public static final String ARTIST_PATTERN = "%A";
	public static final String ALBUM_PATTERN = "%L";
	public static final String TRACK_TITLE_AND_NUMBER = "%TN";
	
	public CdRipper() {
		cddawav = new Cdda2wav();
	}
	
	public CDInfo getCDInfo() {
		return cddawav.getCDInfo();
	}
	
	public boolean ripTracks(ArrayList<Integer> tracks, ArrayList<String> titles, File folder) {
		String extension = encoder != null ? encoder.getExtensionOfEncodedFiles() : "wav";
		logger.info("Running cd ripping of " + tracks.size() + " to " + extension + "...");
		long t0 = System.currentTimeMillis();
		if (!checkFolder(folder)) {
			logger.error("Folder " + folder + " not found or not a directory");
			return false;
		}
		
		if (listener != null)
			listener.notifyProgress(0);
		
		File wavFile = null;
		File resultFile = null;
		for (int i = 0; i < tracks.size(); i++) {
			if (!interrupted) {
				int trackNumber = tracks.get(i);
				wavFile = new File(folder.getAbsolutePath() + "/track" + trackNumber + ".wav");
				if (encoder != null)
					resultFile = new File(folder.getAbsolutePath() + '/' + getFileName(titles, trackNumber, extension));
				
				boolean ripResult = false;
				if (!interrupted)
					ripResult = cddawav.cdda2wav(trackNumber, wavFile);
				
				if (!interrupted && ripResult && encoder != null) {
					boolean encodeResult = encoder.encode(wavFile, resultFile, getStringFormatted(titles != null && titles.size() >= trackNumber ? titles.get(trackNumber-1) : null), trackNumber);
					if (encodeResult && listener != null && !interrupted)
						listener.notifyFileFinished(resultFile);
					
					logger.info("Deleting wav file...");
					wavFile.delete();
					
					if (interrupted && resultFile != null)
						resultFile.delete();
				}
				else if (interrupted)
					wavFile.delete();
				else if (!ripResult)
					logger.error("Rip failed. Skipping track " + trackNumber + "...");
				
				if (listener != null)
					listener.notifyProgress(i+1);
			}
		}
		long t1 = System.currentTimeMillis();
		logger.info("Process finished in " + (t1 - t0) / 1000.0 + " seconds");
		return true;
	}
	
	private String getFileName(ArrayList<String> titles, int trackNumber, String extension) {
		DecimalFormat df = new DecimalFormat("00");
		if (fileNamePattern == null)
			return "track" + trackNumber + '.' + extension;
		String result = fileNamePattern + '.' + extension;
		result = result.replaceAll(ARTIST_PATTERN, getStringFormatted(artist));
		result = result.replaceAll(ALBUM_PATTERN, getStringFormatted(album));
		result = result.replaceAll(TRACK_TITLE_AND_NUMBER, df.format(trackNumber) + (titles != null && titles.size() >= trackNumber ? " - " + getStringFormatted(titles.get(trackNumber-1)) : ""));
		return result;
	}
	
	private static String getStringFormatted(String s) {
		return s != null ? s.replaceAll("'","") : null;
	}
	
	private boolean checkFolder(File folder) {
		boolean result = folder.exists() && folder.isDirectory();
		return result;
	}
	
	public void setDecoderListener(ProgressListener listener) {
		cddawav.setListener(listener);
	}
	
	public void setEncoderListener(ProgressListener listener) {
		if (encoder != null)
			encoder.setListener(listener);
	}
	
	public void setTotalProgressListener(ProgressListener listener) {
		this.listener = listener;
	}
	
	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	public void stop() {
		interrupted = true;
		cddawav.stop();
		if (encoder != null)
			encoder.stop();
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public void setAlbum(String album) {
		if (album == null || album.equals(""))
			this.album = LanguageTool.getString("UNKNOWN_ALBUM");
		else
			this.album = getStringFormatted(album);
		if (encoder != null)
			encoder.setAlbum(this.album);
	}

	public void setArtist(String artist) {
		if (artist == null || artist.equals(""))
			this.artist = LanguageTool.getString("UNKNOWN_ARTIST");
		else
			this.artist = getStringFormatted(artist);
		if (encoder != null)
			encoder.setArtist(this.artist);
	}

	public void setNoCdListener(NoCdListener listener) {
		cddawav.setNoCdListener(listener);
	}
}
