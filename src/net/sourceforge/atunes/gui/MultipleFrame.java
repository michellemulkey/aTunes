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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.JFrame;

import net.sourceforge.atunes.gui.dockableView.CloseListener;
import net.sourceforge.atunes.gui.dockableView.DockableDialog;
import net.sourceforge.atunes.gui.dockableView.DockableFrame;
import net.sourceforge.atunes.gui.dockableView.DockableFrameController;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.gui.views.menu.ApplicationMenuBar;
import net.sourceforge.atunes.gui.views.panels.FilePropertiesPanel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.gui.views.panels.AudioScrobblerPanel;
import net.sourceforge.atunes.gui.views.panels.PlayListPanel;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.handlers.VisualHandler;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class MultipleFrame implements Frame {

	private static final Dimension frameDimension = new Dimension(500,400);
	
	private static final Dimension navigatorDimension = new Dimension(300,689);
	private static final Dimension filePropertiesDimension = new Dimension(500,130);
	private static final Dimension audioScrobblerDimension = new Dimension(300,689);
	
	VisualHandler visualHandler;
	private DockableFrameController dockableController;

	private DockableFrame frame;
	private DockableDialog navigatorDialog;
	private DockableDialog filePropertiesDialog;
	private DockableDialog audioScrobblerDialog;
	
	private ApplicationMenuBar menuBar; // NOT USED
	private ToolBar toolBar; // NOT USED
	private NavigationPanel navigationPanel;
	private PlayListPanel playListPanel;
	private FilePropertiesPanel filePropertiesPanel;
	private AudioScrobblerPanel audioScrobblerPanel;
	
	public MultipleFrame(VisualHandler vHandler) {
		this.visualHandler = vHandler;
		dockableController = new DockableFrameController(new CloseListener() {
			public void close() {
				visualHandler.finish();
			}
			public void minimize() {
			}
		});
	}
	
	public void create() {
		// Created but not used
		menuBar = new ApplicationMenuBar();
		toolBar = new ToolBar();

		frame = dockableController.getNewFrame("", frameDimension.width, frameDimension.height, null, DockableFrameController.NONE, frameDimension);

		Point p = Kernel.getInstance().state.getMultipleViewLocation();
		if (p == null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation((screenSize.width - frameDimension.width) / 2, 100);
		}
		else {
			frame.setLocation(p.x, p.y);
		}
		
		Dimension d = Kernel.getInstance().state.getMutipleViewSize();
		if (d != null) {
			frame.setSize(d);
		}

		
		addContentToFrame();
		
		navigatorDialog = dockableController.getNewDialog(frame, LanguageTool.getString("NAVIGATOR"), navigatorDimension.width, navigatorDimension.height, frame, DockableFrameController.WEST, navigatorDimension);
		addContentToNavigator();
		
		filePropertiesDialog = dockableController.getNewDialog(frame, LanguageTool.getString("PROPERTIES"), filePropertiesDimension.width, filePropertiesDimension.height, frame, DockableFrameController.SOUTH, filePropertiesDimension);
		addContentToFileProperties();
		
		audioScrobblerDialog = dockableController.getNewDialog(frame, LanguageTool.getString("AUDIO_SCROBBLER"), audioScrobblerDimension.width, audioScrobblerDimension.height, frame, DockableFrameController.EAST, audioScrobblerDimension);
		addContentToOpenStrands();
		
	}
	
	public JFrame getFrame() {
		return frame.getFrame();
	}
	
	public ApplicationMenuBar getAppMenuBar() {
		return menuBar;
	}
	
	public ToolBar getToolBar() {
		return toolBar;
	}
	
	public int getExtendedState() {
		return frame.getExtendedState();
	}
	
	public FilePropertiesPanel getPropertiesPanel() {
		return filePropertiesPanel;
	}
	
	public Point getLocation() {
		return frame.getLocation();
	}
	
	public NavigationPanel getNavigationPanel() {
		return navigationPanel;
	}
	
	public PlayListPanel getPlayListPanel() {
		return playListPanel;
	}
	
	public PlayListTable getPlayListTable() {
		return playListPanel.getPlayListTable();
	}
	
	public AudioScrobblerPanel getAudioScrobblerPanel() {
		return audioScrobblerPanel;
	}
	
	public boolean isVisible() {
		return frame.isVisible();
	}
	
	public void setCenterStatusBar(String text) {
	}
	
	public void setDefaultCloseOperation(int op) {
		frame.setDefaultCloseOperation(op);
		navigatorDialog.setDefaultCloseOperation(op);
		filePropertiesDialog.setDefaultCloseOperation(op);
		audioScrobblerDialog.setDefaultCloseOperation(op);
	}
	
	public void setExtendedState(int state) {
		//frame.setExtendedState(state);
	}
	
	public void setLeftStatusBarText(String text) {
	}
	
	public void setLocation(Point location) {
		//frame.setLocation(location.x, location.y);
	}
	
	public void setLocationRelativeTo(Component c) {
		frame.setLocationRelativeTo(c);
	}
	
	public void setRightStatusBar(String text) {
	}
	
	public void setStatusBarImageLabelIcon(Icon icon) {
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
		if (!visible || Kernel.getInstance().state.isShowNavigationPanel())
			navigatorDialog.setVisible(visible);
		if (!visible || Kernel.getInstance().state.isShowSongProperties())
			filePropertiesDialog.setVisible(visible);
		if (!visible || Kernel.getInstance().state.isUseAudioScrobbler())
			audioScrobblerDialog.setVisible(visible);
	}
	
	public void showNavigationPanel(boolean show, boolean changeSize) {
		navigatorDialog.setVisible(show);
	}
	
	public void showNavigationTable(boolean show) {
		navigationPanel.getNavigationTableContainer().setVisible(show);
		if (show) {
			navigationPanel.getSplit().setDividerLocation(0.5);
		}
	}
	
	public void showAudioScrobblerPanel(boolean show, boolean changeSize) {
		audioScrobblerDialog.setVisible(show);
	}
	
	public void showProgressBar(boolean visible) {
	}
	
	public void showSongProperties(boolean show) {
		filePropertiesDialog.setVisible(show);
	}
	
	public void showStatusBarImageLabel(boolean visible) {
	}
	
	private void addContentToFrame() {
		frame.setIcon(ImageLoader.APP_ICON.getImage());
		playListPanel = new PlayListPanel();
		frame.addContent(playListPanel);
		frame.setMenu(menuBar.getMenuAsPopupMenu());
	}
	
	private void addContentToNavigator() {
		navigationPanel = new NavigationPanel();
		navigatorDialog.addContent(navigationPanel);
	}
	
	private void addContentToFileProperties() {
		filePropertiesPanel = new FilePropertiesPanel();
		filePropertiesDialog.addContent(filePropertiesPanel);
	}
	
	private void addContentToOpenStrands() {
		audioScrobblerPanel = new AudioScrobblerPanel();
		audioScrobblerDialog.addContent(audioScrobblerPanel);
	}

	public Dimension getSize() {
		return frame.getFrame().getSize();
	}
	
	public void showStatusBar(boolean show) {
	}
}
