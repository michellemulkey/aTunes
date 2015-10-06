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

package net.sourceforge.atunes.kernel.controllers.navigation;

import javax.swing.JComponent;

import net.sourceforge.atunes.kernel.handlers.RepositoryHandler.SortType;

public class NavigationControllerState {

	private boolean showArtist = true;
	private SortType sortType;
	private String currentFilter;
	private int navigationView;
	private JComponent popupmenuCaller;

	public boolean isShowArtist() {
		return showArtist;
	}

	public void setShowArtist(boolean showArtist) {
		this.showArtist = showArtist;
	}
	
	public void setCurrentFilter(String currentFilter) {
		this.currentFilter = currentFilter;
	}

	public String getCurrentFilter() {
		return currentFilter;
	}

	public void setPopupmenuCaller(JComponent popupmenuCaller) {
		this.popupmenuCaller = popupmenuCaller;
	}

	public JComponent getPopupmenuCaller() {
		return popupmenuCaller;
	}
	
	public int getNavigationView() {
		return navigationView;
	}

	public void setNavigationView(int view) {
		navigationView = view;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
}
