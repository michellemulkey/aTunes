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

package net.sourceforge.atunes.gui.views.controls.playList;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.model.PlayListTableModel;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.TimeUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.jvnet.substance.SubstanceDefaultTableCellRenderer;

public class PlayListTable extends JTable {

	private static final long serialVersionUID = 9209069236823917569L;

	public enum PlayState {STOPPED, PLAYING, PAUSED}
	
	static final Font font = FontSingleton.PLAY_LIST_FONT;
	static final Font boldFont = font.deriveFont(Font.BOLD);
	
	int playingSong;
	PlayState playState = PlayState.STOPPED;
	
	private JPopupMenu menu;
	
	private JMenuItem playItem;
	
	private JMenu tagMenu;
	private JMenuItem editTagItem;
	private JMenuItem autoSetTrackNumberItem;
	private JMenuItem autoSetGenreItem;

	private JMenuItem saveItem;
	private JMenuItem loadItem;
	
	private JMenuItem filterItem;
	
	private JMenuItem topItem;
	private JMenuItem upItem;
	private JMenuItem deleteItem;
	private JMenuItem downItem;
	private JMenuItem bottomItem;
	private JMenuItem infoItem;
	private JMenuItem clearItem;
	
	private JMenuItem favoriteSong;
	private JMenuItem favoriteArtist;
	private JMenuItem favoriteAlbum;
	
	private JMenuItem artistItem;
	private JMenuItem albumItem;
	
	private JMenuItem showControls;
	
	ArrayList<PlayListColumnClickedListener> listeners = new ArrayList<PlayListColumnClickedListener>(); 
	
