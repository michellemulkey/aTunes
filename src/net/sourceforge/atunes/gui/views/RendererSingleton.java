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

package net.sourceforge.atunes.gui.views;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import net.sourceforge.atunes.gui.ColorDefinitions;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.model.info.Folder;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class RendererSingleton {

	static String getToolTipForArtist(Artist a) {
		int albums = a.getAlbums().size();
		return a.getName() + " (" + albums + ' ' + (albums > 1 ? LanguageTool.getString("ALBUMS") : LanguageTool.getString("ALBUM")) + ')';
	}
	
	static String getToolTipForAlbum(Album a) {
		int songs = a.getSongs().size();
		return a.getName() + " - " + a.getArtist() + " (" + songs + ' ' + (songs > 1 ? LanguageTool.getString("SONGS") : LanguageTool.getString("SONG")) + ')'; 
	}
	
	static String getToolTipForRepository() {
		int songs = HandlerProxy.getRepositoryHandler().getSongs().size();
		return LanguageTool.getString("REPOSITORY") + " (" + songs + ' ' + (songs > 1 ? LanguageTool.getString("SONGS") : LanguageTool.getString("SONG")) + ')'; 
	}
	
	public static final TreeCellRenderer NAVIGATION_TREE_RENDERER = new TreeCellRenderer() {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			final Object content = node.getUserObject();

			JLabel label = new JLabel(value.toString());
			if (content instanceof Artist) {
				if (!Kernel.getInstance().state.SHOW_FAVORITES_IN_NAVIGATOR || !HandlerProxy.getRepositoryHandler().getFavoriteArtistsInfo().containsValue(content))
					label.setIcon(ImageLoader.ARTIST);
				else
					label.setIcon(ImageLoader.ARTIST_FAVORITE);
				label.setToolTipText(getToolTipForArtist((Artist)content));
			}
			else if (content instanceof Album) {
				if (!Kernel.getInstance().state.SHOW_FAVORITES_IN_NAVIGATOR || !HandlerProxy.getRepositoryHandler().getFavoriteAlbumsInfo().containsValue(content))
					label.setIcon(ImageLoader.ALBUM);
				else
					label.setIcon(ImageLoader.ALBUM_FAVORITE);
				if (!Kernel.getInstance().state.SHOW_ALBUM_TOOLTIP)
					label.setToolTipText(getToolTipForAlbum((Album)content));
			}
			else {
				label.setIcon(ImageLoader.FOLDER);
				label.setToolTipText(getToolTipForRepository());
			}
			
			if (value.toString() != null)
				if (value.toString().equals(LanguageTool.getString("UNKNOWN_ARTIST")) || value.toString().equals(LanguageTool.getString("UNKNOWN_ALBUM")))
					label.setForeground(ColorDefinitions.GENERAL_UNKNOWN_ELEMENT_FOREGROUND_COLOR);

			return label;
		}
	};
	
	public static final TreeCellRenderer FILE_TREE_RENDERER = new TreeCellRenderer() {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			JLabel icon = new JLabel(value.toString());
			icon.setIcon(ImageLoader.FOLDER);

			if (value.toString() != null)
				if (value.toString().equals(LanguageTool.getString("UNKNOWN_ARTIST")) || value.toString().equals(LanguageTool.getString("UNKNOWN_ALBUM")))
					icon.setForeground(ColorDefinitions.GENERAL_UNKNOWN_ELEMENT_FOREGROUND_COLOR);
			
			return icon;
		}
	};
	
	public static final TreeCellRenderer FAVORITE_TREE_RENDERER = new TreeCellRenderer() {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object content = node.getUserObject();

			JLabel icon = new JLabel(value.toString());
			if (content instanceof Artist)
				icon.setIcon(ImageLoader.ARTIST);
			else if (content instanceof Album)
				icon.setIcon(ImageLoader.ALBUM);
			else
				icon.setIcon(ImageLoader.FAVORITE);

			if (value.toString() != null)
				if (value.toString().equals(LanguageTool.getString("UNKNOWN_ARTIST")) || value.toString().equals(LanguageTool.getString("UNKNOWN_ALBUM")))
					icon.setForeground(ColorDefinitions.GENERAL_UNKNOWN_ELEMENT_FOREGROUND_COLOR);
			
			return icon;	
		}
	};
	
	public static final TreeCellRenderer DEVICE_BY_TAG_TREE_RENDERER = new TreeCellRenderer() {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object content = node.getUserObject();

			JLabel icon = new JLabel(value.toString());
			if (content instanceof Artist)
				icon.setIcon(ImageLoader.ARTIST);
			else if (content instanceof Album)
				icon.setIcon(ImageLoader.ALBUM);
			else
				icon.setIcon(ImageLoader.DEVICE);

			if (value.toString() != null)
				if (value.toString().equals(LanguageTool.getString("UNKNOWN_ARTIST")) || value.toString().equals(LanguageTool.getString("UNKNOWN_ALBUM")))
					icon.setForeground(ColorDefinitions.GENERAL_UNKNOWN_ELEMENT_FOREGROUND_COLOR);

			return icon;	
		}
	};
	
	public static final TreeCellRenderer DEVICE_BY_FOLDER_TREE_RENDERER = new TreeCellRenderer() {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object content = node.getUserObject();
			
			JLabel icon = new JLabel(value.toString());
			if (content instanceof Folder)
				icon.setIcon(ImageLoader.FOLDER);
			else
				icon.setIcon(ImageLoader.DEVICE);

			if (value.toString() != null)
				if (value.toString().equals(LanguageTool.getString("UNKNOWN_ARTIST")) || value.toString().equals(LanguageTool.getString("UNKNOWN_ALBUM")))
					icon.setForeground(ColorDefinitions.GENERAL_UNKNOWN_ELEMENT_FOREGROUND_COLOR);
			
			return icon;	
		}
	};

}
