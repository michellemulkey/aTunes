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

package net.sourceforge.atunes.kernel.controllers.editTitlesDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.atunes.gui.views.dialogs.EditTitlesDialog;
import net.sourceforge.atunes.kernel.modules.amazon.AmazonAlbum;
import net.sourceforge.atunes.kernel.modules.amazon.AmazonDisc;
import net.sourceforge.atunes.kernel.modules.amazon.AmazonService;


public class EditTitlesDialogActionListener implements ActionListener {

	private EditTitlesDialog dialog;
	private EditTitlesDialogController controller;
	
	public EditTitlesDialogActionListener(EditTitlesDialog dialog, EditTitlesDialogController controller) {
		this.dialog = dialog;
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dialog.getRetrieveFromAmazon()) {
			AmazonAlbum amazonAlbum = AmazonService.getAlbum(controller.getAlbum().getArtist(), controller.getAlbum().getName());
			if (amazonAlbum != null) {
				ArrayList<String> tracks = new ArrayList<String>();
				for (Iterator<AmazonDisc> it = amazonAlbum.getDiscs().iterator(); it.hasNext(); ) {
					AmazonDisc disc = it.next();
					tracks.addAll(disc.getTracks());
				}
				controller.setTitles(tracks);
			}
		}
		else if (e.getSource() == dialog.getOkButton()) {
			controller.editFiles();
			dialog.setVisible(false);
		}
		else
			dialog.setVisible(false);
	}
}
