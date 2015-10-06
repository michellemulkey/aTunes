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

package net.sourceforge.atunes.kernel.modules.mplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class MPlayerOutputReader extends Thread {

	private Logger logger = Logger.getLogger(MPlayerOutputReader.class);
	
	private MPlayerHandler handler;
	
	private BufferedReader in;
	
	private boolean finish = false;
	
	MPlayerOutputReader(MPlayerHandler handler, Process process) {
		this.handler = handler;
		in = new BufferedReader(new InputStreamReader(process.getInputStream()));
	}
	
	public void run() {
		String line = null;
		try {
			while ((line = in.readLine()) != null) {
				if (line.matches(".*ANS_LENGTH.*")) { // Read length
					int length = (int) (Float.parseFloat(line.substring(line.indexOf("=")+1)) * 1000.0);
					handler.setCurrentDuration(length);
					handler.setDuration(length);
					logger.info("LENGTH = " + length);
				}
				else if (line.matches(".*ANS_TIME_POSITION.*")) { // Read progress
					int time = (int) (Float.parseFloat(line.substring(line.indexOf("=")+1)) * 1000.0);
					handler.setTime(time);
					//logger.info("REM = " + (handler.getCurrentDuration() - time));
					if (!MPlayerHandler.GAP) {
						if (handler.getCurrentDuration() - time <= 500) {
							finish = true;
							handler.nextWithNoGap();
						}
					}
				}
				else if (line.matches("Exiting.*End.*")) { // EOF
					if (!finish)
						handler.next(true);
				}
			}
		} catch (IOException e) {
			handler.notifyPlayerError(e);
		}
	}
	
	
}
