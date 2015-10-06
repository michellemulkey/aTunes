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

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.sourceforge.atunes.model.info.Folder;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class FileViewRefresher {

	public static void refresh(HashMap<String, Folder> structure, DefaultTreeModel treeModel, String currentFilter, String path) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
		root.setUserObject(LanguageTool.getString("FOLDERS") + "  (" + path + ')');
		root.removeAllChildren();
		RefreshUtils.addFolderNodes(structure, root, currentFilter);
		treeModel.reload();
	}
}
