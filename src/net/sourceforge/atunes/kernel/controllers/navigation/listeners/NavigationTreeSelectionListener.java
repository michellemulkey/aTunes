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

package net.sourceforge.atunes.kernel.controllers.navigation.listeners;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.gui.model.NavigationTableModel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationControllerViews;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


public class NavigationTreeSelectionListener implements javax.swing.event.TreeSelectionListener {

	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationTreeSelectionListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
		
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		treeSelection((JTree)e.getSource());
	}
	
	private void treeSelection(JTree tree) {
    	ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
    	TreePath[] paths = tree.getSelectionPaths();
    	
    	// Avoid events when changes on favorite view
    	if (tree == panel.getFavoritesTree() && controller.getState().getNavigationView() != NavigationControllerViews.FAVORITE_VIEW)
    		return;
    	
    	if (paths != null) {
    		for (int i = 0; i < paths.length; i++) {
    			DefaultMutableTreeNode node = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
    			if (controller.getState().getNavigationView() == NavigationControllerViews.FAVORITE_VIEW)
    				songs.addAll(controller.getSongsForFavoriteTreeNode(node));
    			else if (controller.getState().getNavigationView() == NavigationControllerViews.DEVICE_VIEW)
    				songs.addAll(controller.getSongsForDeviceTreeNode(node));
    			else
    				songs.addAll(controller.getSongsForTreeNode(node));
    		}
    		((NavigationTableModel)panel.getNavigationTable().getModel()).setSongs(songs);
    		controller.adjustColumnsWidth();
    		panel.getNavigationTableAddButton().setEnabled(false);
    		panel.getNavigationTableInfoButton().setEnabled(false);
    	}
	}
}
