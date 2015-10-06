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

import javax.swing.ImageIcon;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


/**
 * @author fleax
 *
 */
public class Album implements Serializable, TreeObject {

	private static final long serialVersionUID = -1481314950918557022L;

	private String name;
	private String artist;
	private ImageIcon picture;
	private ArrayList<AudioFile> songs;
	
	public Album(String name) {
		this.name = name;
		songs = new ArrayList<AudioFile>();
	}
	
	public void addSong(AudioFile file) {
		songs.add(file);
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public ArrayList<AudioFile> getSongs() {
		return (ArrayList<AudioFile>) songs.clone();
	}
	
	public void removeSong(AudioFile file) {
		songs.remove(file);
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public ImageIcon getPicture() {
		return picture;
	}

	public void setPicture(ImageIcon picture) {
		this.picture = picture;
	}
}
