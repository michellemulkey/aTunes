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

import javax.swing.Icon;
import javax.swing.JFrame;

import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.gui.views.menu.ApplicationMenuBar;
import net.sourceforge.atunes.gui.views.panels.FilePropertiesPanel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.gui.views.panels.AudioScrobblerPanel;
import net.sourceforge.atunes.gui.views.panels.PlayListPanel;


public interface Frame {

	public void create();
	public void setVisible(boolean visible);
	public boolean isVisible();
	public void setLeftStatusBarText(String text);
	public void setCenterStatusBar(String text);
	public void setRightStatusBar(String text);
	public void setStatusBarImageLabelIcon(Icon icon);
	public void showStatusBarImageLabel(boolean visible);
	public void showProgressBar(boolean visible);
	public void setTitle(String title);
	public JFrame getFrame();
	public void showSongProperties(boolean show);
	public void showNavigationTable(boolean show);
	public Point getLocation();
	public void setLocation(Point location);
	public void setLocationRelativeTo(Component c);
	public int getExtendedState();
	public void setExtendedState(int state);
	public void showAudioScrobblerPanel(boolean show, boolean changeSize);
	public void showNavigationPanel(boolean show, boolean changeSize);
	public PlayListTable getPlayListTable();
	public void setDefaultCloseOperation(int op);
	public FilePropertiesPanel getPropertiesPanel();
	public ApplicationMenuBar getAppMenuBar();
	public ToolBar getToolBar();
	public NavigationPanel getNavigationPanel();
	public PlayListPanel getPlayListPanel();
	public AudioScrobblerPanel getAudioScrobblerPanel();
	public Dimension getSize();
	public void showStatusBar(boolean show);
	
}
