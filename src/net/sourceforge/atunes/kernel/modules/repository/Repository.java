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

package net.sourceforge.atunes.kernel.modules.repository;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


/**
 * @author fleax
 *
 */
public class Repository implements Serializable {

	private static final long serialVersionUID = -8278937514875788175L;

	private File pathFile;
	private HashMap<String, AudioFile> files;
	private long totalSizeInBytes;
	private long totalDurationInSeconds;
	
	private RepositoryStructure structure;
	private RepositoryFavorites favorites;
	private RepositoryStats stats;
	
	public Repository(File path) {
		pathFile = path;
		files = new HashMap<String, AudioFile>();
		structure = new RepositoryStructure();
		favorites = new RepositoryFavorites();
		stats = new RepositoryStats();
	}
	
	public String getPath() {
		return pathFile.getAbsolutePath();
	}
	
	public int countFiles() {
		return files.size();
	}
	
	public File getPathFile() {
		return pathFile;
	}

	public ArrayList<AudioFile> getFilesList() {
		return new ArrayList<AudioFile>(files.values());
	}
	
	public HashMap<String, AudioFile> getFiles() {
		return files;
	}

	public AudioFile getFile(String fileName) {
		return files.get(fileName);
	}

	public long getTotalSizeInBytes() {
		return totalSizeInBytes;
	}
	
	public void setTotalSizeInBytes(long totalSizeInBytes) {
		this.totalSizeInBytes = totalSizeInBytes;
	}

	public RepositoryStructure getStructure() {
		return structure;
	}

	public RepositoryFavorites getFavorites() {
		return favorites;
	}

	public RepositoryStats getStats() {
		return stats;
	}

	public long getTotalDurationInSeconds() {
		return totalDurationInSeconds;
	}

	public void addDurationInSeconds(long seconds) {
		totalDurationInSeconds += seconds;
	}
}
