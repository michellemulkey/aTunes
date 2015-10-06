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

import net.sourceforge.atunes.gui.model.NavigationTableModel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.handlers.RepositoryHandler.SortType;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;


public class NavigationTableControlsActionListener implements ActionListener {

	private NavigationController controller;
	private NavigationPanel panel;
	
	public NavigationTableControlsActionListener(NavigationController controller, NavigationPanel panel) {
		this.controller = controller;
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == panel.getNavigationTableInfoButton()) {
			int[] selRow = panel.getNavigationTable().getSelectedRows();
			ArrayList<AudioFile> songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongsAt(selRow);
			HandlerProxy.getVisualHandler().showInfo(songs.get(0));
		}
		else if (e.getSource() == panel.getNavigationTableAddButton()) {
			int[] selRow = panel.getNavigationTable().getSelectedRows();
			ArrayList<AudioFile> songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongsAt(selRow);
			HandlerProxy.getPlayListHandler().addToPlayList(songs);
		}
		else if (e.getSource() == panel.getSortByTrack()) {
			if (controller.getState().getSortType() != SortType.BY_TRACK) {
				controller.getState().setSortType(SortType.BY_TRACK);
				ArrayList<AudioFile> songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs();
				((NavigationTableModel)panel.getNavigationTable().getModel()).setSongs(controller.sort(songs, SortType.BY_TRACK));
				controller.adjustColumnsWidth();
			}
		}
		else if (e.getSource() == panel.getSortByTitle()) {
			if (controller.getState().getSortType() != SortType.BY_TITLE) {
				controller.getState().setSortType(SortType.BY_TITLE);
				ArrayList<AudioFile> songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs();
				((NavigationTableModel)panel.getNavigationTable().getModel()).setSongs(controller.sort(songs, SortType.BY_TITLE));
				controller.adjustColumnsWidth();
			}
		}
		else if (e.getSource() == panel.getSortByFile()) {
			if (controller.getState().getSortType() != SortType.BY_FILE) {
				controller.getState().setSortType(SortType.BY_FILE);
				ArrayList<AudioFile> songs = ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongs();
				((NavigationTableModel)panel.getNavigationTable().getModel()).setSongs(controller.sort(songs, SortType.BY_FILE));
				controller.adjustColumnsWidth();
			}
		}
	}
}
