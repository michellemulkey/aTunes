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

/**
 * @author fleax
 *
 */
public class TimeUtils {

	public static String microseconds2String(long micros) {
		long aux = micros / 1000000;
		int minutes = (int) aux / 60;
		aux = aux % 60;
		return minutes + ":" + (aux < 10? "0" : "") + aux;
	}
	
	public static String milliseconds2String(long millis) {
		long aux = millis / 1000;
		int minutes = (int) aux / 60;
		aux = aux % 60;
		return minutes + ":" + (aux < 10? "0" : "") + aux;
	}
	
	public static String seconds2String(long seconds) {
		int minutes = (int) seconds / 60;
		seconds = seconds % 60;
		return minutes + ":" + (seconds < 10? "0" : "") + seconds;
	}
}
