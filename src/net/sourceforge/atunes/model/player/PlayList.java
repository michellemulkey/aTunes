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

package net.sourceforge.atunes.model.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


/**
 * @author fleax
 *
 */
public class PlayList extends ArrayList<AudioFile> {

	private static final long serialVersionUID = 1793886828081842328L;

	private boolean repeatMode;
	private boolean shuffleMode;
	
	private Random randomGenerator;
	private transient int randomCounter = 1;
	
	private int nextFile;
	
	public PlayList() {
		this(new ArrayList<AudioFile>());
	}
	
	public PlayList(ArrayList<AudioFile> files) {
		super(files);
		randomGenerator = new Random(System.currentTimeMillis());
	}
	
	public AudioFile getNextFileToPlay() {
		if (shuffleMode) {
			if (repeatMode) {
				randomCounter = 1;
				int next = Math.abs(randomGenerator.nextInt()) % size();
				nextFile = next;
				return get(nextFile);
			}
			else if (randomCounter < size()) {
				randomCounter++;
				int next = Math.abs(randomGenerator.nextInt()) % size();
				nextFile = next;
				return get(nextFile);
			}
			else {
				return null;
			}
		}
		else if (nextFile < size()-1)
			return get(++nextFile);
		else {
			if (repeatMode) {
				nextFile = 0;
				return get(nextFile);
			}
			return null;
		}
	}

	public void setSongs(ArrayList<AudioFile> songs) {
		clear();
		addAll(songs);
	}
	
	public AudioFile remove(int index) {
		randomCounter = 0;
		return super.remove(index);
	}
	
	public boolean removeAll(Collection c) {
		randomCounter = 0;
		return super.removeAll(c);
	}
	
	public AudioFile getPreviousFileToPlay() {
		if (shuffleMode) {
			if (randomCounter < size() || repeatMode) {
				randomCounter++;
				if (randomCounter == size()) randomCounter = 0;
				int prevFile = Math.abs(randomGenerator.nextInt() % size());
				nextFile = prevFile;
				return get(prevFile);
			}
			return null;
		}
		else if (nextFile > 0)
			return get(--nextFile);
		else {
			if (repeatMode) {
				nextFile = size()-1;
				return nextFile == -1 ? null : get(nextFile);
			}
			return null;
		}
	}
	
	public void setRepeatMode(boolean repeatMode) {
		this.repeatMode = repeatMode;
	}

	public void setShuffleMode(boolean shuffleMode) {
		this.shuffleMode = shuffleMode;
	}

	public int getNextFile() {
		return nextFile;
	}

	public void setNextFile(int nextFile) {
		this.nextFile = nextFile;
	}
	
	public AudioFile getCurrentFile() {
		return size() > 0 && size() > nextFile ? get(nextFile) : null;
	}

	public boolean isRepeatMode() {
		return repeatMode;
	}

	public boolean isShuffleMode() {
		return shuffleMode;
	}
}
