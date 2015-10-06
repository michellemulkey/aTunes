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

package net.sourceforge.atunes.kernel.modules.repository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.handlers.RepositoryHandler;

import org.apache.log4j.Logger;


public class RepositoryAutoRefresher extends Thread {

	private Logger logger = Logger.getLogger(RepositoryAutoRefresher.class);
	
	private RepositoryHandler handler;
	
	public RepositoryAutoRefresher(RepositoryHandler repositoryHandler) {
		super();
		this.handler = repositoryHandler;
		setPriority(Thread.MIN_PRIORITY);
		if (Kernel.getInstance().state.AUTO_REPOSITORY_REFRESH_TIME != 0)
			start();
	}
	
	public void run() {
		super.run();
		try {
			while (true) {
				if (!handler.repositoryIsNull()) {
					logger.info("Checking for repository changes... (" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ')');
					int filesLoaded = handler.getSongs().size();
					int newFilesCount = RepositoryLoader.countFilesInRepositoryPath(new File(handler.getRepositoryPath()));
					if (filesLoaded != newFilesCount)
						HandlerProxy.getRepositoryHandler().refreshRepository();
				}
				Thread.sleep(Kernel.getInstance().state.AUTO_REPOSITORY_REFRESH_TIME * 60000);
			}
		} catch (Exception e) {
			return;
		}
	}
}
