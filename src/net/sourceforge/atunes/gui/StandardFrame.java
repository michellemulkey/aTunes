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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.gui.views.menu.ApplicationMenuBar;
import net.sourceforge.atunes.gui.views.panels.AudioScrobblerPanel;
import net.sourceforge.atunes.gui.views.panels.FilePropertiesPanel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.gui.views.panels.PlayListPanel;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.handlers.VisualHandler;


/**
 * @author fleax
 *
 */
public class StandardFrame extends JFrame implements net.sourceforge.atunes.gui.Frame {

	private static final long serialVersionUID = 1L;

	private transient VisualHandler visualHandler;
	
	private JSplitPane leftVerticalSplitPane;
	private JSplitPane rightVerticalSplitPane;
	private JLabel leftStatusBar;
	private JLabel centerStatusBar;
	private JLabel rightStatusBar;
	private JLabel statusBarImageLabel;
	private JProgressBar progressBar;
	private ApplicationMenuBar appMenuBar;
	private NavigationPanel navigationPanel;
	private PlayListPanel playListPanel;
	private FilePropertiesPanel propertiesPanel;
	private AudioScrobblerPanel audioScrobblerPanel;
	private JPanel statusBarPanel;
	private ToolBar toolBar; 

	public static int navigationPanelWidth = GuiUtils.getComponentWidthForResolution(1280, 280);
	public static int navigationPanelMinimumWidth = navigationPanelWidth - 80;
	public static int navigationPanelMaximumWidth = navigationPanelWidth + 50;
	public static int audioScrobblerPanelWidth = GuiUtils.getComponentWidthForResolution(1280, 280);
	public static int audioScrobblerMinimumWidth = audioScrobblerPanelWidth - 50;
	public static int filePropertiesPanelHeight = 100;
	public static int playListPanelWidth = GuiUtils.getComponentWidthForResolution(1280, 490);
	
	public static final int margin = 100;
	
	public StandardFrame(VisualHandler visualHandler) {
		super();
		this.visualHandler = visualHandler;
	}
	
	public void create() {
		// Set frame basic attributes
		setIconImage(ImageLoader.APP_ICON.getImage());
		setWindowSize();
		
		Point windowLocation = Kernel.getInstance().state.getWindowLocation();
		if (windowLocation != null)
			setLocation(windowLocation);
		else
			setLocationRelativeTo(null);
		
		// Set window state listener
		addWindowStateListener(visualHandler.getWindowStateListener());
		
		addComponentListener(visualHandler.getStandardWindowListener());
		
		// Create frame content
		setContentPane(getContentPanel());
	}
	
