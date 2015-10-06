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

package net.sourceforge.atunes.kernel;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.ColorDefinitions;
import net.sourceforge.atunes.gui.LookAndFeelSelector;
import net.sourceforge.atunes.kernel.executors.BackgroundExecutor;
import net.sourceforge.atunes.kernel.handlers.ApplicationDataHandler;
import net.sourceforge.atunes.kernel.handlers.VisualHandler;
import net.sourceforge.atunes.kernel.modules.state.ApplicationState;
import net.sourceforge.atunes.kernel.utils.LanguageSelector;
import net.sourceforge.atunes.utils.StringUtils;
import net.sourceforge.atunes.utils.Timer;

import org.apache.log4j.Logger;


/**
 * The Kernel is the class responsible of create and interconnect all modules of aTunes. 
 */
public class Kernel {

	/** Unique instance of Kernel. To access Kernel, Kernel.getInstance() must be called */
	private static Kernel instance;
	
	/** Defines if aTunes is running in debug mode */
	public static boolean DEBUG;
	
	/** Logger */
	private Logger logger = Logger.getLogger(Kernel.class);

	/** Application State of aTunes */
	public ApplicationState state;
	
	/** Indicates if is the first time aTunes is executed */
	//private boolean firstExecution = firstExecutionTest();
	
	/** Constructor of Kernel */
	private Kernel() {}

	/**
	 * Static method to create the Kernel instance. 
	 * This method starts the application, so should 
	 * be called from the main method of the application.
	 */
	public static void startKernel() {
		instance = new Kernel();
		ColorDefinitions.initColors();
		if (!ApplicationDataHandler.readState())
			instance.state = new ApplicationState();
		
		// Set look and feel
		LookAndFeelSelector.setLookAndFeel(instance.state.getTheme());
		
		LanguageSelector.setLanguage();
		instance.startCreation();
		instance.start();
	}
	
	/** Creates all objects of aTunes: visual objects, controllers, and handlers  */
	private void startCreation() {
		VisualHandler.showTitle();
		startVisualization();
		startControllers();
		VisualHandler.hideTitle();
		HandlerProxy.getVisualHandler().setTitleBar("");
	}
	
	/** Starts visual objects */
	private void startVisualization() {
		HandlerProxy.getVisualHandler().startVisualization();
	}
	
	/** Starts controllers associated to visual classes */
	private void startControllers() {
		HandlerProxy.getControllerHandler();
	}
	
	/** Once all application is loaded, it's time to load data (repository, playlist) */
	private void start() {
		HandlerProxy.getVisualHandler().setFullFrameVisible(true);
		ApplicationDataHandler.applyState();
		BackgroundExecutor.readRepository();
		BackgroundExecutor.readPlayList();
		logger.info("Application started (" + StringUtils.toString(Timer.stop(), 3) + " seconds)");
	}
	
	/** Called when closing application, finished all necessary modules and writes configuration */
	public void finish() {
		logger.info("Closing " + Constants.APP_NAME + ' ' + Constants.APP_VERSION_NUMBER);
		HandlerProxy.getPlayerHandler().finish();
		HandlerProxy.getPlayListHandler().finish();
		if (HandlerProxy.getRepositoryHandler() != null)
			HandlerProxy.getRepositoryHandler().finish();
		ApplicationDataHandler.storeState();
		logger.info("Goodbye!!");
		System.exit(0);
	}

	/**
	 * Tests if it's the first time aTunes is executed. The test is done checking if file "aTunes.execution" exists.
	 * If not exists, it's the first time is executed
	 * @return true or false
	 */
//	private boolean firstExecutionTest() {
//		String fileName = new File(".").getAbsolutePath() + SystemProperties.fileSeparator + "aTunes.execution";
//		File f = new File(fileName);
//		if (!f.exists()) {
//			try {
//				f.createNewFile();
//			} catch (Exception e) {
//			}
//			return true;
//		}
//		else
//			return false;
//	}

	/**
	 * Getter of the Kernel instance
	 * @return Kernel
	 */
	public static Kernel getInstance() {
		return instance;
	}
}
