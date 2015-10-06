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

package net.sourceforge.atunes.kernel.executors;

import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.executors.processes.ChangeTagsProcess;
import net.sourceforge.atunes.kernel.executors.processes.ChangeTitlesProcess;
import net.sourceforge.atunes.kernel.executors.processes.ExportFilesProcess;
import net.sourceforge.atunes.kernel.executors.processes.SetGenresProcess;
import net.sourceforge.atunes.kernel.executors.processes.SetTrackNumberProcess;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


/**
 * This class is responsible of execute operations in secondary threads
 */
public class BackgroundExecutor {
	
	private static final int READ_REPOSITORY_PRIORITY = Thread.MAX_PRIORITY;
	
	private static final int NORMAL_PRIORITY = Thread.NORM_PRIORITY;
	
	public static void readRepository() {
		Runnable r = new Runnable() {
			public void run() {
				HandlerProxy.getRepositoryHandler().readRepository();
			}
		};
		run(r, READ_REPOSITORY_PRIORITY);
	}
	
	public static void readPlayList() {
		Runnable r = new Runnable() {
			public void run() {
				HandlerProxy.getPlayListHandler().getLastPlayList();
			}
		};
		run(r, NORMAL_PRIORITY);
	}
	
	public static void loadPlayList(ArrayList<String> files) {
		Runnable r = HandlerProxy.getPlayListHandler().getLoadPlayListProcess(files);
		run(r, NORMAL_PRIORITY);
	}

	public static void changeTags(ArrayList<AudioFile> files, HashMap<String, ?>  properties) {
		run(new ChangeTagsProcess(files, properties), NORMAL_PRIORITY);
	}
	
	public static void changeTitles(HashMap<AudioFile, String> filesAndTitles) {
		run(new ChangeTitlesProcess(filesAndTitles), NORMAL_PRIORITY);
	}
	
	public static void changeTrackNumbers(HashMap<AudioFile, Integer> filesAndTracks) {
		run(new SetTrackNumberProcess(filesAndTracks), NORMAL_PRIORITY);
	}
	
	public static void changeGenres(HashMap<AudioFile, String> filesAndGenres) {
		run(new SetGenresProcess(filesAndGenres), NORMAL_PRIORITY);
	}

	public static ExportFilesProcess exportFiles(ArrayList<AudioFile> songs, int exportStructure, String filePattern, String path) {
		final ExportFilesProcess exporter = new ExportFilesProcess(songs, exportStructure, filePattern, path);
		run(exporter, NORMAL_PRIORITY);
		return exporter;
	}
	
	private static void run(Runnable r, int priority) {
		Thread t = new Thread(r);
		t.setPriority(priority);
		t.start();
	}
}
