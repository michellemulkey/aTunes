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

package net.sourceforge.atunes.kernel.controllers.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import net.sourceforge.atunes.model.info.Folder;


public class RefreshUtils {
	
	public static void addFolderNodes(HashMap<String, ?> folders, DefaultMutableTreeNode node, String currentFilter) {
		ArrayList<String> folderNamesList = new ArrayList<String>(folders.keySet());
		Collections.sort(folderNamesList);
		
		for (int i = 0; i < folderNamesList.size(); i++) {
			Folder f = (Folder) folders.get(folderNamesList.get(i));
			if (currentFilter == null || f.getName().toUpperCase().contains(currentFilter.toUpperCase())) {
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
				node.add(child);
				addFolderNodes(f.getFolders(), child, null);
			}
		}
	}
}
