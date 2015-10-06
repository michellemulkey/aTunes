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

package net.sourceforge.atunes.kernel.modules.updates;

public class ApplicationVersion {

	private String date;
	private int majorNumber;
	private int minorNumber;
	private int revisionNumber;
	private String downloadURL;
		
	
	protected void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	protected void setMajorNumber(int majorNumber) {
		this.majorNumber = majorNumber;
	}

	protected void setMinorNumber(int minorNumber) {
		this.minorNumber = minorNumber;
	}

	protected void setRevisionNumber(int revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public String getDownloadURL() {
		return downloadURL;
	}
	
	public int getMajorNumber() {
		return majorNumber;
	}
	
	public int getMinorNumber() {
		return minorNumber;
	}
	
	public int getRevisionNumber() {
		return revisionNumber;
	}

	public String getDate() {
		return date;
	}

	protected void setDate(String date) {
		this.date = date;
	}
	
	public String getVersionNumber() {
		return majorNumber + "." + minorNumber + "." + revisionNumber;
	}
	
	
}
