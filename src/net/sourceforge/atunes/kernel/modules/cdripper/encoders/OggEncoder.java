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


public class OggEncoder implements Encoder {

	public static final String OGGENC = "oggenc";
	public static final String OUTPUT = "-o";
	public static final String TITLE = "-t";
	public static final String ARTIST = "-a";
	public static final String ALBUM = "-l";
	public static final String TRACK = "-N";
	public static final String QUALITY = "-q";
	public static final String VERSION = "--version";
	
	private Process p;
	
	private Logger logger = Logger.getLogger(OggEncoder.class);
	
	private ProgressListener listener;
	
	private String artist;
	private String album;
	
	private String quality;
	
	public static boolean testTool() {
		if (SystemProperties.hostIsWindows())
			return true;
		
		try {
			Process p = Runtime.getRuntime().exec(new String[] {OGGENC, VERSION});
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
	
	public boolean encode(File wavFile, File oggFile, String title, int trackNumber) {
		logger.info("Ogg encoding process started... " + wavFile.getName() + " -> " + oggFile.getName());
		try {
			ArrayList<String> command = new ArrayList<String>();
			if (SystemProperties.hostIsWindows())
				command.add(Constants.WINDOWS_TOOLS_DIR + SystemProperties.fileSeparator + OGGENC);
			else
				command.add(OGGENC);
			command.add(wavFile.getAbsolutePath());
			command.add(OUTPUT);
			command.add(oggFile.getAbsolutePath());
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
			command.add(QUALITY);
			command.add(quality);
			p = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String s = null;
			int percent = -1;
			while ((s = stdInput.readLine()) != null) {
				if (listener != null) {
					if (s.matches("\t\\[.....%.*")) {
						// Percent values can be for example 0.3% or 0,3%, so be careful with "." and ","
						int decimalPointPosition = s.indexOf('.');
						if (decimalPointPosition == -1)
							decimalPointPosition = s.indexOf(',');
						int aux = Integer.parseInt((s.substring(s.indexOf('[')+1, decimalPointPosition).trim()));
						if (aux != percent) {
							percent = aux;
							listener.notifyProgress(percent);
						}
					}
					else if (s.startsWith("Done"))
						listener.notifyProgress(100);
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
		return "ogg";
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

	public void stop() {
		if (p != null)
			p.destroy();	
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

}
