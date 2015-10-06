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

package net.sourceforge.atunes.kernel.controllers.playListControls;

import net.sourceforge.atunes.gui.views.panels.PlayListControlsPanel;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.model.PanelController;

public class PlayListControlsController extends PanelController {

	public PlayListControlsController(PlayListControlsPanel panel) {
		super(panel);
		addBindings();
		addStateBindings();
	}
	
	protected void addStateBindings() {
		disablePlayListControls(true);
		((PlayListControlsPanel) panelControlled).getShowAlbum().setSelected(Kernel.getInstance().state.isShowAlbumInPlayList());
		((PlayListControlsPanel) panelControlled).getShowArtist().setSelected(Kernel.getInstance().state.isShowArtistInPlayList());
		((PlayListControlsPanel) panelControlled).getShowGenre().setSelected(Kernel.getInstance().state.isShowGenreInPlayList());
		((PlayListControlsPanel) panelControlled).getShowTrack().setSelected(Kernel.getInstance().state.isShowTrackInPlayList());
	}
	
	protected void addBindings() {
		final PlayListControlsPanel panel = (PlayListControlsPanel) panelControlled;
		
		PlayListControlsListener listener = new PlayListControlsListener(panel);
		
		panel.getSortByTrack().addActionListener(listener);
		panel.getSortByTitle().addActionListener(listener);
		panel.getSortByArtist().addActionListener(listener);
		panel.getSortByAlbum().addActionListener(listener);
		panel.getSortByGenre().addActionListener(listener);
		panel.getSavePlaylistButton().addActionListener(listener);
		panel.getLoadPlaylistButton().addActionListener(listener);
		panel.getTopButton().addActionListener(listener);
		panel.getUpButton().addActionListener(listener);
		panel.getDeleteButton().addActionListener(listener);
		panel.getDownButton().addActionListener(listener);
		panel.getBottomButton().addActionListener(listener);
		panel.getInfoButton().addActionListener(listener);
		panel.getClearButton().addActionListener(listener);
		panel.getFavoriteSong().addActionListener(listener);
		panel.getFavoriteAlbum().addActionListener(listener);
		panel.getFavoriteArtist().addActionListener(listener);
		panel.getShowTrack().addActionListener(listener);
		panel.getShowArtist().addActionListener(listener);
		panel.getShowGenre().addActionListener(listener);
		panel.getShowAlbum().addActionListener(listener);
		panel.getArtistButton().addActionListener(listener);
		panel.getAlbumButton().addActionListener(listener);
	}

	protected void notifyReload() {}
	
	public void disablePlayListControls(boolean disable) {
		final PlayListControlsPanel panel = (PlayListControlsPanel) panelControlled;
		panel.getInfoButton().setEnabled(!disable);
		panel.getDeleteButton().setEnabled(!disable);
		panel.getClearButton().setEnabled(true);
		panel.getTopButton().setEnabled(!disable);
		panel.getUpButton().setEnabled(!disable);
		panel.getDownButton().setEnabled(!disable);
		panel.getBottomButton().setEnabled(!disable);
		panel.getFavoritePopup().setEnabled(!disable);
		panel.getArtistButton().setEnabled(!disable);
		panel.getAlbumButton().setEnabled(!disable);
	}
	
	public void enableSaveButton(boolean enable) {
		((PlayListControlsPanel) panelControlled).getSavePlaylistButton().setEnabled(enable);
		
	}
}
