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

package net.sourceforge.atunes.gui.dockableView;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class DockableDialog extends DockableWindow {
	
	public DockableDialog(DockableFrame parent, int width, int heigth, Dimension minSize, DockFramePositionListener listener) {
		super(listener, minSize);
		this.listener = listener;
		frame = new JDialog((JFrame)parent.frame);
		((JDialog)frame).setUndecorated(true);
		frame.setSize(width, heigth);
		frame.add(container);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		addResizableBorders();
		setHidden(false);
		frame.setFocusable(false);
	}
	
	public void setTitle(String s) {
		title.setText(s);
		((JDialog)frame).setTitle(s);
	}
	
	public void setIcon(Image i) {}
	
	public void setDefaultCloseOperation(int op) {
		((JDialog)frame).setDefaultCloseOperation(op);
	}
	
	public void setLocationRelativeTo(Component c) {
		((JDialog)frame).setLocationRelativeTo(c);
	}
}
