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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sourceforge.atunes.gui.model.NavigationTableModel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationControllerViews;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


public class NavigationTabbedPaneChangeListener implements ChangeListener {

	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationTabbedPaneChangeListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
	}
	
	public void stateChanged(ChangeEvent e) {
		int view = panel.getTabbedPane().getSelectedIndex();
		controller.setNavigationView(view);
		JTree tree;
		if (view == NavigationControllerViews.TAG_VIEW)
			tree = panel.getNavigationTree();
		else if (view == NavigationControllerViews.FILE_VIEW)
			tree = panel.getFileNavigationTree();
		else if (view == NavigationControllerViews.FAVORITE_VIEW)
			tree = panel.getFavoritesTree();
		else
			tree = panel.getDeviceTree();
		
		panel.getShowAlbum().setEnabled(view == NavigationControllerViews.TAG_VIEW || controller.isDeviceAttached() &&
				view == NavigationControllerViews.DEVICE_VIEW && controller.isSortDeviceByTag());
		panel.getShowArtist().setEnabled(view == NavigationControllerViews.TAG_VIEW || controller.isDeviceAttached() &&
				view == NavigationControllerViews.DEVICE_VIEW && controller.isSortDeviceByTag());
		panel.getFilterTextField().setEnabled(view != NavigationControllerViews.FAVORITE_VIEW);
		panel.getFilterLabel().setEnabled(view != NavigationControllerViews.FAVORITE_VIEW);
		panel.getClearFilterButton().setEnabled(view != NavigationControllerViews.FAVORITE_VIEW);
		if (tree.getSelectionPath() != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) (tree.getSelectionPath().getLastPathComponent());
			ArrayList<AudioFile> songs;
			if (view == NavigationControllerViews.FAVORITE_VIEW)
				songs = controller.getSongsForFavoriteTreeNode(node);
			else if (view == NavigationControllerViews.DEVICE_VIEW)
				songs = controller.getSongsForDeviceTreeNode(node);
			else
				songs = controller.getSongsForTreeNode(node);						
			((NavigationTableModel)panel.getNavigationTable().getModel()).setSongs(songs);
			panel.getNavigationTableAddButton().setEnabled(false);
			panel.getNavigationTableInfoButton().setEnabled(false);
		}
		controller.adjustColumnsWidth();
	}
}
