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

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.gui.model.NavigationTableModel;
import net.sourceforge.atunes.gui.views.RendererSingleton;
import net.sourceforge.atunes.gui.views.dialogs.AlbumToolTip;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.model.PanelController;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationMenusActionListener;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationTabbedPaneChangeListener;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationTableControlsActionListener;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationTableMouseListener;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationTreeControlsActionListener;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationTreeMouseListener;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationTreeSelectionListener;
import net.sourceforge.atunes.kernel.controllers.navigation.listeners.NavigationTreeToolTipListener;
import net.sourceforge.atunes.kernel.handlers.RepositoryHandler;
import net.sourceforge.atunes.kernel.handlers.RepositoryHandler.SortType;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.model.info.Folder;


/**
 * @author fleax
 *
 */
public class NavigationController extends PanelController {

	NavigationPanel panel;
	NavigationControllerState state;
	private NodeToSongsTranslator nodeTranslator;
	
	private Object lastAlbumToolTipContent;
	private AlbumToolTip albumToolTip;
	
	public NavigationController(NavigationPanel panel) {
		super(panel);
		this.panel = panel;
		state = new NavigationControllerState();
		nodeTranslator = new NodeToSongsTranslator(state);
		addBindings();
		addStateBindings();
	}
	
	protected void addStateBindings() {
		state.setShowArtist(true);
		panel.getShowArtist().setSelected(true);
		state.setSortType(SortType.BY_TRACK);
		panel.getSortByTrack().setSelected(true);
		
		panel.getNavigationTableAddButton().setEnabled(false);
		panel.getNavigationTableInfoButton().setEnabled(false);
	}
	
