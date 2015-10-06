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

package net.sourceforge.atunes.kernel.controllers.editPreferencesDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.atunes.gui.views.dialogs.editPreferences.AudioScrobblerPanel;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.EditPreferencesDialog;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.GeneralPanel;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.InternetPanel;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.NavigatorPanel;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.OSDPanel;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.PlayerPanel;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.PreferencesPanel;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.RepositoryPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.model.DialogController;
import net.sourceforge.atunes.kernel.modules.proxy.ProxyBean;
import net.sourceforge.atunes.kernel.modules.state.ApplicationState;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class EditPreferencesDialogController extends DialogController {

	EditPreferencesDialog dialog;
	
	private static final String GENERAL = LanguageTool.getString("GENERAL");
	private static final String REPOSITORY = LanguageTool.getString("REPOSITORY");
	private static final String NAVIGATOR = LanguageTool.getString("NAVIGATOR");
	private static final String OSD = LanguageTool.getString("OSD");
	private static final String AUDIO_SCROBBLER = LanguageTool.getString("AUDIO_SCROBBLER");
	private static final String INTERNET = LanguageTool.getString("INTERNET");
	private static final String PLAYER = LanguageTool.getString("PLAYER");
	
	private String[] treeNodes = new String[] {GENERAL, REPOSITORY, NAVIGATOR, OSD, AUDIO_SCROBBLER, INTERNET};
	
	private InternetPanel internetPanel;
	
	private PreferencesPanel[] panels = new PreferencesPanel[] {getGeneralPanel(), getRepositoryPanel(), 
			getNavigatorPanel(), getOSDPanel(), getAudioScrobblerPanel(), getInternetPanel()};
	
	public EditPreferencesDialogController() {
		super(HandlerProxy.getVisualHandler().getEditPreferencesDialog());
		dialog = HandlerProxy.getVisualHandler().getEditPreferencesDialog();
		addBindings();
	}
	
	public void start() {
		dialog.setPanels(panels);
		buildList();
		updateProperties();
		
		dialog.setVisible(true);
		
	}
	
	// Updates properties that may be changed
	private void updateProperties() {
		((AudioScrobblerPanel)panels[4]).setActivateAudioScrobbler(Kernel.getInstance().state.isUseAudioScrobbler());
	}
	
	private void buildList() {
		DefaultListModel listModel = new DefaultListModel();
		
		for (String s: treeNodes)
			listModel.addElement(s);
		
		dialog.setListModel(listModel);
		dialog.getList().setSelectedIndex(0);
	}
	
	private PreferencesPanel getGeneralPanel() {
		GeneralPanel panel = new GeneralPanel();
		panel.setShowTitle(Kernel.getInstance().state.SHOW_TITLE);
		panel.setWindowType(Kernel.getInstance().state.isMultipleWindow() ? LanguageTool.getString("MULTIPLE_WINDOW") : LanguageTool.getString("STANDARD_WINDOW"));
		panel.setLanguage(LanguageTool.getLanguageSelected());
		panel.setShowIconTray(Kernel.getInstance().state.isShowSystemTray());
		panel.setShowTrayPlayer(Kernel.getInstance().state.isShowTrayPlayer());
		panel.setTheme(Kernel.getInstance().state.getTheme());
		return panel;
	}
	
	private PreferencesPanel getRepositoryPanel() {
		RepositoryPanel panel = new RepositoryPanel();
		panel.setRefreshTime(Kernel.getInstance().state.AUTO_REPOSITORY_REFRESH_TIME);
		return panel;
	}
	
	private PreferencesPanel getNavigatorPanel() {
		NavigatorPanel panel = new NavigatorPanel();
		panel.setShowFavorites(Kernel.getInstance().state.SHOW_FAVORITES_IN_NAVIGATOR);
		panel.setShowAlbumToolTip(Kernel.getInstance().state.SHOW_ALBUM_TOOLTIP);
		panel.setAlbumToolTipDelay(Kernel.getInstance().state.ALBUM_TOOLTIP_DELAY);
		return panel;
	}
	
	private PreferencesPanel getPlayerPanel() {
		PlayerPanel panel = new PlayerPanel();
		return panel;
	}
	
	private PreferencesPanel getOSDPanel() {
		OSDPanel panel = new OSDPanel();
		panel.setAnimateOSD(Kernel.getInstance().state.ANIMATE_OSD);
		panel.setOSDDuration(Kernel.getInstance().state.OSD_DURATION);
		return panel;
	}
	
	private PreferencesPanel getAudioScrobblerPanel() {
		AudioScrobblerPanel panel = new AudioScrobblerPanel();
		panel.setActivateAudioScrobbler(Kernel.getInstance().state.isUseAudioScrobbler());
		panel.setSavePictures(Kernel.getInstance().state.SAVE_PICTURE_FROM_AUDIO_SCROBBLER);
		return panel;
	}
	
	private PreferencesPanel getInternetPanel() {
		InternetPanel panel = new InternetPanel();
		internetPanel = panel;
		panel.setConfiguration(Kernel.getInstance().state.getProxy());
		return panel;
	}
	
	protected void addBindings() {
		dialog.getList().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				dialog.showPanel(dialog.getList().getSelectedIndex());
			}
		});
		dialog.getCancel().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
			}
		});
		dialog.getOk().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (noErrors()) {
					processPreferences();
					dialog.setVisible(false);
					updateApplication();
				}
			}
		});
	}

	boolean noErrors() {
		return internetPanel.getResult() != null;
	}
	
	void processPreferences() {
		// Fetch result from panels
		HashMap<String, Object> result = new HashMap<String, Object>();	
		for (PreferencesPanel p: panels)
			result.putAll(p.getResult());
		
		// Set properties
		ApplicationState state = Kernel.getInstance().state;
		state.SHOW_TITLE = (Boolean) result.get(LanguageTool.getString("SHOW_TITLE"));
		state.setMultipleWindow(result.get(LanguageTool.getString("WINDOW_TYPE")).equals(LanguageTool.getString("MULTIPLE_WINDOW")));
		state.setLanguage(LanguageTool.getLanguageFileByName((String)result.get(LanguageTool.getString("LANGUAGE"))));
		
		HandlerProxy.getVisualHandler().repaint();
		
		state.setShowSystemTray((Boolean) result.get(LanguageTool.getString("SHOW_TRAY_ICON")));
		state.setShowTrayPlayer((Boolean) result.get(LanguageTool.getString("SHOW_TRAY_PLAYER")));
		state.setTheme((String)result.get(LanguageTool.getString("THEME")));
		
		state.AUTO_REPOSITORY_REFRESH_TIME = (Integer) result.get(LanguageTool.getString("REPOSITORY_REFRESH_TIME"));
		state.SHOW_FAVORITES_IN_NAVIGATOR = (Boolean) result.get(LanguageTool.getString("SHOW_FAVORITES"));
		state.SHOW_ALBUM_TOOLTIP = (Boolean) result.get(LanguageTool.getString("SHOW_ALBUM_TOOLTIP"));
		state.ALBUM_TOOLTIP_DELAY = (Integer) result.get(LanguageTool.getString("ALBUM_TOOLTIP_DELAY"));
		state.ANIMATE_OSD = (Boolean) result.get(LanguageTool.getString("ANIMATE_OSD"));
		state.OSD_DURATION = (Integer) result.get(LanguageTool.getString("OSD_DURATION"));
		state.setUseAudioScrobbler((Boolean) result.get(LanguageTool.getString("ACTIVATE_AUDIO_SCROBBLER")));
		state.SAVE_PICTURE_FROM_AUDIO_SCROBBLER = (Boolean) result.get(LanguageTool.getString("SAVE_PICTURES_TO_AUDIO_FOLDERS"));
		state.setProxy((ProxyBean) result.get("PROXY"));
	}
	
	void updateApplication() {
		HandlerProxy.getVisualHandler().showAudioScrobblerPanel(Kernel.getInstance().state.isUseAudioScrobbler(), true);
		HandlerProxy.getAudioScrobblerServiceHandler().updateService(Kernel.getInstance().state.getProxy());
		HandlerProxy.getSystemTrayHandler().setTrayIconVisible(Kernel.getInstance().state.isShowSystemTray());
		HandlerProxy.getSystemTrayHandler().setTrayPlayerVisible(Kernel.getInstance().state.isShowTrayPlayer());
	}

	protected void addStateBindings() {
	}

	protected void notifyReload() {
	}

}
