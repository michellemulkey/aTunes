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

package net.sourceforge.atunes.kernel;

import net.sourceforge.atunes.gui.ToolBar;
import net.sourceforge.atunes.gui.views.dialogs.EditTitlesDialog;
import net.sourceforge.atunes.gui.views.menu.ApplicationMenuBar;
import net.sourceforge.atunes.gui.views.panels.FilePropertiesPanel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.gui.views.panels.PlayListFilterPanel;
import net.sourceforge.atunes.gui.views.panels.PlayListPanel;
import net.sourceforge.atunes.gui.views.panels.PlayerControlsPanel;
import net.sourceforge.atunes.kernel.controllers.audioScrobbler.AudioScrobblerController;
import net.sourceforge.atunes.kernel.controllers.editPreferencesDialog.EditPreferencesDialogController;
import net.sourceforge.atunes.kernel.controllers.editTagDialog.EditTagDialogController;
import net.sourceforge.atunes.kernel.controllers.editTitlesDialog.EditTitlesDialogController;
import net.sourceforge.atunes.kernel.controllers.exportOptionsDialog.ExportOptionsDialogController;
import net.sourceforge.atunes.kernel.controllers.fileProperties.FilePropertiesController;
import net.sourceforge.atunes.kernel.controllers.menu.MenuController;
import net.sourceforge.atunes.kernel.controllers.navigation.NavigationController;
import net.sourceforge.atunes.kernel.controllers.playList.PlayListController;
import net.sourceforge.atunes.kernel.controllers.playListControls.PlayListControlsController;
import net.sourceforge.atunes.kernel.controllers.playListFilter.PlayListFilterController;
import net.sourceforge.atunes.kernel.controllers.playerControls.PlayerControlsController;
import net.sourceforge.atunes.kernel.controllers.stats.StatsDialogController;
import net.sourceforge.atunes.kernel.controllers.toolbar.ToolBarController;


public class ControllerProxy {
	
	//private static Logger logger = Logger.getLogger(ControllerProxy.class);
	
	private MenuController fullViewMenuController;
	private NavigationController navigationController;
	private PlayListController playListController;
	private PlayListControlsController playListControlsController;
	private PlayListFilterController playListFilterController;
	private PlayerControlsController playerControlsController;
	private FilePropertiesController filePropertiesController;
	private EditTagDialogController editTagDialogController;
	private ExportOptionsDialogController exportOptionsController;
	private StatsDialogController statsDialogController;
	private EditTitlesDialogController editTitlesDialogController;
	private AudioScrobblerController audioScrobblerController;
	private EditPreferencesDialogController editPreferencesDialogController;
	private ToolBarController toolBarController;
	
	public ControllerProxy() {
		// Force creation of non-autocreated controllers
		getPlayListFilterController();
	}
	
	public MenuController getMenuController() {
		if (fullViewMenuController == null) {
			ApplicationMenuBar menuBar = HandlerProxy.getVisualHandler().getMenuBar();
			fullViewMenuController = new MenuController(menuBar);
		}
		return fullViewMenuController;
	}
	
	public ToolBarController getToolBarController() {
		if (toolBarController == null) {
			ToolBar t = HandlerProxy.getVisualHandler().getToolBar();
			toolBarController = new ToolBarController(t);
		}
		return toolBarController;
	}
	
	public NavigationController getNavigationController() {
		if (navigationController == null) {
			NavigationPanel panel = HandlerProxy.getVisualHandler().getNavigationPanel();
			navigationController = new NavigationController(panel);
		}
		return navigationController;
	}
	
	public PlayListController getPlayListController() {
		if (playListController == null) {
			PlayListPanel panel = null;
			panel = HandlerProxy.getVisualHandler().getPlayListPanel();
			playListController = new PlayListController(panel);
		}
		return playListController;
	}
	
	public PlayListControlsController getPlayListControlsController() {
		if (playListControlsController == null) {
			PlayListPanel panel = HandlerProxy.getVisualHandler().getPlayListPanel();
			playListControlsController = new PlayListControlsController(panel.getPlayListControls());
		}
		return playListControlsController;
	}
	
	public PlayListFilterController getPlayListFilterController() {
		if (playListFilterController == null) {
			PlayListFilterPanel panel = HandlerProxy.getVisualHandler().getPlayListPanel().getPlayListFilter();
			playListFilterController = new PlayListFilterController(panel);
		}
		return playListFilterController;
	}
	
	public PlayerControlsController getPlayerControlsController() {
		if (playerControlsController == null) {
			PlayerControlsPanel panel = null;
			panel = HandlerProxy.getVisualHandler().getPlayListPanel().getPlayerControls();
			playerControlsController = new PlayerControlsController(panel);
		}
		return playerControlsController;
	}
	
	public FilePropertiesController getFilePropertiesController() {
		if (filePropertiesController == null) {
			FilePropertiesPanel panel = HandlerProxy.getVisualHandler().getPropertiesPanel();
			filePropertiesController = new FilePropertiesController(panel);
		}
		return filePropertiesController;
	}
	
	public EditTagDialogController getEditTagDialogController() {
		if (editTagDialogController == null) {
			editTagDialogController = new EditTagDialogController(HandlerProxy.getVisualHandler().getEditTagDialog());
		}
		return editTagDialogController;
	}
	
	public ExportOptionsDialogController getExportOptionsController() {
		if (exportOptionsController == null) {
			exportOptionsController = new ExportOptionsDialogController(HandlerProxy.getVisualHandler().getExportDialog());
		}
		return exportOptionsController;
	}
	
	public StatsDialogController getStatsDialogController() {
		if (statsDialogController == null) {
			statsDialogController = new StatsDialogController(HandlerProxy.getVisualHandler().getStatsDialog());
		}
		return statsDialogController;
	}

	public EditTitlesDialogController getEditTitlesDialogController() {
		if (editTitlesDialogController == null) {
			EditTitlesDialog dialog = HandlerProxy.getVisualHandler().getEditTitlesDialog();
			editTitlesDialogController = new EditTitlesDialogController(dialog);
		}
		return editTitlesDialogController;
	}

	public AudioScrobblerController getAudioScrobblerController() {
		if (audioScrobblerController == null) {
			audioScrobblerController = new AudioScrobblerController(HandlerProxy.getVisualHandler().getAudioScrobblerPanel());
		}
		return audioScrobblerController;
	}

	public EditPreferencesDialogController getEditPreferencesDialogController() {
		if (editPreferencesDialogController == null)
			editPreferencesDialogController = new EditPreferencesDialogController();
		return editPreferencesDialogController;
	}

}
