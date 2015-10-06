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

package net.sourceforge.atunes.kernel.utils;

import java.io.File;

public class SystemProperties {

	public static final String userHome = System.getProperty("user.home");
	public static final String fileSeparator = System.getProperty("file.separator");
	public static final String lineTerminator = getSystemLineTerminator();
	
	private static String getSystemLineTerminator() {
		if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) 
			return "\r\n";
		return "\n";
	}
	
	public static boolean hostIsWindows() {
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}
	
	/**
	 * Gets folder where state is stored. If not exists, it's created
	 * @param useWorkDir
	 * @return
	 */
	public static String getUserConfigFolder(boolean useWorkDir) {
		if (useWorkDir)
			return ".";
		String userHomePath = SystemProperties.userHome;
		if (userHomePath != null) {
			File userConfigFolder = new File(userHomePath + "/.aTunes");
			if (!userConfigFolder.exists()) {
				if (!userConfigFolder.mkdir())
					return ".";
			}
			return userConfigFolder.getAbsolutePath();
		}
		return ".";
	}

}
