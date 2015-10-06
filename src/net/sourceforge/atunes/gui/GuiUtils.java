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

package net.sourceforge.atunes.gui;

import java.awt.Toolkit;

import javax.swing.JTree;

public class GuiUtils {

	public static void collapseTree(JTree tree) {
		for (int i = 1; i < tree.getRowCount(); i++) {
			tree.collapseRow(i);
		}
		tree.setSelectionRow(0);
	}
	
	public static void expandTree(JTree tree) {
		for (int i = 1; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		tree.setSelectionRow(0);
	}
	
	/**
	 * Returns a proportional width according to screenWidth and desiredSize for the current screen resolution
	 * @param screenWidth
	 * @param desiredSize
	 * @return
	 */
	public static int getComponentWidthForResolution(int screenWidth, int desiredSize) {
		int currentScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int result = desiredSize * currentScreenWidth / screenWidth;
		return result;
	}
}
