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

package net.sourceforge.atunes.kernel.controllers.editTagDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.atunes.gui.views.dialogs.EditTagDialog;
import net.sourceforge.atunes.utils.ImageUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class EditTagDialogActionListener implements ActionListener {

	private EditTagDialogController controller;
	private EditTagDialog dialog;
	
	public EditTagDialogActionListener(EditTagDialogController controller, EditTagDialog dialog) {
		this.controller = controller;
		this.dialog = dialog;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dialog.getRemovePictureCheckBox()) {
			dialog.getPictureButton().setEnabled(dialog.getRemovePictureCheckBox().isSelected());
		}
		else if (e.getSource() == dialog.getPictureButton()) {
			JFileChooser fileChooser = new JFileChooser();
			if (controller.getSelectedFile() != null)
				fileChooser.setCurrentDirectory(controller.getSelectedFile().getParentFile());
			FileFilter filter = EditTagDialogController.getImageFilter();
			fileChooser.setFileFilter(filter);
			if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.exists()) {
					ImageIcon image = new ImageIcon(ImageUtils.scaleImage(new ImageIcon(file.getAbsolutePath()).getImage(), 100, 100));
					dialog.getPictureButton().setIcon(image);
					controller.setSelectedFile(file);
				} else
					JOptionPane.showMessageDialog(dialog, LanguageTool.getString("FILE_NOT_FOUND"), LanguageTool.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getSource() == dialog.getOkButton()) {
			controller.editTag();
		}
		else if (e.getSource() == dialog.getCancelButton()) {
			dialog.setVisible(false);
		}
	}
}
