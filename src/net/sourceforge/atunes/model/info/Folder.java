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

package net.sourceforge.atunes.model.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


/**
 * @author fleax
 *
 */
public class Folder implements Serializable, TreeObject {
	
	private static final long serialVersionUID = 2608221109707838025L;

	private String name;
	private ArrayList<AudioFile> files;
	private HashMap<String, Folder> folders;
	
	public Folder(String name) {
		this.name = name;
		this.files = new ArrayList<AudioFile>();
		folders = new HashMap<String, Folder>();
	}

	public String getName() {
		return name;
	}

	public void addFolder(Folder f) {
		if (folders.containsKey(f.getName())) {
			Folder folder = folders.get(f.getName());
			folder.addFoldersOf(f);
		}
		else
			folders.put(f.getName(), f);
	}
	
	private void addFoldersOf(Folder f) {
		folders.putAll(f.getFolders());
	}
	
	public ArrayList getFiles() {
		return files;
	}
	
	public Folder getFolder(String name) {
		return folders.get(name);
	}
	
	public HashMap<String, Folder> getFolders() {
		return folders;
	}
	
	public ArrayList<AudioFile> getSongs() {
		ArrayList<AudioFile> result = new ArrayList<AudioFile>();
		result.addAll(files);
		for (Iterator it = folders.keySet().iterator(); it.hasNext(); ) {
			Folder f = folders.get(it.next());
			result.addAll(f.getSongs());
		}
		return result;
	}
	
	public void addFile(AudioFile file) {
		files.add(file);
	}
	
	public boolean containsFolder(String folderName) {
		return folders.containsKey(folderName);
	}
	
	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
