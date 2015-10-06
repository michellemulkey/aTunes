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

package net.sourceforge.atunes.gui.views.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;


/**
 * @author fleax
 *
 */
public class PlayListPanel extends JPanel {

	private static final long serialVersionUID = -1886323054505118303L;

	private PlayListFilterPanel playListFilter; 
	private PlayListTable playListTable;
	private JScrollPane playListTableScroll;
	private PlayListControlsPanel playListControls;
	private PlayerControlsPanel playerControls;
	
	public PlayListPanel() {
		super(new GridBagLayout());
		addContent();
	}
	
	private void addContent() {
		playListFilter = new PlayListFilterPanel();
		// Hide by default
		playListFilter.setVisible(false);
		playListTable = new PlayListTable();
		playListTableScroll = new JScrollPane(playListTable);
		playListTableScroll.setBorder(BorderFactory.createEmptyBorder());
		playListControls = new PlayListControlsPanel();
		playerControls = new PlayerControlsPanel();

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;  c.insets = new Insets(1,1,0,1);
		add(playListFilter, c); 
		c.gridx = 0; c.gridy = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		add(playListTableScroll, c);
		c.gridx = 0; c.gridy = 2; c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(0,1,0,1);
		add(playListControls, c);
		c.gridy = 3;
		add(playerControls, c);
	}

	public PlayListTable getPlayListTable() {
		return playListTable;
	}

	public PlayerControlsPanel getPlayerControls() {
		return playerControls;
	}
	
	public PlayListControlsPanel getPlayListControls() {
		return playListControls;
	}

	public JScrollPane getPlayListTableScroll() {
		return playListTableScroll;
	}

	public PlayListFilterPanel getPlayListFilter() {
		return playListFilter;
	}
}
