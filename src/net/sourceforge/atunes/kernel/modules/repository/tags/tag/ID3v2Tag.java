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

package net.sourceforge.atunes.kernel.modules.repository.tags.tag;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author fleax
 *
 */
public class ID3v2Tag extends ID3v1_1Tag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4192002826764888355L;
	private int pictureBegin;
	private int pictureLength;
	
	public ID3v2Tag() {
		super();
	}
	
	public void addFrame(String frameID, String data) {
		// Null condition of fields checked to avoid repeated frames
		String dataTrimmed = data.trim();
		if (getTitle() == null && frameID.equals("TIT2") || frameID.equals("TT2")) {
			setTitle(dataTrimmed);
		}
		else if (getArtist() == null && frameID.equals("TPE1") || frameID.equals("TP1")) {
			setArtist(dataTrimmed);
		}
		else if (getAlbum() == null && frameID.equals("TALB") || frameID.equals("TAL")) {
			setAlbum(dataTrimmed);
		}
		else if (getTrackNumber() == 0 && frameID.equals("TRCK") || frameID.equals("TRK")) {
			try {
				// Try to read an int value
				int track = Integer.parseInt(dataTrimmed);
				setTrackNumber(track > 0 ? track : 0);
			} catch (NumberFormatException e) {
				// Try to read a string of type "1/10"
				StringTokenizer st = new StringTokenizer(dataTrimmed, "/");
				if (st.hasMoreTokens()) {
					String s = st.nextToken();
					try {
						int track = Integer.parseInt(s);
						setTrackNumber(track > 0 ? track : 0);
					} catch (NumberFormatException ex) {
						setTrackNumber(0);
					}
				}
				else
					setTrackNumber(0);
			}
			if (getTrackNumber() < 0)
				setTrackNumber(0);
		}
		else if (getYear() == 0 && frameID.equals("TYER")) {
			try {
				if (Integer.parseInt(dataTrimmed) != 0)
					setYear(Integer.parseInt(dataTrimmed));
				else
					setYear(0);
			} catch (NumberFormatException e) {
				setYear(0);
			}
		}
		else if (getComment() == null && frameID.equals("COMM") || frameID.equals("COM")) {
			setComment(dataTrimmed);
		}
		else if (getGenre() == null && frameID.equals("TCON") || frameID.equals("TCO")) {
			if (dataTrimmed.matches("\\([0-9]+\\).*")) {
				String genreCode = dataTrimmed.substring(dataTrimmed.indexOf("(")+1, dataTrimmed.indexOf(")"));
				try {
					int genre = Integer.parseInt(genreCode);
					setGenre(genre);
				} catch (NumberFormatException e){
				}
			}
			else
				setGenre(dataTrimmed);
		}
	}
	
	public int getPictureBegin() {
		return pictureBegin;
	}

	public void setPictureBegin(int pictureBegin) {
		this.pictureBegin = pictureBegin;
	}

	public int getPictureLength() {
		return pictureLength;
	}

	public void setPictureLength(int pictureLength) {
		this.pictureLength = pictureLength;
	}
	
	@Override
	public Tag getTagFromProperties(HashMap properties) {
		ID3v2Tag id3Tag = new ID3v2Tag();
		id3Tag.setTitle((String)properties.get("TITLE"));
		id3Tag.setArtist((String)properties.get("ARTIST"));
		id3Tag.setAlbum((String)properties.get("ALBUM"));
		try {
			id3Tag.setYear(Integer.parseInt((String)properties.get("YEAR")));
		} catch (NumberFormatException ex) {
			id3Tag.setYear(-1);
		}
		id3Tag.setComment((String)properties.get("COMMENT"));
		String genreString = (String)properties.get("GENRE");
		if (genreString == null)
			id3Tag.setGenre(-1);
		else {
			int genre = Tag.getGenre(genreString);
			id3Tag.setGenre(genre);
		}
		try {
			id3Tag.setTrackNumber(Integer.parseInt((String)properties.get("TRACK")));
		} catch (NumberFormatException ex) {
			id3Tag.setTrackNumber(-1);
		}
		return id3Tag;
	}

}
