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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;


public class ColorDefinitions {

	/* TITLE DIALOG COLORS */
	public static final Color TITLE_DIALOG_FONT_COLOR = Color.black;
	/* END TITLE DIALOG COLORS */
	
	/* OSD DIALOG COLORS */
	public static final Color OSD_DIALOG_FONT_COLOR = Color.black;
	/* END OSD DIALOG COLORS */
	
	/* ABOUT DIALOG COLORS */
	public static final Color ABOUT_DIALOG_FONT_COLOR = Color.BLACK;
	/* END ABOUT DIALOG COLORS */
	
	/** DIALOG BORDER COLOR */
	public static final Color DIALOG_BORDER_COLOR = Color.GRAY;
	
	/** BUTTON BORDER COLOR */
	public static final Color BUTTON_BORDER_COLOR = Color.GRAY.brighter();
	
	/** SLIDERS AND PROGRESS BARS FOREGROUND */
	public static final Color SLIDER_AND_PROGRESS_BAR_COLOR_1 = new Color(123, 186, 238, 255);
	public static final Color SLIDER_AND_PROGRESS_BAR_COLOR_2 = new Color(0, 122, 194, 255);
	
	/* GENERAL COLORS */
	public static final Color GENERAL_PANEL_TOP_GRADIENT_COLOR = new Color(230,230,230);
	public static final Color GENERAL_PANEL_BOTTOM_GRADIENT_COLOR = Color.LIGHT_GRAY;
	
	public static final Color GENERAL_TREE_AND_TABLE_SELECTED_ELEMENT_COLOR_1 = SLIDER_AND_PROGRESS_BAR_COLOR_1;
	public static final Color GENERAL_TREE_AND_TABLE_SELECTED_ELEMENT_COLOR_2 = SLIDER_AND_PROGRESS_BAR_COLOR_2;

	public static final Color GENERAL_BORDER_COLOR = GENERAL_PANEL_BOTTOM_GRADIENT_COLOR;
	
	public static final Color GENERAL_NON_PANEL_TOP_GRADIENT_COLOR = GENERAL_PANEL_BOTTOM_GRADIENT_COLOR.brighter();
	public static final Color GENERAL_NON_PANEL_BOTTOM_GRADIENT_COLOR = GENERAL_PANEL_TOP_GRADIENT_COLOR;

	public static final Color GENERAL_BUTTON_TOP_GRADIENT_COLOR = GENERAL_PANEL_BOTTOM_GRADIENT_COLOR;
	public static final Color GENERAL_BUTTON_BOTTOM_GRADIENT_COLOR = GENERAL_PANEL_TOP_GRADIENT_COLOR;
	
	public static final Color GENERAL_TABLE_HEADER_TOP_COLOR = GENERAL_PANEL_TOP_GRADIENT_COLOR;
	public static final Color GENERAL_TABLE_HEADER_BOTTOM_COLOR = GENERAL_PANEL_BOTTOM_GRADIENT_COLOR;

	public static final Color GENERAL_MENU_BAR_TOP_GRADIENT_COLOR = GENERAL_PANEL_BOTTOM_GRADIENT_COLOR;
	public static final Color GENERAL_MENU_BAR_BOTTOM_GRADIENT_COLOR = GENERAL_PANEL_TOP_GRADIENT_COLOR;
	
	public static final Color GENERAL_SCROLL_BACKGROUND_COLOR_1 = GENERAL_PANEL_TOP_GRADIENT_COLOR;
	public static final Color GENERAL_SCROLL_BACKGROUND_COLOR_2 = GENERAL_PANEL_BOTTOM_GRADIENT_COLOR;
	public static final Color GENERAL_SCROLL_BAR_COLOR_1 = GENERAL_PANEL_TOP_GRADIENT_COLOR;
	public static final Color GENERAL_SCROLL_BAR_COLOR_2 = GENERAL_PANEL_BOTTOM_GRADIENT_COLOR;
	public static final Color GENERAL_SCROLL_BAR_BORDER = GENERAL_BORDER_COLOR;
	
	public static final Color GENERAL_UNKNOWN_ELEMENT_FOREGROUND_COLOR = Color.RED;
	/* END GENERAL COLORS */
	
	/* PLAY LIST CONTROL COLORS */
	public static final Color PLAY_LIST_PLAYING_SONG_COLOR_1 = new Color(110,235,190);
	public static final Color PLAY_LIST_PLAYING_SONG_COLOR_2 = new Color(60,150,80);
	public static final Color PLAY_LIST_PLAYING_SONG_FOREGROUND = Color.WHITE;
	public static final Color PLAY_LIST_SELECTED_SONG_FOREGROUND = Color.WHITE;
	/* END PLAY LIST CONTROL COLORS */
		
	public static final Color TREE_AND_TABLE_SELECTED_FOREGROUND_COLOR = Color.WHITE;
	
	public static void initColors() {
		UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.GRAY));
		UIManager.put("ToolTip.background", new ColorUIResource(Color.WHITE));
		UIManager.put("ToolTip.foreground", new ColorUIResource(Color.BLACK));
	}
	
}
