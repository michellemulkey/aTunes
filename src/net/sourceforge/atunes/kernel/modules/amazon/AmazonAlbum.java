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

package net.sourceforge.atunes.kernel.modules.amazon;

import java.util.ArrayList;

import net.sourceforge.atunes.kernel.modules.xml.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AmazonAlbum {

	private String artist;
	private String album;
	private String imageURL;
	private ArrayList<AmazonDisc> discs;
	
	protected static AmazonAlbum getAlbum(Document xml) {
		AmazonAlbum album = new AmazonAlbum();
		
		Element root = (Element) xml.getElementsByTagName("ItemSearchResponse").item(0);
		Element operationRequest = (Element) root.getElementsByTagName("OperationRequest").item(0);
		Element argument = (Element) operationRequest.getElementsByTagName("Arguments").item(0);
		NodeList arguments = argument.getChildNodes();
		
		for (int i = 0; i < arguments.getLength(); i++) {
			Element arg = (Element) arguments.item(i);
			if (arg.getAttribute("Name").equals("Artist")) { // Artist
				album.artist = arg.getAttribute("Value");
			}
			if (arg.getAttribute("Name").equals("Title")) { // Album
				album.album = arg.getAttribute("Value");
			}
		}

		Element items = (Element) root.getElementsByTagName("Items").item(0);
		Element item = (Element) items.getElementsByTagName("Item").item(0);
		if (item == null) // No Items -> abort
			return null;
		Element mediumImage = (Element) item.getElementsByTagName("MediumImage").item(0);
		album.imageURL = XMLUtils.getChildElementContent(mediumImage, "URL");
		
		Element tracks = (Element) item.getElementsByTagName("Tracks").item(0);
		NodeList discs = tracks.getChildNodes();
		
		for (int i = 0; i < discs.getLength(); i++) {
			ArrayList<String> t = new ArrayList<String>();
			
			Element disc = (Element) discs.item(i);
			NodeList trackList = disc.getElementsByTagName("Track");
			
			for (int j = 0; j < trackList.getLength(); j++) {
				Element tr = (Element) trackList.item(j);
				t.add(tr.getTextContent());
			}
			
			AmazonDisc d = new AmazonDisc(t);
			album.discs.add(d);
		}
		
		
		
		return album;
	}
	
	protected AmazonAlbum() {
		discs = new ArrayList<AmazonDisc>();
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public ArrayList<AmazonDisc> getDiscs() {
		return discs;
	}
	
	public String getImageURL() {
		return imageURL;
	}
}
