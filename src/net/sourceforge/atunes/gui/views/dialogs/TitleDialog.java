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

package net.sourceforge.atunes.gui.views.dialogs;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.ColorDefinitions;
import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;

public class TitleDialog extends CustomDialog {

	private static final long serialVersionUID = -7279259267018738903L;
	
	public TitleDialog() {
		super(null, 332, 152, false);
		setAlwaysOnTop(true);
		setContent(getContent());
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel(null);
		panel.setOpaque(false);
		
		JLabel image = new JLabel(ImageLoader.APP_TITLE);
		image.setSize(new Dimension(330,150));
		image.setLocation(0,0);

		JLabel label = new JLabel(Constants.APP_VERSION + "  " + ((char) 169) + ' ' + Constants.APP_AUTHOR);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(FontSingleton.APP_VERSION_TITLE_FONT);
		label.setForeground(ColorDefinitions.TITLE_DIALOG_FONT_COLOR);
		label.setSize(300,20);
		label.setLocation(15, 110);
		
		panel.add(label);
		panel.add(image);

		return panel;
	}
	
	public static void main(String[] args) {
		new TitleDialog().setVisible(true);
	}
}
