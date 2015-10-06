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

package net.sourceforge.atunes.kernel.executors.processes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.atunes.gui.views.dialogs.CopyProgressDialog;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.utils.SystemProperties;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;


public class ExportFilesProcess implements Runnable {
	
	private Logger logger = Logger.getLogger(ExportFilesProcess.class);
	
	public static final int ALL_EXPORT = 1;
	public static final int SELECTION_EXPORT = 2;
	public static final int FAVORITES_EXPORT = 3;

	public static final int FLAT_STRUCTURE = 1;
	public static final int ARTIST_STRUCTURE = 2;
	public static final int FULL_STRUCTURE = 3;
	
	private ArrayList<AudioFile> songs;
	private int structure;
	private String filePattern;
	private String path;
	
	private boolean interrupt;
	
	public ExportFilesProcess(ArrayList<AudioFile> songs, int structure, String filePattern, String path) {
		this.songs = songs;
		this.structure = structure;
		this.filePattern = filePattern;
		this.path = path;
	}
	
	public void run() {
		File destination = new File(path); 
		int filesExported = 0;

		HandlerProxy.getVisualHandler().getExportProgressDialog().getProgressLabel().setText(Integer.toString(filesExported));
		boolean errors = false;
		logger.info("Exporting songs to " + destination);
		try {
			for (Iterator it = songs.iterator(); it.hasNext() && !interrupt; ) {
				AudioFile song = (AudioFile) it.next();
				File destDir = getDirectory(song, destination, structure);
				FileUtils.copyFileToDirectory(song, destDir);
				if (filePattern != null) {
					File destFile = new File(destDir.getAbsolutePath() + SystemProperties.fileSeparator + song.getName());
					String newName = getNewName(filePattern, song);
					destFile.renameTo(new File(destDir.getAbsolutePath() + SystemProperties.fileSeparator + newName));
				}
				filesExported++;
				HandlerProxy.getVisualHandler().getExportProgressDialog().getProgressLabel().setText(Integer.toString(filesExported));
				HandlerProxy.getVisualHandler().getExportProgressDialog().getProgressBar().setValue(filesExported);
			}
		} catch (IOException e) {
			errors = true;
			logger.error(e.getMessage());
		}
		logger.info("Exporting process done");
		CopyProgressDialog dialog = HandlerProxy.getVisualHandler().getExportProgressDialog();
		dialog.setVisible(false);
		if (errors)
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("ERRORS_IN_EXPORT_PROCESS"));
	}
	
	public void notifyCancel() {
		interrupt = true;
	}
	
	private File getDirectory(AudioFile song, File destination, int structure) {
		if (structure == FLAT_STRUCTURE)
			return destination;
		else if (structure == ARTIST_STRUCTURE)
			return new File(destination.getAbsolutePath() + SystemProperties.fileSeparator + song.getArtist());
		else // FULL_STRUCTURE
			return new File(destination.getAbsolutePath() + SystemProperties.fileSeparator + song.getArtist() + SystemProperties.fileSeparator + song.getAlbum());
	}
	
	private String getNewName(String filePattern, AudioFile song) {
		String result = filePattern.replaceAll("%T", song.getTitleOrFileName());
		result = result.replaceAll("%A", song.getArtist());
		result = result.replaceAll("%L", song.getAlbum());
		result = result + song.getName().substring(song.getName().lastIndexOf('.'));
		return result;
	}
}
