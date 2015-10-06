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

package net.sourceforge.atunes.kernel.controllers.model;

import org.apache.log4j.Logger;

/**
 * @author fleax
 *
 */
public abstract class Controller {
	
	protected Logger logger;
	
	public Controller() {
		this.logger = Logger.getLogger(this.getClass());
	}
	
	protected abstract void addStateBindings();
	protected abstract void addBindings();
	protected abstract void notifyReload();
}