	public PlayListTable() {
		super();
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setShowGrid(false);
		getTableHeader().setReorderingAllowed(false);

		getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TableColumnModel columnModel = getColumnModel();
				int column = columnModel.getColumnIndexAtX( e.getX() );
				String columnName = (String) columnModel.getColumn(column).getHeaderValue();
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).columnClicked(columnName);
				}
			}
		});
		
		setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setModel(new PlayListTableModel());
		
		setDefaultRenderer(Integer.class, new SubstanceDefaultTableCellRenderer() {
			private static final long serialVersionUID = 4027676693367876748L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row,column);
				if (playingSong == row)
					((JLabel)c).setIcon(playState == PlayState.PLAYING ? ImageLoader.PLAY_TINY : (playState == PlayState.STOPPED ? ImageLoader.STOP_TINY : ImageLoader.PAUSE_TINY));
				else
					((JLabel)c).setIcon(((Integer)value) != 1 ? ImageLoader.EMPTY : ImageLoader.FAVORITE);
				return c;
				
			}
		});
		
		setDefaultRenderer(AudioFile.class, new SubstanceDefaultTableCellRenderer() {
			private static final long serialVersionUID = 7305230546936745766L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, ((AudioFile)value).getTitleOrFileName(), isSelected, hasFocus, row,column);
				((JLabel)c).setFont(playingSong != row ? font : boldFont);
				return c;
			}
		});

		setDefaultRenderer(String.class, new SubstanceDefaultTableCellRenderer() {
			private static final long serialVersionUID = 7305230546936745766L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,column);
				((JLabel)c).setFont(playingSong != row ? font : boldFont);
				((JLabel)c).setHorizontalAlignment(column != 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
				return c;
			}
		});
		
		setDefaultRenderer(Long.class, new SubstanceDefaultTableCellRenderer() {
			private static final long serialVersionUID = 7305230546936745766L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, TimeUtils.seconds2String((Long)value), isSelected, hasFocus, row,column);
				((JLabel)c).setHorizontalAlignment(SwingConstants.RIGHT);
				((JLabel)c).setFont(playingSong != row ? font : boldFont);
				return c;
			}
		});
		
		menu = new JPopupMenu();
		
		playItem = setMenuItem(new JMenuItem(LanguageTool.getString("PLAY"), ImageLoader.PLAY_MENU));
		
		tagMenu = setMenu(new JMenu(LanguageTool.getString("TAGS")));
		editTagItem = setMenuItem(new JMenuItem(LanguageTool.getString("EDIT_TAG")));
		autoSetTrackNumberItem = setMenuItem(new JMenuItem(LanguageTool.getString("AUTO_SET_TRACK_NUMBER")));
		autoSetGenreItem = setMenuItem(new JMenuItem(LanguageTool.getString("AUTO_SET_GENRE")));
		tagMenu.add(editTagItem);
		tagMenu.add(autoSetTrackNumberItem);
		tagMenu.add(autoSetGenreItem);
		
		
		saveItem = setMenuItem(new JMenuItem(LanguageTool.getString("SAVE") + "...", ImageLoader.SAVE));
		loadItem = setMenuItem(new JMenuItem(LanguageTool.getString("LOAD") + "...", ImageLoader.FOLDER));
		filterItem = setMenuItem(new JMenuItem(LanguageTool.getString("FILTER")));
		topItem = setMenuItem(new JMenuItem(LanguageTool.getString("MOVE_TO_TOP"), ImageLoader.GO_TOP));
		upItem = setMenuItem(new JMenuItem(LanguageTool.getString("MOVE_UP"), ImageLoader.GO_UP));
		deleteItem = setMenuItem(new JMenuItem(LanguageTool.getString("REMOVE"), ImageLoader.REMOVE));
		downItem = setMenuItem(new JMenuItem(LanguageTool.getString("MOVE_DOWN"), ImageLoader.GO_DOWN));
		bottomItem = setMenuItem(new JMenuItem(LanguageTool.getString("MOVE_TO_BOTTOM"), ImageLoader.GO_BOTTOM));
		infoItem = setMenuItem(new JMenuItem(LanguageTool.getString("INFO"), ImageLoader.INFO));
		clearItem = setMenuItem(new JMenuItem(LanguageTool.getString("CLEAR"), ImageLoader.CLEAR));
		favoriteSong = setMenuItem(new JMenuItem(LanguageTool.getString("SET_FAVORITE_SONG"), ImageLoader.FAVORITE));
		favoriteAlbum = setMenuItem(new JMenuItem(LanguageTool.getString("SET_FAVORITE_ALBUM"), ImageLoader.FAVORITE));
		favoriteArtist = setMenuItem(new JMenuItem(LanguageTool.getString("SET_FAVORITE_ARTIST"), ImageLoader.FAVORITE));
		artistItem = setMenuItem(new JMenuItem(LanguageTool.getString("SET_ARTIST_AS_PLAYLIST"), ImageLoader.ARTIST));
		albumItem = setMenuItem(new JMenuItem(LanguageTool.getString("SET_ALBUM_AS_PLAYLIST"), ImageLoader.ALBUM));
		showControls = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_PLAYLIST_CONTROLS"));
		showControls.setFont(font);
		
		menu.add(playItem);
		menu.add(infoItem);
		menu.add(new JSeparator());
		menu.add(tagMenu);
		menu.add(new JSeparator());
		menu.add(saveItem);
		menu.add(loadItem);
		menu.add(new JSeparator());
		menu.add(filterItem);
		menu.add(new JSeparator());
		menu.add(deleteItem);
		menu.add(clearItem);
		menu.add(new JSeparator());
		menu.add(topItem);
		menu.add(upItem);
		menu.add(downItem);
		menu.add(bottomItem);
		menu.add(new JSeparator());
		menu.add(favoriteSong);
		menu.add(favoriteAlbum);
		menu.add(favoriteArtist);
		menu.add(new JSeparator());
		menu.add(artistItem);
		menu.add(albumItem);
		menu.add(new JSeparator());
		menu.add(showControls);
	}
	
	public void addPlayListColumnClickedListener(PlayListColumnClickedListener l) {
		listeners.add(l);
	}
	
	private JMenuItem setMenuItem(JMenuItem item) {
		item.setFont(font);
		return item;
	}

	private JMenu setMenu(JMenu item) {
		item.setFont(font);
		return item;
	}

	public void setPlayingSong(int row) {
		playingSong = row;
		((PlayListTableModel)getModel()).refresh();
	}
	
	public int getPlayingSong() {
		return playingSong;
	}

	public JMenuItem getDeleteItem() {
		return deleteItem;
	}

	public JPopupMenu getMenu() {
		return menu;
	}

	public JMenuItem getBottomItem() {
		return bottomItem;
	}

	public JMenuItem getDownItem() {
		return downItem;
	}

	public JMenuItem getInfoItem() {
		return infoItem;
	}

	public JMenuItem getTopItem() {
		return topItem;
	}

	public JMenuItem getUpItem() {
		return upItem;
	}

	public JMenuItem getClearItem() {
		return clearItem;
	}

	public JMenuItem getLoadItem() {
		return loadItem;
	}

	public JMenuItem getPlayItem() {
		return playItem;
	}

	public JMenuItem getSaveItem() {
		return saveItem;
	}

	public JMenuItem getFavoriteAlbum() {
		return favoriteAlbum;
	}

	public JMenuItem getFavoriteArtist() {
		return favoriteArtist;
	}

	public JMenuItem getFavoriteSong() {
		return favoriteSong;
	}

	public JMenuItem getAlbumItem() {
		return albumItem;
	}

	public JMenuItem getArtistItem() {
		return artistItem;
	}

	public JMenuItem getShowControls() {
		return showControls;
	}

	public JMenuItem getEditTagItem() {
		return editTagItem;
	}

	public JMenuItem getFilterItem() {
		return filterItem;
	}

	public void setPlayState(PlayState playState) {
		this.playState = playState;
		((PlayListTableModel)getModel()).refresh(playingSong);
	}

	public JMenuItem getAutoSetTrackNumberItem() {
		return autoSetTrackNumberItem;
	}

	public JMenuItem getAutoSetGenreItem() {
		return autoSetGenreItem;
	}
}
