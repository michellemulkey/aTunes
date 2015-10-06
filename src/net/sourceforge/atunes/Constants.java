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

package net.sourceforge.atunes;

/**
 * @author fleax
 *
 */
public class Constants {

	public static final int APP_MAJOR_NUMBER = 1;
	public static final int APP_MINOR_NUMBER = 6;
	public static final int APP_REVISION_NUMBER = 0;
	
	public static final String APP_VERSION_NUMBER = Integer.toString(APP_MAJOR_NUMBER) + '.' + Integer.toString(APP_MINOR_NUMBER) + '.' + Integer.toString(APP_REVISION_NUMBER);

	public static final String LOG4J_FILE = "log4j.properties";
	public static final String APP_WEB = "http://www.atunes.org";
	public static final String APP_SOURCEFORGE_WEB = "http://sourceforge.net/projects/atunes";
	public static final String APP_NAME = "aTunes";
	public static final String APP_DESCRIPTION = "GPL Audio Player";
	public static final String APP_VERSION = "Version " + APP_VERSION_NUMBER;
	public static final String APP_AUTHOR = "2006-2007 Alex Aranda";
	public static final String CACHE_REPOSITORY_NAME = "repository.dat";
	public static final String CACHE_SAVED_REPOSITORIES_NAME = "savedRepositories.dat";
	public static final int IMAGE_WIDTH = 90;
	public static final int IMAGE_HEIGHT = 90;
	public static final int TOOLTIP_IMAGE_WIDTH = 60;
	public static final int TOOLTIP_IMAGE_HEIGHT = 60;
	public static final int APP_SIMPLE_WIDTH = 350;
	public static final int APP_SIMPLE_HEIGTH = 220;
	public static final int APP_ULTRA_SIMPLE_WIDTH = 350;
	public static final int APP_ULTRA_SIMPLE_HEIGHT = 27;
	public static final int THUMBS_WINDOW_WIDTH = 900;
	public static final int THUMBS_WINDOW_HEIGHT = 700;
	public static final int MAX_RECENT_REPOSITORIES = 5;
	public static final String PROPERTIES_FILE = "atunes.config";
	public static final String LAST_PLAYLIST_FILE = "playList.dat";
	public static final String WINDOWS_TOOLS_DIR = "win_tools";
	public static final int AUDIO_SCROBBLER_IMAGE_WIDTH = 50;
	public static final int AUDIO_SCROBBLER_IMAGE_HEIGHT = 50;
}
