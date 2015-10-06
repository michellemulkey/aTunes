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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class CustomDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel container;
	
	public CustomDialog(JFrame owner, int width, int height, boolean modal) {
		super(owner);
		setSize(width, height);
		setUndecorated(true);
		setModal(modal);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		add(container);
	}
	
	public void setContent(JPanel content) {
		content.setOpaque(false);
		container.add(content, BorderLayout.CENTER);
	}
}
