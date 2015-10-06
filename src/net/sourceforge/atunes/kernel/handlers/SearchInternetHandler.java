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

package net.sourceforge.atunes.kernel.handlers;

import java.net.MalformedURLException;
import java.net.URL;

import net.sourceforge.atunes.model.search.Search;

import org.apache.log4j.Logger;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;


public class SearchInternetHandler {

	private static Logger logger = Logger.getLogger(SearchInternetHandler.class);

	public static void openSearch(Search search, String query) {
		if (search != null)
			try {
				Desktop.browse(search.getURL(query));
			} catch (MalformedURLException e) {
				logger.error(e);
			} catch (DesktopException e) {
				logger.error(e);
			}
	}
	
	public static void openURL(String url) {
		try {
			Desktop.browse(new URL(url));
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
