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

import net.sourceforge.atunes.kernel.modules.xml.XMLUtils;

import org.w3c.dom.Element;

public class AudioScrobblerTrack {

	private String title;
	private String url;
	
	protected static AudioScrobblerTrack getTrack(Element t) {
		AudioScrobblerTrack track = new AudioScrobblerTrack();
		
		track.title = t.getAttribute("title");
		track.url = XMLUtils.getChildElementContent(t, "url");
		
		return track;
	}
	
	
	public String getTitle() {
		return title;
	}
	public String getUrl() {
		return url;
	}
	
	
}
