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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.RendererSingleton;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.DragSourceTable;
import net.sourceforge.atunes.gui.views.controls.DragSourceTree;
import net.sourceforge.atunes.gui.views.controls.PopUpButton;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.utils.TimeUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.jvnet.substance.SubstanceDefaultTableCellRenderer;


/**
 * @author fleax
 *
 */
public class NavigationPanel extends JPanel {

	private static final long serialVersionUID = -2900418193013495812L;

	private JTree navigationTree;
	private JScrollPane navigationTreeScrollPane;
	private DragSourceTable navigationTable;
	private JPanel navigationTableButtonPanel;
	private DefaultTreeModel navigationTreeModel;
	private CustomButton navigationTableInfoButton;
	private CustomButton navigationTableAddButton;
	private JRadioButtonMenuItem sortByTrack;
	private JRadioButtonMenuItem sortByFile;
	private JRadioButtonMenuItem sortByTitle;
	private JPanel navigationTableContainer;
	
	private JTree fileNavigationTree;
	private DefaultTreeModel fileNavigationTreeModel;
	
	private JTree favoritesTree;
	private DefaultTreeModel favoritesTreeModel;
	private JTabbedPane tabbedPane;
	
	private JTree deviceTree;
	private DefaultTreeModel deviceTreeModel;
	
	private JSplitPane split;
	
	private PopUpButton prefsButton;
	private JRadioButtonMenuItem showArtist;
	private JRadioButtonMenuItem showAlbum;
	
	private CustomButton addToPlayList;
	
	private JMenuItem expandTree;
	private JMenuItem collapseTree;
	
	private JLabel filterLabel;
	private JTextField filterTextField;
	private CustomButton clearFilterButton;
	
	private JPopupMenu favoriteMenu;
	private JMenuItem addToPlaylistMenuItem;
	private JMenuItem setAsPlaylistMenuItem;
	private JMenuItem removeFromFavoritesMenuItem;
	
	private JPopupMenu nonFavoriteMenu;
	private JMenuItem nonFavoriteAddToPlaylistMenuItem;
	private JMenuItem nonFavoriteSetAsPlaylistMenuItem;
	private JMenuItem nonFavoriteSetAsFavoriteSongMenuItem;
	private JMenuItem nonFavoriteSetAsFavoriteAlbumMenuItem;
	private JMenuItem nonFavoriteSetAsFavoriteArtistMenuItem;
	private JMenuItem nonFavoriteEditTagMenuItem;
	private JMenuItem nonFavoriteClearTagMenuItem;
	private JMenuItem nonFavoriteExtractPictureMenuItem;
	private JMenuItem nonFavoriteEditTitlesMenuItem;
	private JMenuItem nonFavoriteSearch;
	private JMenuItem nonFavoriteSearchAt;
	
	private JPopupMenu deviceMenu;
	private JMenuItem deviceCopyToRepositoryMenuItem;
	
	public NavigationPanel() {
		super(new BorderLayout(), true);
		addContent();
	}
	
	private void addContent() {
		JPanel treePanel = new JPanel(new BorderLayout());
		navigationTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(LanguageTool.getString("REPOSITORY")));
		navigationTree = new DragSourceTree(navigationTreeModel);
		navigationTree.setToggleClickCount(0);
		navigationTree.setCellRenderer(RendererSingleton.NAVIGATION_TREE_RENDERER);
		ToolTipManager.sharedInstance().registerComponent(navigationTree);
		navigationTreeScrollPane = new JScrollPane(navigationTree);
		
		fileNavigationTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(LanguageTool.getString("FOLDERS")));
		fileNavigationTree = new DragSourceTree(fileNavigationTreeModel);
		fileNavigationTree.setToggleClickCount(0);
		fileNavigationTree.setCellRenderer(RendererSingleton.FILE_TREE_RENDERER);
		JScrollPane scrollPane3 = new JScrollPane(fileNavigationTree);
		
		favoritesTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(LanguageTool.getString("FAVORITES")));
		favoritesTree = new DragSourceTree(favoritesTreeModel);
		favoritesTree.setToggleClickCount(0);
		favoritesTree.setCellRenderer(RendererSingleton.FAVORITE_TREE_RENDERER);
		JScrollPane scrollPane4 = new JScrollPane(favoritesTree);
		
		deviceTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(LanguageTool.getString("DEVICE")));
		deviceTree = new DragSourceTree(deviceTreeModel);
		deviceTree.setToggleClickCount(0);
		deviceTree.setCellRenderer(RendererSingleton.DEVICE_BY_TAG_TREE_RENDERER);
		JScrollPane scrollPane5 = new JScrollPane(deviceTree);
		
		favoriteMenu = new JPopupMenu();
		addToPlaylistMenuItem = new JMenuItem(LanguageTool.getString("ADD_TO_PLAYLIST"));
		setAsPlaylistMenuItem = new JMenuItem(LanguageTool.getString("SET_AS_PLAYLIST"));
		setAsPlaylistMenuItem.setFont(FontSingleton.GENERAL_FONT);
		removeFromFavoritesMenuItem = new JMenuItem(LanguageTool.getString("REMOVE_FROM_FAVORITES"));
		removeFromFavoritesMenuItem.setFont(FontSingleton.GENERAL_FONT);
		favoriteMenu.add(addToPlaylistMenuItem);
		favoriteMenu.add(setAsPlaylistMenuItem);
		favoriteMenu.add(removeFromFavoritesMenuItem);
		
		nonFavoriteMenu = new JPopupMenu();
		nonFavoriteAddToPlaylistMenuItem = new JMenuItem(LanguageTool.getString("ADD_TO_PLAYLIST"));
		nonFavoriteSetAsPlaylistMenuItem = new JMenuItem(LanguageTool.getString("SET_AS_PLAYLIST"));
		nonFavoriteSetAsPlaylistMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteEditTagMenuItem = new JMenuItem(LanguageTool.getString("EDIT_TAG"));
		nonFavoriteEditTagMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteEditTitlesMenuItem = new JMenuItem(LanguageTool.getString("EDIT_TITLES"));
		nonFavoriteEditTitlesMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteClearTagMenuItem = new JMenuItem(LanguageTool.getString("CLEAR_TAG"));
		nonFavoriteClearTagMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteExtractPictureMenuItem = new JMenuItem(LanguageTool.getString("EXTRACT_PICTURE"));
		nonFavoriteExtractPictureMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteSetAsFavoriteSongMenuItem = new JMenuItem(LanguageTool.getString("SET_FAVORITE_SONG"));
		nonFavoriteSetAsFavoriteSongMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteSetAsFavoriteAlbumMenuItem = new JMenuItem(LanguageTool.getString("SET_FAVORITE_ALBUM"));
		nonFavoriteSetAsFavoriteAlbumMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteSetAsFavoriteArtistMenuItem = new JMenuItem(LanguageTool.getString("SET_FAVORITE_ARTIST"));
		nonFavoriteSetAsFavoriteArtistMenuItem.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteSearch = new JMenuItem(LanguageTool.getString("SEARCH_ARTIST"));
		nonFavoriteSearch.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteSearchAt = new JMenuItem(LanguageTool.getString("SEARCH_ARTIST_AT") + "...");
		nonFavoriteSearchAt.setFont(FontSingleton.GENERAL_FONT);
		nonFavoriteMenu.add(nonFavoriteAddToPlaylistMenuItem);
		nonFavoriteMenu.add(nonFavoriteSetAsPlaylistMenuItem);
		nonFavoriteMenu.add(new JSeparator());
		nonFavoriteMenu.add(nonFavoriteEditTagMenuItem);
		nonFavoriteMenu.add(nonFavoriteEditTitlesMenuItem);
		nonFavoriteMenu.add(nonFavoriteClearTagMenuItem);
		nonFavoriteMenu.add(nonFavoriteExtractPictureMenuItem);
		nonFavoriteMenu.add(new JSeparator());
		nonFavoriteMenu.add(nonFavoriteSetAsFavoriteSongMenuItem);
		nonFavoriteMenu.add(nonFavoriteSetAsFavoriteAlbumMenuItem);
		nonFavoriteMenu.add(nonFavoriteSetAsFavoriteArtistMenuItem);
		nonFavoriteMenu.add(new JSeparator());
		nonFavoriteMenu.add(nonFavoriteSearch);
		nonFavoriteMenu.add(nonFavoriteSearchAt);
		
		deviceMenu = new JPopupMenu();
		deviceCopyToRepositoryMenuItem = new JMenuItem(LanguageTool.getString("COPY_TO_REPOSITORY"));
		deviceCopyToRepositoryMenuItem.setFont(FontSingleton.GENERAL_FONT);
		deviceMenu.add(deviceCopyToRepositoryMenuItem);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(BorderFactory.createEmptyBorder());
		tabbedPane.addTab(LanguageTool.getString("TAGS"), ImageLoader.INFO, navigationTreeScrollPane, LanguageTool.getString("TAGS_TAB_TOOLTIP"));
		tabbedPane.addTab(LanguageTool.getString("FOLDERS"), ImageLoader.FOLDER, scrollPane3, LanguageTool.getString("FOLDER_TAB_TOOLTIP"));
		tabbedPane.addTab(LanguageTool.getString("FAVORITES"), ImageLoader.FAVORITE, scrollPane4, LanguageTool.getString("FAVORITES_TAB_TOOLTIP"));
		tabbedPane.addTab(LanguageTool.getString("DEVICE"), ImageLoader.DEVICE, scrollPane5, LanguageTool.getString("DEVICE_VIEW"));
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		addToPlayList = new CustomButton(ImageLoader.ADD, null);
		addToPlayList.setPreferredSize(new Dimension(20,20));
		addToPlayList.setToolTipText(LanguageTool.getString("ADD_TO_PLAYLIST_TOOLTIP"));
		
		prefsButton = new PopUpButton(LanguageTool.getString("OPTIONS"), PopUpButton.TOP_RIGHT);
		showArtist = new JRadioButtonMenuItem(LanguageTool.getString("SHOW_ARTISTS"), ImageLoader.ARTIST);
		showAlbum = new JRadioButtonMenuItem(LanguageTool.getString("SHOW_ALBUMS"), ImageLoader.ALBUM);
		prefsButton.add(showArtist);
		prefsButton.add(showAlbum);
		prefsButton.add(new JSeparator());
		expandTree = new JMenuItem(LanguageTool.getString("EXPAND"));
		prefsButton.add(expandTree);
		collapseTree = new JMenuItem(LanguageTool.getString("COLLAPSE"));
		prefsButton.add(collapseTree);
		
		// Filter controls
		filterLabel = new JLabel(LanguageTool.getString("FILTER"));
		filterLabel.setFont(FontSingleton.GENERAL_FONT);
		filterTextField = new JTextField();
		filterTextField.setToolTipText(LanguageTool.getString("FILTER_TEXTFIELD_TOOLTIP"));
		clearFilterButton = new CustomButton(ImageLoader.CLEAR, null);
		clearFilterButton.setPreferredSize(new Dimension(20,20));
		clearFilterButton.setToolTipText(LanguageTool.getString("CLEAR_FILTER_BUTTON_TOOLTIP"));
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(showArtist);
		group2.add(showAlbum);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(0,0,0,5);
		buttonPanel.add(prefsButton, c);
		
		c.gridx = 1; c.gridy = 0; c.insets = new Insets(2,0,0,2);
		buttonPanel.add(filterLabel, c);
		
		c.gridx = 2; c.gridy = 0; c.weightx = 0.5; c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.WEST; c.insets = new Insets(0,0,0,2);
		buttonPanel.add(filterTextField, c);
		
		c.gridx = 3; c.gridy = 0; c.weightx = 0.0; c.fill = GridBagConstraints.NONE; c.insets = new Insets(0,0,0,3);
		buttonPanel.add(clearFilterButton, c);
		
		c.gridx = 4; c.gridy = 0;  c.weighty = 0; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.EAST; c.insets = new Insets(0,0,0,0);
		buttonPanel.add(addToPlayList, c);

		treePanel.add(tabbedPane, BorderLayout.CENTER);
		treePanel.add(buttonPanel, BorderLayout.SOUTH);
		
		navigationTable = new DragSourceTable();
		navigationTable.getTableHeader().setResizingAllowed(false);
		navigationTable.getTableHeader().setReorderingAllowed(false);
		navigationTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		navigationTable.setDefaultRenderer(Integer.class, new SubstanceDefaultTableCellRenderer() {
			private static final long serialVersionUID = 1337377851290885658L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row,column);
				((JLabel)c).setIcon(((Integer)value) != 1 ? ImageLoader.EMPTY : ImageLoader.FAVORITE);
				return c;
				
			}
		});
		
		navigationTable.setDefaultRenderer(String.class, new SubstanceDefaultTableCellRenderer() {
			private static final long serialVersionUID = 8693307342964711167L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,column);
				((JLabel)c).setFont(FontSingleton.GENERAL_FONT);
				return c;
			}
		});
		
		navigationTable.setDefaultRenderer(Long.class, new SubstanceDefaultTableCellRenderer() {
			private static final long serialVersionUID = 7614440163302045553L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, TimeUtils.seconds2String((Long)value), isSelected, hasFocus, row,column);
				((JLabel)c).setHorizontalAlignment(SwingConstants.RIGHT);
				((JLabel)c).setFont(FontSingleton.GENERAL_FONT);
				return c;
			}
		});


		JScrollPane scrollPane2 = new JScrollPane(navigationTable);
		scrollPane2.setBorder(BorderFactory.createEmptyBorder());
		
		navigationTableContainer = new JPanel(new BorderLayout());
		//navigationTableContainer.setMinimumSize(d);
		navigationTableButtonPanel = new JPanel(new GridBagLayout());
		
		navigationTableInfoButton = new CustomButton(ImageLoader.INFO, null);
		navigationTableInfoButton.setPreferredSize(new Dimension(20,20));
		navigationTableInfoButton.setToolTipText(LanguageTool.getString("INFO_BUTTON_TOOLTIP"));
		navigationTableAddButton = new CustomButton(ImageLoader.ADD, null);
		navigationTableAddButton.setPreferredSize(new Dimension(20,20));
		navigationTableAddButton.setToolTipText(LanguageTool.getString("ADD_TO_PLAYLIST_TOOLTIP"));

		PopUpButton sortButton = new PopUpButton(LanguageTool.getString("OPTIONS"), PopUpButton.TOP_RIGHT);
		sortByTrack = new JRadioButtonMenuItem(LanguageTool.getString("SORT_BY_TRACK_NUMBER"));
		sortByTitle = new JRadioButtonMenuItem(LanguageTool.getString("SORT_BY_TITLE"));
		sortByFile = new JRadioButtonMenuItem(LanguageTool.getString("SORT_BY_FILE_NAME"));
		sortButton.add(sortByTrack);
		sortButton.add(sortByTitle);
		sortButton.add(sortByFile);
		ButtonGroup group = new ButtonGroup();
		group.add(sortByTrack);
		group.add(sortByTitle);
		group.add(sortByFile);
		
		c.gridx = 0; c.gridy = 0; c.weightx = 0.5; c.weighty = 0; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.WEST; c.insets = new Insets(0,0,0,0);
		navigationTableButtonPanel.add(sortButton, c);
		
		c.gridx = 1; c.gridy = 0; c.weightx = 0; c.weighty = 0; c.anchor = GridBagConstraints.EAST; 
		navigationTableButtonPanel.add(navigationTableInfoButton, c);
		c.gridx = 2; c.gridy = 0; c.weightx = 0; c.weighty = 0;
		navigationTableButtonPanel.add(navigationTableAddButton, c);
		
		split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		navigationTableContainer.add(scrollPane2, BorderLayout.CENTER);
		navigationTableContainer.add(navigationTableButtonPanel, BorderLayout.SOUTH);
		split.add(treePanel);
		split.add(navigationTableContainer);
		
		split.setDividerLocation(0.5);
		split.setResizeWeight(0.5);
		
		if (!Kernel.getInstance().state.isShowNavigationTable())
			navigationTableContainer.setVisible(false);
		
		add(split);
	}
	
	public JTree getNavigationTree() {
		return navigationTree;
	}

	public DefaultTreeModel getNavigationTreeModel() {
		return navigationTreeModel;
	}

	public JTable getNavigationTable() {
		return navigationTable;
	}
	
	public CustomButton getAddToPlayList() {
		return addToPlayList;
	}

	public CustomButton getNavigationTableAddButton() {
		return navigationTableAddButton;
	}

	public CustomButton getNavigationTableInfoButton() {
		return navigationTableInfoButton;
	}

	public JRadioButtonMenuItem getSortByFile() {
		return sortByFile;
	}

	public JRadioButtonMenuItem getSortByTitle() {
		return sortByTitle;
	}

	public JRadioButtonMenuItem getShowAlbum() {
		return showAlbum;
	}

	public JRadioButtonMenuItem getShowArtist() {
		return showArtist;
	}

	public JMenuItem getCollapseTree() {
		return collapseTree;
	}

	public JMenuItem getExpandTree() {
		return expandTree;
	}

	public CustomButton getClearFilterButton() {
		return clearFilterButton;
	}

	public JTextField getFilterTextField() {
		return filterTextField;
	}

	public JPanel getNavigationTableContainer() {
		return navigationTableContainer;
	}

	public JSplitPane getSplit() {
		return split;
	}

	public JTree getFileNavigationTree() {
		return fileNavigationTree;
	}

	public DefaultTreeModel getFileNavigationTreeModel() {
		return fileNavigationTreeModel;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public PopUpButton getPrefsButton() {
		return prefsButton;
	}

	public JTree getFavoritesTree() {
		return favoritesTree;
	}

	public DefaultTreeModel getFavoritesTreeModel() {
		return favoritesTreeModel;
	}

	public JLabel getFilterLabel() {
		return filterLabel;
	}

	public JMenuItem getAddToPlaylistMenuItem() {
		return addToPlaylistMenuItem;
	}

	public JPopupMenu getFavoriteMenu() {
		return favoriteMenu;
	}

	public JMenuItem getRemoveFromFavoritesMenuItem() {
		return removeFromFavoritesMenuItem;
	}

	public JMenuItem getSetAsPlaylistMenuItem() {
		return setAsPlaylistMenuItem;
	}

	public JMenuItem getNonFavoriteAddToPlaylistMenuItem() {
		return nonFavoriteAddToPlaylistMenuItem;
	}

	public JPopupMenu getNonFavoriteMenu() {
		return nonFavoriteMenu;
	}

	public JMenuItem getNonFavoriteSetAsPlaylistMenuItem() {
		return nonFavoriteSetAsPlaylistMenuItem;
	}

	public JMenuItem getNonFavoriteSetAsFavoriteAlbumMenuItem() {
		return nonFavoriteSetAsFavoriteAlbumMenuItem;
	}

	public JMenuItem getNonFavoriteSetAsFavoriteArtistMenuItem() {
		return nonFavoriteSetAsFavoriteArtistMenuItem;
	}

	public JMenuItem getNonFavoriteSetAsFavoriteSongMenuItem() {
		return nonFavoriteSetAsFavoriteSongMenuItem;
	}

	public JMenuItem getNonFavoriteEditTagMenuItem() {
		return nonFavoriteEditTagMenuItem;
	}

	public JMenuItem getNonFavoriteClearTagMenuItem() {
		return nonFavoriteClearTagMenuItem;
	}

	public JMenuItem getNonFavoriteExtractPictureMenuItem() {
		return nonFavoriteExtractPictureMenuItem;
	}

	public DefaultTreeModel getDeviceTreeModel() {
		return deviceTreeModel;
	}

	public JTree getDeviceTree() {
		return deviceTree;
	}

	public JPopupMenu getDeviceMenu() {
		return deviceMenu;
	}

	public JMenuItem getDeviceCopyToRepositoryMenuItem() {
		return deviceCopyToRepositoryMenuItem;
	}

	public JMenuItem getNonFavoriteEditTitlesMenuItem() {
		return nonFavoriteEditTitlesMenuItem;
	}

	public JMenuItem getNonFavoriteSearchAt() {
		return nonFavoriteSearchAt;
	}

	public JMenuItem getNonFavoriteSearch() {
		return nonFavoriteSearch;
	}

	public JScrollPane getNavigationTreeScrollPane() {
		return navigationTreeScrollPane;
	}

	public JRadioButtonMenuItem getSortByTrack() {
		return sortByTrack;
	}
}
