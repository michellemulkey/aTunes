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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import net.sourceforge.atunes.gui.GuiUtils;
import net.sourceforge.atunes.gui.model.NavigationTableModel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


public class NavigationTreeControlsActionListener implements ActionListener {

	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationTreeControlsActionListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == panel.getCollapseTree()) {
			GuiUtils.collapseTree(panel.getNavigationTree());
			GuiUtils.collapseTree(panel.getFileNavigationTree());
			GuiUtils.collapseTree(panel.getFavoritesTree());
			GuiUtils.collapseTree(panel.getDeviceTree());
		}
		else if (e.getSource() == panel.getExpandTree()) {
			GuiUtils.expandTree(panel.getNavigationTree());
			GuiUtils.expandTree(panel.getFileNavigationTree());
			GuiUtils.expandTree(panel.getFavoritesTree());
			GuiUtils.expandTree(panel.getDeviceTree());
		}
		else if (e.getSource() == panel.getFilterTextField()) {
			String text = panel.getFilterTextField().getText();
			if (text.equals(""))
				text = null;
			controller.getState().setCurrentFilter(text);
			controller.refreshTreeContent();
		}
		else if (e.getSource() == panel.getClearFilterButton()) {
			panel.getFilterTextField().setText("");
			controller.getState().setCurrentFilter(null);
			controller.refreshTreeContent();
		}
		else if (e.getSource() == panel.getAddToPlayList()) {
			ArrayList<AudioFile> songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs();
			HandlerProxy.getPlayListHandler().addToPlayList(songs);
		}
		else if (e.getSource() == panel.getShowArtist()) {
			if (!controller.getState().isShowArtist()) {
				controller.getState().setShowArtist(true);
				controller.refreshTreeContent();
				panel.getCollapseTree().setEnabled(true);
				panel.getExpandTree().setEnabled(true);
			}
		}
		else if (e.getSource() == panel.getShowAlbum()) {
			if (controller.getState().isShowArtist()) {
				controller.getState().setShowArtist(false);
				controller.refreshTreeContent();
				panel.getCollapseTree().setEnabled(false);
				panel.getExpandTree().setEnabled(false);
			}
		}
	}
}