	public void setWindowSize() {
		if (Kernel.getInstance().state.isMaximized()) {
			Dimension screen = getToolkit().getScreenSize();
			setSize(screen.width - margin, screen.height - margin);
			setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		else {
			if (Kernel.getInstance().state.getWindowSize() != null) {
				setSize(Kernel.getInstance().state.getWindowSize());
			}
			else {
				Dimension screen = getToolkit().getScreenSize();
				setSize(screen.width - margin, screen.height - margin);
			}
		}
	}

	private Container getContentPanel() {
		// Main Container
		JPanel panel = new JPanel(new GridBagLayout());

		// Main Split Pane			
		leftVerticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		// Create menu bar
		appMenuBar = new ApplicationMenuBar();
		setJMenuBar(appMenuBar);

		GridBagConstraints c = new GridBagConstraints();

		// Navigation Panel
		leftVerticalSplitPane.add(getNavigationPanel());
		
		leftVerticalSplitPane.setResizeWeight(0.2);

		// Play List, File Properties, AudioScrobbler panel
		JPanel nonNavigatorPanel = new JPanel(new BorderLayout());
		rightVerticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		rightVerticalSplitPane.setBorder(BorderFactory.createEmptyBorder());
		rightVerticalSplitPane.setResizeWeight(1);

		JPanel centerPanel = new JPanel(new GridBagLayout());
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		centerPanel.add(getPlayListPanel(), c);
		c.gridy = 1; c.weightx = 1; c.weighty = 0; c.fill = GridBagConstraints.BOTH; 
		centerPanel.add(getPropertiesPanel(), c);
		
		rightVerticalSplitPane.add(centerPanel);
		rightVerticalSplitPane.add(getAudioScrobblerPanel());
		
		nonNavigatorPanel.add(rightVerticalSplitPane, BorderLayout.CENTER);
		
		leftVerticalSplitPane.add(nonNavigatorPanel);

		toolBar = new ToolBar();
		
		c.gridx = 0; c.gridy = 0;
		panel.add(toolBar, c);
		
		c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		panel.add(leftVerticalSplitPane, c);

		c.gridy = 2; c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(getStatusBarPanel(), c);
		
		return panel;
	}
	
	public ToolBar getToolBar() {
		return toolBar;
	}
	
	public JPanel getStatusBarPanel() {
		if (statusBarPanel == null) {
			statusBarPanel = new JPanel(new BorderLayout(30, 0));
			statusBarPanel.add(getLeftStatusBar(), BorderLayout.WEST);
			statusBarPanel.setPreferredSize(new Dimension(0,20));
			statusBarPanel.setMinimumSize(new Dimension(0,20));

			GridBagConstraints c = new GridBagConstraints();  
			JPanel centerPanel = new JPanel(new GridBagLayout());//new BorderLayout(30,0));
			centerPanel.setOpaque(false);
			c.gridx = 0; c.gridy = 0; c.weightx = 0.5; c.fill = GridBagConstraints.HORIZONTAL;
			centerPanel.add(getCenterStatusBar(), c);//BorderLayout.WEST);
			c.gridx = 1;
			centerPanel.add(getRightStatusBar(), c);//BorderLayout.CENTER);
			c.gridx = 2;
			centerPanel.add(getStatusBarImageLabel(), c);//BorderLayout.EAST);
			
			statusBarPanel.add(centerPanel, BorderLayout.CENTER);
			statusBarPanel.add(getProgressBar(), BorderLayout.EAST);
		}
		statusBarPanel.setVisible(false);
		return statusBarPanel;
	}
	
	private JLabel getLeftStatusBar() {
		if (leftStatusBar == null) {
			leftStatusBar = new JLabel(" ");
			leftStatusBar.setFont(FontSingleton.GENERAL_FONT);
		}
		return leftStatusBar;
	}

	private JLabel getCenterStatusBar() {
		if (centerStatusBar == null) {
			centerStatusBar = new JLabel(" ");
			centerStatusBar.setFont(FontSingleton.GENERAL_FONT);
		}
		return centerStatusBar;
	}

	private JLabel getRightStatusBar() {
		if (rightStatusBar == null) {
			rightStatusBar = new JLabel(" ");
			rightStatusBar.setFont(FontSingleton.GENERAL_FONT);
		}
		return rightStatusBar;
	}

	private JLabel getStatusBarImageLabel() {
		if (statusBarImageLabel == null) {
			statusBarImageLabel = new JLabel();
		}
		return statusBarImageLabel;
	}
	
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
		}
		return progressBar;
	}
	
	public NavigationPanel getNavigationPanel() {
		if (navigationPanel == null) {
			navigationPanel = new NavigationPanel();
			navigationPanel.setPreferredSize(new Dimension(navigationPanelWidth, 1));
			navigationPanel.setMinimumSize(new Dimension(navigationPanelMinimumWidth, 1));
			navigationPanel.setMaximumSize(new Dimension(navigationPanelMaximumWidth, 1));
			// If must be hidden, hide directly
			if (!Kernel.getInstance().state.isShowNavigationPanel())
				navigationPanel.setVisible(false);
		}
		return navigationPanel;
	}

	public PlayListPanel getPlayListPanel() {
		if (playListPanel == null) {
			playListPanel = new PlayListPanel();
			playListPanel.setMinimumSize(new Dimension(playListPanelWidth, 1));
			playListPanel.setPreferredSize(new Dimension(playListPanelWidth, 1));
		}
		return playListPanel;
	}

	public void dispose() {
		visualHandler.finish();
		super.dispose();
	}

	public ApplicationMenuBar getAppMenuBar() {
		return appMenuBar;
	}

	public JSplitPane getLeftVerticalSplitPane() {
		return leftVerticalSplitPane;
	}
	
	public void setLeftStatusBarText(String text) {
		getLeftStatusBar().setText(text);
	}
	
	public void setCenterStatusBar(String text) {
		getCenterStatusBar().setText(' ' + text);
	}
	
	public void setRightStatusBar(String text) {
		getRightStatusBar().setText(' ' + text);
	}
	
	public void setStatusBarImageLabelIcon(Icon icon) {
		getStatusBarImageLabel().setIcon(icon);
	}
	
	public void showStatusBarImageLabel(boolean visible) {
		getStatusBarImageLabel().setVisible(visible);
	}
	
	public void showProgressBar(boolean visible) {
		getProgressBar().setVisible(visible);
	}
	
	public JFrame getFrame() {
		return this;
	}
	
	public void showSongProperties(boolean show) {
		getPropertiesPanel().setVisible(show);
	}
	
	public void showNavigationTable(boolean show) {
		getNavigationPanel().getNavigationTableContainer().setVisible(show);
		if (show) {
			getNavigationPanel().getSplit().setDividerLocation(0.5);
		}
	}
	
	public void showAudioScrobblerPanel(boolean show, boolean changeSize) {
		boolean wasVisible = getAudioScrobblerPanel().isVisible();
		getAudioScrobblerPanel().setVisible(show);
		if (!wasVisible && show) {
			int playListWidth = playListPanel.getWidth();
			rightVerticalSplitPane.setDividerLocation(rightVerticalSplitPane.getSize().width - StandardFrame.audioScrobblerPanelWidth);
			playListWidth = playListWidth - StandardFrame.audioScrobblerPanelWidth;
			if (playListWidth < playListPanelWidth && changeSize) {
				int diff = playListPanelWidth - playListWidth;
				setSize(getSize().width + diff, getSize().height);
			}
		}
	}
	
	public void showNavigationPanel(boolean show, boolean changeSize) {
		getNavigationPanel().setVisible(show);
		if (show) {
			int playListWidth = playListPanel.getWidth();
			getLeftVerticalSplitPane().setDividerLocation(StandardFrame.navigationPanelWidth);
			playListWidth = playListWidth - StandardFrame.navigationPanelWidth;
			if (playListWidth < playListPanelWidth && changeSize) {
				int diff = playListPanelWidth - playListWidth;
				setSize(getSize().width + diff, getSize().height);
			}
		}
	}
	
	public PlayListTable getPlayListTable() {
		return getPlayListPanel().getPlayListTable();
	}

	public FilePropertiesPanel getPropertiesPanel() {
		if (propertiesPanel == null) {
			propertiesPanel = new FilePropertiesPanel();
			propertiesPanel.setPreferredSize(new Dimension(1, filePropertiesPanelHeight));
			if (!Kernel.getInstance().state.isShowSongProperties())
				propertiesPanel.setVisible(false);
		}
		return propertiesPanel;
	}

	public AudioScrobblerPanel getAudioScrobblerPanel() {
		if (audioScrobblerPanel == null) {
			audioScrobblerPanel = new AudioScrobblerPanel();
			audioScrobblerPanel.setPreferredSize(new Dimension(audioScrobblerPanelWidth, 1));
			audioScrobblerPanel.setMinimumSize(new Dimension(audioScrobblerMinimumWidth, 1));
			if (!Kernel.getInstance().state.isUseAudioScrobbler())
				audioScrobblerPanel.setVisible(false);
		}
		return audioScrobblerPanel;
	}
	
	public void showStatusBar(boolean show) {
		getStatusBarPanel().setVisible(show);
	}
}
