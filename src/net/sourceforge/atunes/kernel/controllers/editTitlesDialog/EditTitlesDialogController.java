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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.sourceforge.atunes.gui.views.dialogs.EditTitlesDialog;
import net.sourceforge.atunes.kernel.controllers.model.DialogController;
import net.sourceforge.atunes.kernel.executors.BackgroundExecutor;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.model.info.Album;


public class EditTitlesDialogController extends DialogController {

	private EditTitlesDialog dialog;
	private ArrayList<AudioFile> filesToEdit;
	private Album album;
	private EditTitlesTableModel model;
	
	public EditTitlesDialogController(EditTitlesDialog dialog) {
		super(dialog);
		this.dialog = dialog;
		addBindings();
	}

	protected void addBindings() {
		EditTitlesDialogActionListener actionListener = new EditTitlesDialogActionListener(dialog, this);
		dialog.getRetrieveFromAmazon().addActionListener(actionListener);
		dialog.getOkButton().addActionListener(actionListener);
		dialog.getCancelButton().addActionListener(actionListener);
	}

	protected void addStateBindings() {}
	protected void notifyReload() {}

	public void editFiles(Album album) {
		this.album = album;
		filesToEdit = album.getSongs();
		Collections.sort(filesToEdit);
		model = new EditTitlesTableModel(filesToEdit);
		dialog.getTable().setModel(model);
		dialog.setVisible(true);
	}
	
	protected void editFiles() {
		HashMap<AudioFile, String> filesAndTitles = ((EditTitlesTableModel)dialog.getTable().getModel()).getNewValues();
		BackgroundExecutor.changeTitles(filesAndTitles);
	}

	protected void setTitles(ArrayList<String> tracks) {
		model.setTitles(tracks);
		dialog.getTable().repaint();
	}
	
	protected Album getAlbum() {
		return album;
	}
}
