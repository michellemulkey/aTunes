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

package net.sourceforge.atunes.kernel.controllers.exportOptionsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import net.sourceforge.atunes.gui.views.dialogs.CopyProgressDialog;
import net.sourceforge.atunes.gui.views.dialogs.ExportOptionsDialog;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.controllers.model.DialogController;
import net.sourceforge.atunes.kernel.executors.BackgroundExecutor;
import net.sourceforge.atunes.kernel.executors.processes.ExportFilesProcess;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class ExportOptionsDialogController extends DialogController {

	private int exportType;
	private int exportStructure;
	private String filePattern;
	private String path;
	
	private boolean canceled;
	
	private ExportOptionsDialog dialogControlled;
	
	public ExportOptionsDialogController(ExportOptionsDialog dialogControlled) {
		super(dialogControlled);
		this.dialogControlled = dialogControlled;
		addBindings();
		addStateBindings();
	}
	
	protected void addBindings() {
		ExportOptionsDialogActionListener actionListener = new ExportOptionsDialogActionListener(this, dialogControlled);
		dialogControlled.getExportButton().addActionListener(actionListener);
		dialogControlled.getCancelButton().addActionListener(actionListener);
		dialogControlled.getFlatStructureRadioButton().addActionListener(actionListener);
		dialogControlled.getArtistStructureRadioButton().addActionListener(actionListener);
		dialogControlled.getFullStructureRadioButton().addActionListener(actionListener);
		dialogControlled.getNoChangeFileNamesRadioButton().addActionListener(actionListener);
		dialogControlled.getAllTypeRadioButton().addActionListener(actionListener);
		dialogControlled.getSelectionTypeRadioButton().addActionListener(actionListener);
		dialogControlled.getFavoritesTypeRadioButton().addActionListener(actionListener);
	}
	
	protected void addStateBindings() {
		exportType = ExportFilesProcess.ALL_EXPORT;
		dialogControlled.getAllTypeRadioButton().setSelected(true);
		
		exportStructure = ExportFilesProcess.FULL_STRUCTURE;
		dialogControlled.getFullStructureRadioButton().setSelected(true);
		
		filePattern = null;
		dialogControlled.getNoChangeFileNamesRadioButton().setSelected(true);
	}
	
	protected void notifyReload() {}
	
	public void beginExportProcess() {
		dialogControlled.setVisible(true);
		if (!canceled) {
			boolean isValidPath = verifyPath();
			boolean isValidFilePattern = verifyFilePattern();
			if (isValidPath && isValidFilePattern) {
				boolean pathExists = new File(path).exists();
				boolean userWantsToCreate = false;
				if (!pathExists) {
					if (HandlerProxy.getVisualHandler().showConfirmationDialog(LanguageTool.getString("DIR_NO_EXISTS"), LanguageTool.getString("INFO")) == JOptionPane.OK_OPTION) {
						pathExists = new File(path).mkdir();
						userWantsToCreate = true;
					}
				}
				if (pathExists) {
					ArrayList<AudioFile> songs;
					if (exportType == ExportFilesProcess.ALL_EXPORT)
						songs = HandlerProxy.getRepositoryHandler().getSongs();
					else if (exportType == ExportFilesProcess.SELECTION_EXPORT)
						songs = HandlerProxy.getControllerHandler().getNavigationController().getSongsForNavigationTree();
					else
						songs = HandlerProxy.getRepositoryHandler().getFavoriteSongs();
					int filesToExport = songs.size();
					
					CopyProgressDialog exportProgressDialog = HandlerProxy.getVisualHandler().getExportProgressDialog();
					exportProgressDialog.getTotalFilesLabel().setText(" / " + filesToExport);
					exportProgressDialog.getProgressBar().setMaximum(filesToExport);
					exportProgressDialog.setVisible(true);
					
					final ExportFilesProcess process = BackgroundExecutor.exportFiles(songs, exportStructure, filePattern, path);
					
					exportProgressDialog.getCancelButton().addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							process.notifyCancel();
						}
					});
				}
				else if (userWantsToCreate) {
					HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("COULD_NOT_CREATE_DIR"));
				}
			}
			else {
				if (!isValidPath) {
					HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("INCORRECT_EXPORT_PATH"));
				}
				if (!isValidFilePattern) {
					HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("INCORRECT_FILE_PATTERN"));
				}
			}
		}
	}
	
	private boolean verifyPath() {
		return path != null && !path.equals("");
	}
	
	private boolean verifyFilePattern() {
		return filePattern == null || !filePattern.equals("");
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public void setExportStructure(int exportStructure) {
		this.exportStructure = exportStructure;
	}

	public void setExportType(int exportType) {
		this.exportType = exportType;
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
