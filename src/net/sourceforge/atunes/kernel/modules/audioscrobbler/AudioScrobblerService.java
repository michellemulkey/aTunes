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

package net.sourceforge.atunes.kernel.modules.audioscrobbler;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import net.sourceforge.atunes.kernel.modules.network.NetworkUtils;
import net.sourceforge.atunes.kernel.modules.proxy.Proxy;
import net.sourceforge.atunes.kernel.modules.xml.XMLBuilder;
import net.sourceforge.atunes.kernel.modules.xml.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AudioScrobblerService {

	private static final String ARTIST_WILDCARD = "(%ARTIST%)";
	private static final String ALBUM_WILDCARD = "(%ALBUM%)";

	private static final String albumInfoURL = "http://ws.audioscrobbler.com/1.0/album/" + ARTIST_WILDCARD + '/' + ALBUM_WILDCARD + "/info.xml";
	private static final String albumListURL = "http://ws.audioscrobbler.com/1.0/artist/" + ARTIST_WILDCARD + "/topalbums.xml";
	private static final String similarArtistsURL = "http://ws.audioscrobbler.com/1.0/artist/" + ARTIST_WILDCARD + "/similar.xml";
	private static final String artistTagURL = "http://ws.audioscrobbler.com/1.0/artist/" + ARTIST_WILDCARD + "/toptags.xml";
	
	private static final String noCoverURL = "/depth/catalogue/noimage/cover_large.gif";
	
	private static final boolean showAlbumsWithoutCover = false;
	
	private Proxy proxy;
	
	public AudioScrobblerService(Proxy proxy) {
		this.proxy = proxy;
	}
	
	public AudioScrobblerAlbum getAlbum(String artist, String album) {
		try {
			// build url
			String urlString = albumInfoURL.replace(ARTIST_WILDCARD, NetworkUtils.encodeString(artist)).replace(ALBUM_WILDCARD, NetworkUtils.encodeString(album));
			URL url = new URL(urlString);
			// read xml
			Document xml = XMLBuilder.getXMLDocument(NetworkUtils.readURL(NetworkUtils.getConnection(url, proxy)));
			return AudioScrobblerAlbum.getAlbum(xml);
		} catch (Exception e) {
		}
		return null;
	}
	
	public ArrayList<AudioScrobblerAlbum> getAlbumList(String artist) {
		try {
			// build url
			String urlString = albumListURL.replace(ARTIST_WILDCARD, NetworkUtils.encodeString(artist));
			URL url = new URL(urlString);
			// read xml
			Document xml = XMLBuilder.getXMLDocument(NetworkUtils.readURL(NetworkUtils.getConnection(url, proxy)));
			ArrayList<AudioScrobblerAlbum> albums = AudioScrobblerAlbum.getAlbumList(xml);
			if (showAlbumsWithoutCover)
				return albums;
			ArrayList<AudioScrobblerAlbum> result = new ArrayList<AudioScrobblerAlbum>();
			for (AudioScrobblerAlbum a : albums) {
				if (!a.getSmallCoverURL().endsWith(noCoverURL))
					result.add(a);
			}
			return result;
		} catch (Exception e) {
		}
		return null;
	}
	
	public AudioScrobblerSimilarArtists getSimilarArtists(String artist) {
		try {
			// build url
			String urlString = similarArtistsURL.replace(ARTIST_WILDCARD, NetworkUtils.encodeString(artist));
			URL url = new URL(urlString);
			// read xml
			Document xml = XMLBuilder.getXMLDocument(NetworkUtils.readURL(NetworkUtils.getConnection(url, proxy)));
			return AudioScrobblerSimilarArtists.getSimilarArtists(xml);
		} catch (Exception e) {
		}
		return null;
	}
	
	public String getArtistTopTag(String artist) {
		try {
			// build url
			String urlString = artistTagURL.replace(ARTIST_WILDCARD, NetworkUtils.encodeString(artist));
			URL url = new URL(urlString);
			// read xml
			Document xml = XMLBuilder.getXMLDocument(NetworkUtils.readURL(NetworkUtils.getConnection(url, proxy)));
			return getTopTag(xml);
		} catch (Exception e) {
		}
		return null;
	}
	
	public Image getImage(AudioScrobblerAlbum album) {
		try {
			return NetworkUtils.getImage(NetworkUtils.getConnection(new URL(album.getCoverURL()), proxy));
		} catch (Exception e) {
			return null;
		}
	}
	
	public Image getImage(AudioScrobblerArtist artist) {
		try {
			return NetworkUtils.getImage(NetworkUtils.getConnection(new URL(artist.getImageUrl()), proxy));
		} catch (Exception e) {
			return null;
		}
	}

	public Image getImage(AudioScrobblerSimilarArtists similar) {
		try {
			return NetworkUtils.getImage(NetworkUtils.getConnection(new URL(similar.getPicture()), proxy));
		} catch (Exception e) {
			return null;
		}
	}
	
	public Image getSmallImage(AudioScrobblerAlbum album) {
		try {
			return NetworkUtils.getImage(NetworkUtils.getConnection(new URL(album.getSmallCoverURL()), proxy));
		} catch (Exception e) {
			return null;
		}
	}	
	
	private String getTopTag(Document xml) {
		Element el = (Element) xml.getElementsByTagName("toptags").item(0);
		
		NodeList tags = el.getElementsByTagName("tag");
		if (tags.getLength() > 0) {
			Element e = (Element) tags.item(0);
			return XMLUtils.getChildElementContent(e, "name");
		}
		return null;
	}
}
