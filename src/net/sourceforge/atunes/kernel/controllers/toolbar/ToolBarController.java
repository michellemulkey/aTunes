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

import javax.swing.JToolBar;

import net.sourceforge.atunes.gui.ToolBar;

public class ToolBarController extends net.sourceforge.atunes.kernel.controllers.model.ToolBarController {

	public ToolBarController(JToolBar toolBar) {
		super(toolBar);
		addBindings();
	}
	
	protected void addBindings() {
		ToolBarListener l = new ToolBarListener((ToolBar)toolBarControlled);
		((ToolBar)toolBarControlled).getSelectRepository().addActionListener(l);
		((ToolBar)toolBarControlled).getRefreshRepository().addActionListener(l);
		((ToolBar)toolBarControlled).getPreferences().addActionListener(l);
		((ToolBar)toolBarControlled).getShowNavigator().addActionListener(l);
		((ToolBar)toolBarControlled).getShowFileProperties().addActionListener(l);
		((ToolBar)toolBarControlled).getShowAudioScrobbler().addActionListener(l);
		((ToolBar)toolBarControlled).getStats().addActionListener(l);
		((ToolBar)toolBarControlled).getRipCD().addActionListener(l);
	}

	protected void addStateBindings() {
	}

	protected void notifyReload() {}
	
	public void setShowSongProperties(boolean show) {
		((ToolBar)toolBarControlled).getShowFileProperties().setSelected(show);
	}
	
	public void setShowNavigatorPanel(boolean show) {
		((ToolBar)toolBarControlled).getShowNavigator().setSelected(show);
	}
	
	public void setShowAudioScrobblerPanel(boolean show) {
		((ToolBar)toolBarControlled).getShowAudioScrobbler().setSelected(show);
	}

}
