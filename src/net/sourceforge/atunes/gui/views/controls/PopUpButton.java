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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import net.sourceforge.atunes.gui.FontSingleton;

/**
 * @author fleax
 *
 */
public class PopUpButton extends JButton {

	private static final long serialVersionUID = 5193978267971626102L;

	public static final int TOP_LEFT = 0;
	public static final int TOP_RIGHT = 1;
	public static final int BOTTOM_LEFT = 3;
	public static final int BOTTOM_RIGHT = 4;
	
	JPopupMenu menu;
	PopUpButton object;
	
	private int location;
	int xLocation;
	int yLocation;
	
	private ArrayList items = new ArrayList();
	
	public PopUpButton(ImageIcon image, int location) {
		super(null, image);
		setPreferredSize(new Dimension(20,20));
		setButton(location);
}
	
	public PopUpButton(String text, int location) {
		super(text, null);
		setFont(FontSingleton.GENERAL_FONT);
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getFontMetrics(FontSingleton.GENERAL_FONT).stringWidth(text) + 20, 20));
		setButton(location);
	}
	
	private void setButton(int location) {
		object = this;
		this.location = location;
		menu = new JPopupMenu();
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMenuLocation(getLocationProperty());
				menu.show(object, xLocation,yLocation);
			}
		});
	}
	
	void setMenuLocation(int location) {
		if (!items.isEmpty()) {
			if (location == TOP_LEFT || location == TOP_RIGHT)
				yLocation = - (int) ((Component)items.get(0)).getPreferredSize().getHeight() * items.size() - 5;
			else
				yLocation = 21;
			if (location == TOP_LEFT || location == BOTTOM_LEFT) {
				int maxWidth = 0;
				for (int i = 0; i < items.size(); i++) {
					if ((int) ((Component)items.get(i)).getPreferredSize().getWidth() > maxWidth) {
						maxWidth = (int) ((Component)items.get(i)).getPreferredSize().getWidth();
					}
				}
				xLocation = - maxWidth + (int) getPreferredSize().getWidth(); 
			}
			else {
				xLocation = 0;
			}
		}
	}

	int getLocationProperty() {
		return location;
	}
	
	public Component add(Component item) {
		if (!(item instanceof JSeparator)) {
			items.add(item);
			((JMenuItem)item).setOpaque(false);
			((JMenuItem)item).setFont(FontSingleton.GENERAL_FONT);
		}
		return menu.add(item);
	}
	
	public void removeAllItems() {
		menu.removeAll();
		items.clear();
	}
	
}
