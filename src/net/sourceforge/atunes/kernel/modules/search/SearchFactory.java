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

package net.sourceforge.atunes.kernel.modules.search;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.atunes.model.search.Search;


public class SearchFactory {

	public static Search youTubeSearch = new Search() {
		public URL getURL(String query) throws MalformedURLException {
			query = query.replaceAll(" +","+");
			return new URL("http://www.youtube.com/results?search_query=" + query + "&search=Search");
		}
		public String toString() {
			return "YouTube";
		}
	};
	
	public static Search wikipediaENSearch = new Search() {
		public URL getURL(String query) throws MalformedURLException {
			query = query.replaceAll(" +","_");
			return new URL("http://en.wikipedia.org/wiki/" + query);
		}
		public String toString() {
			return "Wikipedia (English)";
		}
	};
	
	public static Search freeDBSearch = new Search() {
		public URL getURL(String query) throws MalformedURLException {
			query = query.replaceAll(" +","+");
			return new URL("http://www.freedb.org/freedb_search.php?words=" + query);
		}
		public String toString() {
			return "FreeDB";
		}
	};
	
	public static Search musicBrainzSearch = new Search() {
		public URL getURL(String query) throws MalformedURLException {
			query = query.replaceAll(" +","+");
			String quotedQuery = "%22" + query + "%22";
			return new URL("http://musicbrainz.org/search/textsearch.html?query=" + quotedQuery + "&type=artist&limit=&an=1&as=1&handlearguments=1");
		}
		public String toString() {
			return "MusicBrainz";
		}
	};
	
	public static Search googleVideoSearch = new Search() {
		public URL getURL(String query) throws MalformedURLException {
			query = query.replaceAll(" +","+");
			return new URL("http://video.google.com/videosearch?q=" + query);
		}
		public String toString() {
			return "Google Video";
		}
	};

	public static List<Search> getSearches() {
		ArrayList<Search> result = new ArrayList<Search>();
		result.add(freeDBSearch);
		result.add(googleVideoSearch);
		result.add(musicBrainzSearch);
		result.add(wikipediaENSearch);
		result.add(youTubeSearch);
		return result;
	}
}
