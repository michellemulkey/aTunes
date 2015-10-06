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

package net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.kernel.modules.cdripper.ProgressListener;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.model.CDInfo;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.model.Device;
import net.sourceforge.atunes.kernel.utils.SystemProperties;

import org.apache.log4j.Logger;


public class Cdda2wav {

	public static final String CDDA2WAV = "cdda2wav";
	public static final String SCAN_BUS = "-scanbus";
	public static final String DEVICE = "-D";
	public static final String LIST_TRACKS = "-J";
	public static final String GUI = "-g";
	public static final String NO_INFO_FILE = "-H";
	public static final String TRACKS = "-t";
	public static final String VERSION = "--version";
	
	private Logger logger = Logger.getLogger(Cdda2wav.class);
	
	private ProgressListener listener;
	private NoCdListener noCdListener;
	
	private Device device;
	private Process process;
	
	boolean cdLoaded;
	
	public Cdda2wav() {
		if (SystemProperties.hostIsWindows())
			device = doScanBus().get(0); 
	}
	
	public static boolean testTool() {
		if (SystemProperties.hostIsWindows())
			return true;
		
		try {
			Process p = Runtime.getRuntime().exec(new String[] {CDDA2WAV, VERSION});
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while (stdInput.readLine() != null) {
			}			

			int code = p.waitFor();
			if (code != 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public ArrayList<Device> doScanBus() {
		logger.info("Scanning bus...");
		try {
			ArrayList<String> command = new ArrayList<String>();
			if (SystemProperties.hostIsWindows())
				command.add(Constants.WINDOWS_TOOLS_DIR + SystemProperties.fileSeparator + CDDA2WAV);
			else
				command.add(CDDA2WAV);
			command.add(SCAN_BUS);
			command.add(NO_INFO_FILE);
			process = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			ArrayList<Device> devices = new ArrayList<Device>();
			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				if (s.contains("CD-ROM")) {
					String line = s.trim();
					String id = line.substring(0, line.indexOf("\t"));
					id = id.replace("\t", "");
					line = line.substring(line.indexOf('\'') + 1, line.lastIndexOf('\''));
					String description = line.replaceAll("'", "");
					Device dev = new Device(id, description);
					devices.add(dev);
				}
			}			

			int code = process.waitFor();
			if (code != 0) {
				logger.error("Process returned code " + code);
				return null;
			}
			
			logger.info("Found " + devices.size() + " devices");
			return devices;
		} catch (Exception e) {
			logger.error("Process execution caused exception " + e);
			return null;
		}
	}
	
	public CDInfo getCDInfo() {
		logger.info("Getting cd information...");
		try {
			ArrayList<String> command = new ArrayList<String>();
			if (SystemProperties.hostIsWindows())
				command.add(Constants.WINDOWS_TOOLS_DIR + SystemProperties.fileSeparator + CDDA2WAV);
			else
				command.add(CDDA2WAV);
			if (device != null) {
				command.add(DEVICE);
				command.add(device.getId());
			}
			command.add(LIST_TRACKS);
			command.add(GUI);
			command.add(NO_INFO_FILE);
			process = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
			
			cdLoaded = false;
			
			Thread cdMonitor = new Thread() {
				public void run() {
					try {
						Thread.sleep(20000); // Wait until launch no cd notification
					} catch (InterruptedException e) {
					}
					if (!cdLoaded)
						notifyNoCd();
				}
			};
			cdMonitor.start();
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			// read the output from the command
			CDInfo cd = new CDInfo();
			String s = null;
			int tracks = 0;
			String totalDuration = null;
			String id = null;
			ArrayList<String> durations = new ArrayList<String>();
			while ((s = stdInput.readLine()) != null) {
				if (s.matches("Tracks:.*")) {
					tracks = Integer.parseInt(s.substring(s.indexOf(':')+1, s.indexOf(' ')));
					totalDuration = s.substring(s.indexOf(' ') + 1);
				}
				else if (s.matches("CDDB discid.*")) {
					id = s.substring(s.indexOf('0'));
				}
				else if (s.matches("T..:.*")) {
					String duration = s.substring(12, 18).trim();
					durations.add(duration);
				}
			}			

			cd.setTracks(tracks);
			cd.setDurations(durations);
			cd.setDuration(totalDuration);
			cd.setId(id);
			
			int code = process.waitFor();
			if (code != 0) {
				logger.error("Process returned code " + code);
				return null;
			}
			
			logger.info("CD info: " + cd);
			cdLoaded = true;
			return cd;
		} catch (Exception e) {
			logger.error("Process execution caused exception " + e);
			return null;
		}
	}
	
	public void notifyNoCd() {
		if (noCdListener != null)
			noCdListener.noCd();
	}
	
	public boolean cdda2wav(int track, File fileName) {
		logger.info("Writing wav file for track " + track + " in file " + fileName.getName());
		try {
			ArrayList<String> command = new ArrayList<String>();
			if (SystemProperties.hostIsWindows())
				command.add(Constants.WINDOWS_TOOLS_DIR + SystemProperties.fileSeparator + CDDA2WAV);
			else
				command.add(CDDA2WAV);
			if (device != null) {
				command.add(DEVICE);
				command.add(device.getId());
			}
			command.add(TRACKS + track + '+' + track);
			command.add(NO_INFO_FILE);
			command.add(fileName.getAbsolutePath());
			process = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				if (listener != null) {
					if (s.matches(".*%.*")) {
						int pos = s.indexOf('%');
						int percent = Integer.parseInt(s.substring(pos-3, pos).trim());
						listener.notifyProgress(percent);
					}
				}
			}
			
			int code = process.waitFor();
			if (code != 0) {
				logger.error("Process returned code " + code);
				return false;
			}
			
			logger.info("Wav file ok!!");
			return true;
		} catch (Exception e) {
			logger.error("Process execution caused exception " + e);
			return false;
		}
	}

	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}
	
	public void stop() {
		process.destroy();
	}

	public void setNoCdListener(NoCdListener noCdListener) {
		this.noCdListener = noCdListener;
	}
	
}
