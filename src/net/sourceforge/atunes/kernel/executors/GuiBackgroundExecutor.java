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

package net.sourceforge.atunes.kernel.executors;

import net.sourceforge.atunes.gui.views.dialogs.TitleDialog;

/**
 * This class is responsible of execution of GUI operations in secondary threads
 */
public class GuiBackgroundExecutor {
	
	private static Thread applicationTitleThread;
	static TitleDialog titleDialog;
	
	public static void showApplicationTitle() {
		Runnable runnable = new Runnable() {
			public void run() {
				titleDialog = new TitleDialog();
				titleDialog.setVisible(true);
			}
		};
		applicationTitleThread = new Thread(runnable);
		applicationTitleThread.start();
	}
	
	public static void hideApplicationTitle() {
		titleDialog.setVisible(false);
		titleDialog.dispose();
		applicationTitleThread.interrupt();
		applicationTitleThread = null;
	}
	
	

}
