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

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class NavigationTreeToolTipListener implements MouseMotionListener, MouseWheelListener {

	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationTreeToolTipListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
	}
	
	public void mouseMoved(java.awt.event.MouseEvent e) {
		if (!Kernel.getInstance().state.SHOW_ALBUM_TOOLTIP)
			return;
		
	    TreePath selectedPath = panel.getNavigationTree().getPathForLocation(e.getX(), e.getY());
	    if (selectedPath != null) {
	    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
	    	Object content = node.getUserObject();
	    	
	    	if (content instanceof Album) {
	    		if (!controller.getAlbumToolTip().isVisible() || controller.getLastAlbumToolTipContent() == null || controller.getLastAlbumToolTipContent() != content) {
	    			if (controller.getAlbumToolTip().isVisible())
	    				controller.getAlbumToolTip().setVisible(false);
	    			controller.getAlbumToolTip().setLocation((int)panel.getNavigationTree().getLocationOnScreen().getX() + e.getX(), (int)panel.getNavigationTree().getLocationOnScreen().getY() + e.getY() + 20);
	    			if (content instanceof Album) {
	    				controller.getAlbumToolTip().setPicture(((Album)content).getPicture());
	    				controller.getAlbumToolTip().setAlbum(((Album)content).getName());
	    				controller.getAlbumToolTip().setArtist(((Album)content).getArtist());
	    				int songs = ((Album)content).getSongs().size();
	    				controller.getAlbumToolTip().setSongs(songs + " " + (songs > 1 ? LanguageTool.getString("SONGS") : LanguageTool.getString("SONG")));
	    			}
	    			controller.setLastAlbumToolTipContent(content);

	    			controller.getAlbumToolTip().timer.setInitialDelay(Kernel.getInstance().state.ALBUM_TOOLTIP_DELAY * 1000);
	    			controller.getAlbumToolTip().timer.setRepeats(false);
	    			controller.getAlbumToolTip().timer.start();
	    		}
	    	}
	    	else {
	    		controller.setLastAlbumToolTipContent(null);
	    		controller.getAlbumToolTip().setVisible(false);
	    		controller.getAlbumToolTip().timer.stop();
	    	}
	    }
	    else {
    		controller.getAlbumToolTip().setVisible(false);
    		controller.getAlbumToolTip().timer.stop();
	    }
	}
	
	public void mouseDragged(MouseEvent arg0) {
		if (!Kernel.getInstance().state.SHOW_ALBUM_TOOLTIP)
			return;

		controller.setLastAlbumToolTipContent(null);
		controller.getAlbumToolTip().setVisible(false);
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!Kernel.getInstance().state.SHOW_ALBUM_TOOLTIP)
			return;

		controller.setLastAlbumToolTipContent(null);
		controller.getAlbumToolTip().setVisible(false);
	}

}
