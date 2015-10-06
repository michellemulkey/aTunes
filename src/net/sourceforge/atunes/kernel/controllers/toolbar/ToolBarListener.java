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

package net.sourceforge.atunes.kernel.controllers.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sourceforge.atunes.gui.ToolBar;
import net.sourceforge.atunes.kernel.HandlerProxy;

public class ToolBarListener implements ActionListener {

	private ToolBar toolBar;
	
	public ToolBarListener(ToolBar t) {
		this.toolBar = t;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(toolBar.getSelectRepository())) {
			HandlerProxy.getRepositoryHandler().selectRepository();
		}
		else if (e.getSource().equals(toolBar.getRefreshRepository())) {
			HandlerProxy.getRepositoryHandler().refreshRepository();
		}
		else if (e.getSource().equals(toolBar.getPreferences())) {
			HandlerProxy.getControllerHandler().getEditPreferencesDialogController().start();
		}
		else if (e.getSource().equals(toolBar.getShowNavigator())) {
			HandlerProxy.getVisualHandler().showNavigationPanel(toolBar.getShowNavigator().isSelected(), true);
		}
		else if (e.getSource().equals(toolBar.getShowFileProperties())) {
			HandlerProxy.getVisualHandler().showSongProperties(toolBar.getShowFileProperties().isSelected(), true);
		}
		else if (e.getSource().equals(toolBar.getShowAudioScrobbler())) {
			HandlerProxy.getVisualHandler().showAudioScrobblerPanel(toolBar.getShowAudioScrobbler().isSelected(), true);
		}
		else if (e.getSource().equals(toolBar.getStats())) {
			HandlerProxy.getControllerHandler().getStatsDialogController().showStats();
		}
		else if (e.getSource().equals(toolBar.getRipCD())) {
			HandlerProxy.getRipperHandler().startCdRipper();
		}
	}
	

}