	protected void addBindings() {
		// Keys
		panel.getNonFavoriteAddToPlaylistMenuItem().setAccelerator(KeyStroke.getKeyStroke('+'));
		panel.getNonFavoriteSetAsPlaylistMenuItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
		// End keys
		
		panel.getNavigationTable().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				adjustColumnsWidth();
			}
		});
		
		panel.getNavigationTable().setModel(new NavigationTableModel(this));
		adjustColumnsWidth();
		
		NavigationTreeSelectionListener treeSelectionListener = new NavigationTreeSelectionListener(this, panel);
		panel.getNavigationTree().addTreeSelectionListener(treeSelectionListener);
		panel.getFileNavigationTree().addTreeSelectionListener(treeSelectionListener);
		panel.getFavoritesTree().addTreeSelectionListener(treeSelectionListener);
		panel.getDeviceTree().addTreeSelectionListener(treeSelectionListener);

		NavigationTreeMouseListener treeMouseListener = new NavigationTreeMouseListener(this, panel);
		panel.getNavigationTree().addMouseListener(treeMouseListener);
		panel.getFileNavigationTree().addMouseListener(treeMouseListener);
		panel.getFavoritesTree().addMouseListener(treeMouseListener);
		panel.getDeviceTree().addMouseListener(treeMouseListener);
		
		NavigationTreeToolTipListener tooltipListener = new NavigationTreeToolTipListener(this, panel);
		panel.getNavigationTree().addMouseMotionListener(tooltipListener);
		panel.getNavigationTreeScrollPane().addMouseWheelListener(tooltipListener);
		
		panel.getNavigationTable().addMouseListener(new NavigationTableMouseListener(this, panel));
		panel.getTabbedPane().addChangeListener(new NavigationTabbedPaneChangeListener(this, panel));
		
		NavigationTreeControlsActionListener treeControlsActionListener = new NavigationTreeControlsActionListener(this, panel);
		panel.getCollapseTree().addActionListener(treeControlsActionListener);
		panel.getExpandTree().addActionListener(treeControlsActionListener);
		panel.getFilterTextField().addActionListener(treeControlsActionListener);
		panel.getFilterTextField().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					panel.getFilterTextField().setText("");
					state.setCurrentFilter(null);
					refreshTreeContent();
				}
			}
		});
		panel.getClearFilterButton().addActionListener(treeControlsActionListener);
		panel.getAddToPlayList().addActionListener(treeControlsActionListener);
    	panel.getShowArtist().addActionListener(treeControlsActionListener);
    	panel.getShowAlbum().addActionListener(treeControlsActionListener);
		
    	NavigationTableControlsActionListener tableControlsActionListener = new NavigationTableControlsActionListener(this, panel);
    	panel.getNavigationTableInfoButton().addActionListener(tableControlsActionListener);
    	panel.getNavigationTableAddButton().addActionListener(tableControlsActionListener);
    	panel.getSortByTrack().addActionListener(tableControlsActionListener);
    	panel.getSortByFile().addActionListener(tableControlsActionListener);
    	panel.getSortByTitle().addActionListener(tableControlsActionListener);
    	
    	NavigationMenusActionListener menusActionListener = new NavigationMenusActionListener(this, panel);
    	panel.getAddToPlaylistMenuItem().addActionListener(menusActionListener);
    	panel.getSetAsPlaylistMenuItem().addActionListener(menusActionListener);
    	panel.getRemoveFromFavoritesMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteAddToPlaylistMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteSetAsPlaylistMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteSetAsFavoriteSongMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteSetAsFavoriteAlbumMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteSetAsFavoriteArtistMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteEditTagMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteEditTitlesMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteClearTagMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteExtractPictureMenuItem().addActionListener(menusActionListener);
    	panel.getDeviceCopyToRepositoryMenuItem().addActionListener(menusActionListener);
    	panel.getNonFavoriteSearch().addActionListener(menusActionListener);
    	panel.getNonFavoriteSearchAt().addActionListener(menusActionListener);
	}
	
	public void refreshTreeContent() {
		refreshTagViewTreeContent();
		refreshFileViewTreeContent();
		refreshDeviceTreeContent();
	}
	
	public void refreshTagViewTreeContent() { 
		HashMap<String, Artist> data = HandlerProxy.getRepositoryHandler().getArtistAndAlbumStructure();
		TagViewRefresher.refresh(data, panel.getNavigationTreeModel(), state.isShowArtist(), state.getCurrentFilter());
		panel.getNavigationTree().expandRow(0);
		panel.getNavigationTree().setSelectionRow(0);
	}
	
	private void refreshFileViewTreeContent() {
		HashMap<String, Folder> data = HandlerProxy.getRepositoryHandler().getFolderStructure();
		FileViewRefresher.refresh(data, panel.getFileNavigationTreeModel(), state.getCurrentFilter(), HandlerProxy.getRepositoryHandler().getRepositoryPath());
		panel.getFileNavigationTree().expandRow(0);
		panel.getFileNavigationTree().setSelectionRow(0);
	}
	
	public void refreshFavoriteTree() {
		HashMap<String, Artist> artists = HandlerProxy.getRepositoryHandler().getFavoriteArtistsInfo();
		HashMap<String, Album> albums = HandlerProxy.getRepositoryHandler().getFavoriteAlbumsInfo();
		FavoriteViewRefresher.refresh(panel.getFavoritesTree(), artists, albums, panel.getFavoritesTreeModel());
		panel.getFavoritesTree().expandRow(0);
		panel.getFavoritesTree().setSelectionRow(0);
	}
	
	public void refreshDeviceTreeContent() {
		HashMap<String, ?> data;
		if (Kernel.getInstance().state.isSortDeviceByTag()) {
			panel.getDeviceTree().setCellRenderer(RendererSingleton.DEVICE_BY_TAG_TREE_RENDERER);
			data = HandlerProxy.getRepositoryHandler().getDeviceArtistAndAlbumStructure();
		}
		else {
			panel.getDeviceTree().setCellRenderer(RendererSingleton.DEVICE_BY_FOLDER_TREE_RENDERER);
			data = HandlerProxy.getRepositoryHandler().getDeviceFolderStructure();
		}
		
		if (state.getNavigationView() == NavigationControllerViews.DEVICE_VIEW) {
			panel.getShowAlbum().setEnabled(HandlerProxy.getRepositoryHandler().getDeviceRepository() != null && Kernel.getInstance().state.isSortDeviceByTag());
			panel.getShowArtist().setEnabled(HandlerProxy.getRepositoryHandler().getDeviceRepository() != null && Kernel.getInstance().state.isSortDeviceByTag());
		}
		
		DeviceViewRefresher.refresh(data, panel.getDeviceTreeModel(), state.isShowArtist(), state.getCurrentFilter(), Kernel.getInstance().state.isSortDeviceByTag());
		panel.getDeviceTree().expandRow(0);
		panel.getDeviceTree().setSelectionRow(0);
		
		adjustColumnsWidth();
	}
	
	public DefaultTreeModel getNavigationTreeModel() {
		return panel.getNavigationTreeModel();
	}
	
	public DefaultTreeModel getFolderTreeModel() {
		return panel.getFileNavigationTreeModel();
	}
	
	public DefaultTreeModel getFavoritesTreeModel() {
		return panel.getFavoritesTreeModel();
	}
	
	public ArrayList<AudioFile> getSongsForNavigationTree() {
		TreePath[] paths = panel.getNavigationTree().getSelectionPaths();
		return nodeTranslator.getSongsForNavigationTree(paths);
	}
	
	public void notifyReload() {
		refreshTreeContent();
		refreshFavoriteTree();
		adjustColumnsWidth();
	}
	
	public void notifyDeviceReload() {
		refreshDeviceTreeContent();
	}
	
	public ArrayList<AudioFile> getSongsForTreeNode(DefaultMutableTreeNode node) {
		return nodeTranslator.getSongsForTreeNode(node);
	}
	
	public ArrayList<AudioFile> getSongsForDeviceTreeNode(DefaultMutableTreeNode node) {
		return nodeTranslator.getSongsForDeviceTreeNode(node);
	}
	
	public ArrayList<AudioFile> getSongsForFavoriteTreeNode(DefaultMutableTreeNode node) {
		return nodeTranslator.getSongsForFavoriteTreeNode(node);
	}
	
	public void setNavigationView(int view) {
		state.setNavigationView(view);
		Kernel.getInstance().state.setNavigationView(view);
		panel.getTabbedPane().setSelectedIndex(view);
		
		panel.getShowAlbum().setEnabled(view == NavigationControllerViews.TAG_VIEW);
		panel.getShowArtist().setEnabled(view == NavigationControllerViews.TAG_VIEW);
		
		if (view == NavigationControllerViews.FAVORITE_VIEW){
			panel.getFilterLabel().setEnabled(false);
			panel.getFilterTextField().setEnabled(false);
			panel.getClearFilterButton().setEnabled(false);
		}
		else {
			panel.getFilterLabel().setEnabled(true);
			panel.getFilterTextField().setEnabled(true);
			panel.getClearFilterButton().setEnabled(true);
		}
	}

	public void setTreeView(int view) {
		panel.getTabbedPane().setSelectedIndex(view);
	}
	
	public AudioFile getSongInNavigationTable(int row) {
		return ((NavigationTableModel)panel.getNavigationTable().getModel()).getSongAt(row);
	}

	public boolean isDeviceAttached() {
		return HandlerProxy.getRepositoryHandler().getDeviceRepository() != null;
	}
	
	public boolean isSortDeviceByTag() {
		return Kernel.getInstance().state.isSortDeviceByTag();
	}
	
	public ArrayList<AudioFile> sort(ArrayList<AudioFile> songs, SortType type) {
		return HandlerProxy.getRepositoryHandler().sort(songs, type);
	}

	public RepositoryHandler getRepositoryHandler() {
		return HandlerProxy.getRepositoryHandler();
	}
	
	public void setVisible() {
		panel.getSplit().setDividerLocation(0.5);
	}

	public NavigationControllerState getState() {
		return state;
	}
	
	public AlbumToolTip getAlbumToolTip() {
		if (albumToolTip == null)
			albumToolTip = new AlbumToolTip();
		return albumToolTip;
	}

	public Object getLastAlbumToolTipContent() {
		return lastAlbumToolTipContent;
	}

	public void setLastAlbumToolTipContent(Object lastAlbumToolTipContent) {
		this.lastAlbumToolTipContent = lastAlbumToolTipContent;
	}
	
	public void adjustColumnsWidth() {
		JTable table = panel.getNavigationTable();
		
		if (table.getColumnModel().getColumnCount() < 3)
			return;
		
		int favoriteColumnWidth = 18;
		int durationColumnWidth = 50;

		table.getColumnModel().getColumn(0).setMinWidth(favoriteColumnWidth);
		table.getColumnModel().getColumn(0).setMaxWidth(favoriteColumnWidth);
		
		table.getColumnModel().getColumn(2).setMinWidth(durationColumnWidth);
		table.getColumnModel().getColumn(2).setMaxWidth(durationColumnWidth);
		
		table.invalidate();
		HandlerProxy.getVisualHandler().repaint();
	}

}
