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

package net.sourceforge.atunes.utils;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.kernel.utils.SystemProperties;

import org.apache.log4j.PropertyConfigurator;


public class Log4jPropertiesLoader {

	/**
	 * Set log4j properties
	 * @param multipleLog
	 */
	public static void loadProperties(boolean debug, boolean multipleLog) {
		PropertyResourceBundle bundle;
		try {
			bundle = new PropertyResourceBundle(new FileInputStream(Constants.LOG4J_FILE));
			Enumeration<String> keys = bundle.getKeys();
			Properties props = new Properties();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = bundle.getString(key);

				/** If MULTIPLELOG use a file with timestamp for every run of the program. If not, use normal aTunes.log file */
				if (key.equals("log4j.appender.A2.file")) {
					String timeStamp = new SimpleDateFormat("ddMMyyHHmmss").format(new Date());
					if (multipleLog)
						value = SystemProperties.getUserConfigFolder(debug) + SystemProperties.fileSeparator + "logs" + SystemProperties.fileSeparator + "aTunes_" + timeStamp + ".log";
					else
						value = SystemProperties.getUserConfigFolder(debug) + SystemProperties.fileSeparator + "aTunes.log";
				}
				
				props.setProperty(key, value);
			}
			PropertyConfigurator.configure(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
