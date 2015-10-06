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

import java.util.ArrayList;

import net.sourceforge.atunes.utils.language.LanguageTool;


/**
 * @author fleax
 *
 */
public class StringUtils {

	private static final long KILOBYTE = 1024;
	private static final long MEGABYTE = KILOBYTE * 1024;
	private static final long GIGABYTE = MEGABYTE * 1024;
	
	private static final long SECONDS_MINUTE = 60;
	private static final long SECONDS_HOUR = 3600;
	private static final long SECONDS_DAY = 86400;
	
	public static String format(String s) {
		if (s != null) {
			s = s.trim();
			String result = "";
			int i = 0;
			boolean previousSpace = true;
			while (i < s.length()) {
				if (s.charAt(i) == ' ') {
					if (!previousSpace)
						result = result + s.charAt(i);
					previousSpace = true;
				}
				else if (previousSpace) {
					result = result + Character.toUpperCase(s.charAt(i));
					previousSpace = false;
				}
				else {
					result = result + s.charAt(i);
				}
				i++;
			}
			return result;
		}
		return null;
	}
	
	public static String toString(double value, int numberOfDecimals) {
		String result = Double.toString(value);
		if (result.contains(".")) {
			int nDecimals = Math.min(numberOfDecimals, result.length() - result.indexOf('.'));
			if (result.indexOf('.') + nDecimals + 1 < result.length())
				return result.substring(0, result.indexOf('.') + nDecimals + 1);
			return result.substring(0, result.length());
				
		}
		return result;
	}
	
	public static String fromByteToMegaOrGiga(long size) {
		if (size < KILOBYTE)
			return String.valueOf(size) + " bytes";
		else if (size < MEGABYTE)
			return toString((double)size / KILOBYTE, 2) + " Kb";
		else if (size < GIGABYTE)
			return toString((double)size / MEGABYTE, 2) + " Mb";
		else return toString((double)size / GIGABYTE, 2) + " Gb";
	}
	
	public static String fromSecondsToHoursAndDays(long seconds) {
		long days = seconds / SECONDS_DAY;
		seconds = seconds % SECONDS_DAY;
		long hours = seconds / SECONDS_HOUR;
		seconds = seconds % SECONDS_HOUR;
		long minutes = seconds / SECONDS_MINUTE;
		seconds = seconds % SECONDS_MINUTE;
		
		String hoursMinutesSeconds = hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
		if (days == 1)
			return days + " " + LanguageTool.getString("DAY") + " " + hoursMinutesSeconds;
		else if (days > 1)
			return days + " " + LanguageTool.getString("DAYS") + " " + hoursMinutesSeconds;
		else
			return hoursMinutesSeconds;
	}
	
	public static ArrayList<String> fromStringArrayToList(String[] str) {
		ArrayList<String> result = new ArrayList<String>();
		for (String s : str)
			result.add(s);
		return result;
	}
}
