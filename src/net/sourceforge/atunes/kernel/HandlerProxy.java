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

import net.sourceforge.atunes.kernel.handlers.AbstractPlayer;
import net.sourceforge.atunes.kernel.handlers.AudioScrobblerServiceHandler;
import net.sourceforge.atunes.kernel.handlers.PlayListHandler;
import net.sourceforge.atunes.kernel.handlers.RepositoryHandler;
import net.sourceforge.atunes.kernel.handlers.RipperHandler;
import net.sourceforge.atunes.kernel.handlers.SystemTrayHandler;
import net.sourceforge.atunes.kernel.handlers.VisualHandler;
import net.sourceforge.atunes.kernel.modules.mplayer.MPlayerHandler;

public class HandlerProxy {

	/** Handler responsible of controllers */
	private static ControllerProxy controllerHandler;
	
	/** Handler responsible of GUI operations */
	private static VisualHandler visualHandler;
	
	/** Handler responsible of read and manage repositories */
	private static RepositoryHandler repositoryHandler;
	
	/** Handler responsible of the player */
	private static AbstractPlayer playerHandler;
	
	/** Handler responsible of system tray icons */
	private static SystemTrayHandler systemTrayHandler;

	/** Handler responsible of Open Strands service */
	private static AudioScrobblerServiceHandler audioScrobblerServiceHandler;
	
	/** Handler responsible of play list operations (save, load) */
	private static PlayListHandler playListHandler;
	
	/** Handler responsible of CD-Ripping operations */
	private static RipperHandler ripperHandler;

	
	public static ControllerProxy getControllerHandler() {
		if (controllerHandler == null)
			controllerHandler = new ControllerProxy();
		return controllerHandler;
	}

	public static AudioScrobblerServiceHandler getAudioScrobblerServiceHandler() {
		if (audioScrobblerServiceHandler == null)
			audioScrobblerServiceHandler = new AudioScrobblerServiceHandler();
		return audioScrobblerServiceHandler;
	}

	public static AbstractPlayer getPlayerHandler() {
		if (playerHandler == null)
			playerHandler = new MPlayerHandler();
		return playerHandler;
	}

	public static PlayListHandler getPlayListHandler() {
		if (playListHandler == null)
			playListHandler = new PlayListHandler();
		return playListHandler;
	}

	public static RepositoryHandler getRepositoryHandler() {
		if (repositoryHandler == null)
			repositoryHandler = new RepositoryHandler();
		return repositoryHandler;
	}

	public static RipperHandler getRipperHandler() {
		if (ripperHandler == null)
			ripperHandler = new RipperHandler();
		return ripperHandler;
	}

	public static SystemTrayHandler getSystemTrayHandler() {
		if (systemTrayHandler == null)
			systemTrayHandler = new SystemTrayHandler();
		return systemTrayHandler;
	}

	public static VisualHandler getVisualHandler() {
		if (visualHandler == null)
			visualHandler = new VisualHandler();
		return visualHandler;
	}
}
