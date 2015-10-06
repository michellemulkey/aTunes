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

import java.awt.Font;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.UIManager;

import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.utils.Log4jPropertiesLoader;
import net.sourceforge.atunes.utils.StringUtils;
import net.sourceforge.atunes.utils.Timer;

import org.apache.log4j.Logger;

import com.fleax.ant.BuildNumberReader;

public class Main {
	
	/**
	 * Logger
	 */
	static Logger logger = Logger.getLogger(Main.class);

	/**
	 * Debug constant
	 */
	public static final String DEBUG = "debug";
	
	/**
	 * Multiple log constant
	 */
	public static final String MULTIPLE_LOG = "multiple_log";
	
	/**
	 * Main method for calling aTunes
	 * @param args
	 */
	public static void main(String[] args) {
		setUIFont(new javax.swing.plaf.FontUIResource(Font.SANS_SERIF,Font.PLAIN,12));
		
		// Start time measurement
		Timer.start();

		// Fetch arguments into a list
		ArrayList<String> arguments = StringUtils.fromStringArrayToList(args);
		
		// Set debug flag in kernel
		Kernel.DEBUG = arguments.contains(DEBUG);

		// Enable uncaught exception catching
		if (!Kernel.DEBUG)
			uncaughtExceptions();
		
		// Set log4j properties
		Log4jPropertiesLoader.loadProperties(Kernel.DEBUG, arguments.contains(MULTIPLE_LOG));
		
		// Log program properties
		logProgramProperties(arguments);
		
		// Start the Kernel, which really starts application
		Kernel.startKernel();
	}
	
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
	    //
	    // sets the default font for all Swing components.
	    // ex. 
	    //  setUIFont (new javax.swing.plaf.FontUIResource
	    //   ("Serif",Font.ITALIC,12));
	    //
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	    }    
	
	/**
	 * Redirects uncaught exceptions to logger
	 *
	 */
	private static void uncaughtExceptions() {
		try{
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					logger.error(e);
					StackTraceElement[] trace = e.getStackTrace();
					for (StackTraceElement te : trace)
						logger.error(te);
				}
			});
		}catch(Throwable t){
			logger.error(t);
		}
	}
	
	/**
	 * Log some properties
	 * @param arguments
	 */
	private static void logProgramProperties(ArrayList<String> arguments) {
		logger.info("Starting " + Constants.APP_NAME + ' ' + Constants.APP_VERSION_NUMBER + " (Build " + BuildNumberReader.getBuildNumber() + " [" + 
				new SimpleDateFormat("dd/MM/yyyy").format(BuildNumberReader.getBuildDate()) + "])");
		logger.info("Running in VM " + System.getProperty("java.vm.version"));
		logger.info("Arguments = " + arguments);
		logger.info("Debug mode = " + Kernel.DEBUG);
		logger.info("Execution path = " + new File("").getAbsolutePath());
	}
}
