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

import java.io.Serializable;
import java.util.HashMap;

import net.sourceforge.atunes.utils.StringUtils;


/**
 * @author fleax
 *
 */
public abstract class Tag implements Serializable {

	private String title;
	private String artist;
	private String album;
	private int year;
	private String comment;
	private String genre;

	 /**
     * Description of the Field
     */
    public static final String[] genres = { "Blues", "Classic Rock",
            "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
            "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap",
            "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska",
            "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient",
            "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical",
            "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
            "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
            "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
            "Gothic", "Darkwave", "Techno-Industrial", "Electronic",
            "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
            "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle",
            "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave",
            "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk",
            "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll",
            "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
            "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
            "Avantgarde", "Gothic Rock", "Progressive Rock",
            "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band",
            "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
            "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
            "Booty Brass", "Primus", "Porn Groove", "Satire", "Slow Jam",
            "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
            "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
            "A Capela", "Euro-House", "Dance Hall", "Goa", "Drum & Bass",
            "Club-House", "Hardcore", "Terror", "Indie", "BritPop",
            "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap",
            "Heavy Metal", "Black Metal", "Crossover",
            "Contemporary Christian", "Christian Rock", "Merengue", "Salsa",
            "Thrash Metal", "Anime", "JPop", "SynthPop" };


    public static final String getGenreForCode(int code) {
    	return code >= 0 ? genres[code] : "";
    }
    
    /**
     * Tries to find the string provided in the table and returns the
     * corresponding int code if successful. Returns -1 if the genres is not
     * found in the table.
     * 
     * @param str
     *            the genre to search for
     * @return the integer code for the genre or -1 if the genre is not found
     */
    public static int getGenre(String str) {
        int retval = -1;

        for (int i = 0; (i < genres.length); i++) {
            if (genres[i].equalsIgnoreCase(str)) {
                retval = i;
                break;
            }
        }

        return retval;
    }
	
	public String getAlbum() {
		return StringUtils.format(album);
	}
	public void setAlbum(String album) {
		if (album != null)
			this.album = album.trim();
	}
	
	public String getArtist() {
		return StringUtils.format(artist);
	}
	public void setArtist(String artist) {
		if (artist != null)
			this.artist = artist.trim();
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		if (comment != null && this.comment == null)
			this.comment = comment.trim();
	}
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(int genre) {
		this.genre = getGenreForCode(genre);
	}
	public void setGenre(String genre) {
		this.genre = StringUtils.format(genre);
	}
	
	public String getTitle() {
		return StringUtils.format(title);
	}
	public void setTitle(String title) {
		if (title != null)
			this.title = title.trim();
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public abstract Tag getTagFromProperties(HashMap properties);
}
