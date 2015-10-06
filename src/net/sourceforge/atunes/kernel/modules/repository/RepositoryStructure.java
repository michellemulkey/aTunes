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

import java.io.Serializable;
import java.util.HashMap;

import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.model.info.Folder;



public class RepositoryStructure implements Serializable {
	
	private static final long serialVersionUID = -2230698137764691254L;

	private HashMap<String, Artist> treeStructure;
	private HashMap<String, Folder> folderStructure;
	
	protected RepositoryStructure() {
		treeStructure = new HashMap<String, Artist>();
		folderStructure = new HashMap<String, Folder>();
	}

	public HashMap<String, Folder> getFolderStructure() {
		return folderStructure;
	}

	public HashMap<String, Artist> getTreeStructure() {
		return treeStructure;
	}
}
