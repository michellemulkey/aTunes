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

package net.sourceforge.atunes.gui.views.controls;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import net.sourceforge.atunes.kernel.handlers.SearchInternetHandler;


public class UrlLabel extends JLabel {

	private static final long serialVersionUID = -8368596300673361747L;

	String url;
	
	public UrlLabel(String text) {
		super(text);
		setListeners();
	}
	
	public UrlLabel() {
		super();
		setListeners();
	}
	
	private void setListeners() {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				SearchInternetHandler.openURL(url);
			}
			public void mouseEntered(MouseEvent e) {
				setFont(getFont().deriveFont(Font.BOLD));
			}
			public void mouseExited(MouseEvent e) {
				setFont(getFont().deriveFont(Font.PLAIN));
			}
		});
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
