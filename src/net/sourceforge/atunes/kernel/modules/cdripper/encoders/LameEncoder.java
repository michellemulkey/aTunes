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

package net.sourceforge.atunes.kernel.modules.cdripper.encoders;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;


import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.kernel.modules.cdripper.ProgressListener;
import net.sourceforge.atunes.kernel.utils.SystemProperties;

import org.apache.log4j.Logger;


public class LameEncoder implements Encoder {

	public static final String LAME = "lame";
	public static final String TITLE = "--tt";
	public static final String ARTIST = "--ta";
	public static final String ALBUM = "--tl";
	public static final String TRACK = "--tn";
	public static final String ID3v2 = "--id3v2-only";
	public static final String VERSION = "--version";
	public static final String QUALITY = "-b";
	
	private Process p;
	
	private Logger logger = Logger.getLogger(LameEncoder.class);
	
	private ProgressListener listener;
	
	private String artist;
	private String album;
	
	private String quality;
	
	public static boolean testTool() {
		if (SystemProperties.hostIsWindows())
			return true;
		
		try {
			Process p = Runtime.getRuntime().exec(new String[] {LAME, VERSION});
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			while (stdInput.readLine() != null) {
			}

			int code = p.waitFor();
			if (code != 0) {
				return false;
			}			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean encode(File wavFile, File mp3File, String title, int trackNumber) {
		logger.info("Mp3 encoding process started... " + wavFile.getName() + " -> " + mp3File.getName());
		try {
			ArrayList<String> command = new ArrayList<String>();
			if (SystemProperties.hostIsWindows())
				command.add(Constants.WINDOWS_TOOLS_DIR + SystemProperties.fileSeparator + LAME);
			else
				command.add(LAME);
			command.add(wavFile.getAbsolutePath());
			command.add(mp3File.getAbsolutePath());
			if (title != null && !title.equals("")) {
				command.add(TITLE);
				command.add(title);
			}
			if (artist != null && !artist.equals("")) {
				command.add(ARTIST);
				command.add(artist);
			}
			if (album != null && !album.equals("")) {
				command.add(ALBUM);
				command.add(album);
			}
			command.add(TRACK);
			command.add(Integer.toString(trackNumber));
			command.add(ID3v2);
			command.add(QUALITY);
			command.add(quality);
			p = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String s = null;
			int percent = -1;
			while ((s = stdInput.readLine()) != null) {
				if (listener != null) {
					if (s.matches(".*\\(..%\\).*")) {
						int aux = Integer.parseInt((s.substring(s.indexOf('(')+1, s.indexOf('%'))).trim());
						if (aux != percent) {
							percent = aux;
							listener.notifyProgress(percent);
						}
					}
					else if (s.matches(".*\\(100%\\).*")) {
						if (percent != 100) {
							percent = 100;
							listener.notifyProgress(100);
						}
					}
				}
			}
			
			int code = p.waitFor();
			if (code != 0) {
				logger.error("Process returned code " + code);
				return false;
			}			
			
			logger.info("Encoded ok!!");
			return true;
		} catch (Exception e) {
			logger.error("Process execution caused exception " + e);
			return false;
		}
	}
	
	public String getExtensionOfEncodedFiles() {
		return "mp3";
	}
	
	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	public void stop() {
		p.destroy();
	}
}
