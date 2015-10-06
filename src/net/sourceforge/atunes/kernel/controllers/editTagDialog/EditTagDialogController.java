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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.atunes.gui.views.dialogs.EditTagDialog;
import net.sourceforge.atunes.kernel.controllers.model.DialogController;
import net.sourceforge.atunes.kernel.executors.BackgroundExecutor;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v2Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;
import net.sourceforge.atunes.kernel.utils.AudioFilePictureUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class EditTagDialogController extends DialogController {
	
	private ArrayList<AudioFile> audioFilesEditing;
	private File selectedPictureFile;
	
	public EditTagDialogController(EditTagDialog dialog) {
		super(dialog);
		addBindings();
		addStateBindings();
	}
	
	protected void addBindings() {
		final EditTagDialog dialog = (EditTagDialog)dialogControlled;
		
		// Add genres combo box items
		String [] genresSorted = new String[Tag.genres.length];
		for (int i = 0; i < Tag.genres.length; i++) {
			genresSorted[i] = Tag.genres[i];
		}
		Arrays.sort(genresSorted);
		dialog.getGenreComboBox().addItem("");
		for (int i = 0; i < genresSorted.length; i++)
			dialog.getGenreComboBox().addItem(genresSorted[i]);	
		
		EditTagDialogActionListener actionListener = new EditTagDialogActionListener(this, dialog);
		dialog.getRemovePictureCheckBox().addActionListener(actionListener);
		dialog.getPictureButton().addActionListener(actionListener);
		dialog.getOkButton().addActionListener(actionListener);
		dialog.getCancelButton().addActionListener(actionListener);
	}
	
	protected void addStateBindings() {}
	protected void notifyReload() {}
	
	protected void editTag() {
		EditTagDialog dialog = (EditTagDialog)dialogControlled;
		dialog.setVisible(false);
		
		// Build editor props
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("TITLE", dialog.getTitleTextField().getText());
		properties.put("ARTIST", dialog.getArtistTextField().getText());
		properties.put("ALBUM", dialog.getAlbumTextField().getText());
		properties.put("YEAR", dialog.getYearTextField().getText());
		properties.put("COMMENT", dialog.getCommentTextArea().getText());
		if (dialog.getGenreComboBox().getSelectedIndex() == 0)
			properties.put("GENRE", "-1");
		else
			properties.put("GENRE", dialog.getGenreComboBox().getSelectedItem());
		properties.put("TRACK", dialog.getTrackNumberTextField().getText());
		properties.put("HAS_PICTURE", dialog.getRemovePictureCheckBox().isSelected());
		if (selectedPictureFile != null)
			properties.put("PICTURE", selectedPictureFile);
		
		BackgroundExecutor.changeTags(audioFilesEditing, properties);
	}
	
	public void editFiles(ArrayList<AudioFile> files) {
		audioFilesEditing = files;
		EditTagDialog dialog = (EditTagDialog)dialogControlled;
		
		if (files.size() == 1) {
			dialog.getTrackNumberTextField().setEnabled(true);
			Tag tag = files.get(0).getTag();
			if (tag != null) {
				dialog.getTitleTextField().setText(tag.getTitle());
				dialog.getTitleTextField().setEnabled(true);
				if (tag instanceof ID3v2Tag) {
					if (((ID3v2Tag)tag).getTrackNumber() > 0)
						dialog.getTrackNumberTextField().setText(Integer.toString(((ID3v2Tag)tag).getTrackNumber()));
					else
						dialog.getTrackNumberTextField().setText("");
				}
				else
					dialog.getTrackNumberTextField().setText("");
			}
			else {
				dialog.getTitleTextField().setText("");
				dialog.getTitleTextField().setEnabled(true);
				dialog.getTrackNumberTextField().setText("");
			}
		}
		else {
			dialog.getTitleTextField().setText("");
			dialog.getTitleTextField().setEnabled(false);
			dialog.getTrackNumberTextField().setText("");
			dialog.getTrackNumberTextField().setEnabled(false);
		}
		
		HashSet<String> artists = new HashSet<String>();
		HashSet<String> albums = new HashSet<String>();
		HashSet<Integer> years = new HashSet<Integer>();
		HashSet<String> comments = new HashSet<String>();
		HashSet<String> genres = new HashSet<String>();
		HashSet<ImageIcon> images = new HashSet<ImageIcon>();
		for (int i = 0; i < files.size(); i++) {
			String artist = "";
			String album = "";
			int year = -1;
			String comment = "";
			String genre = "";
			if (files.get(i).getTag() != null) {
				Tag tag = files.get(i).getTag();
				artist = tag.getArtist();
				album = tag.getAlbum();
				year = tag.getYear();
				comment = tag.getComment();
				genre = tag.getGenre();
			}
			artists.add(artist);
			albums.add(album);
			years.add(year);
			comments.add(comment);
			genres.add(genre);
			
			ImageIcon img = AudioFilePictureUtils.getInsidePicture(files.get(i), 100,100);
			if (img != null)
				images.add(img);
		}
		
		if (artists.size() == 1 && files.get(0).getTag() != null)
			dialog.getArtistTextField().setText(files.get(0).getTag().getArtist());
		else
			dialog.getArtistTextField().setText("");

		if (albums.size() == 1 && files.get(0).getTag() != null)
			dialog.getAlbumTextField().setText(files.get(0).getTag().getAlbum());
		else
			dialog.getAlbumTextField().setText("");
		
		if (years.size() == 1 && files.get(0).getTag() != null && files.get(0).getTag().getYear() > 0)
			dialog.getYearTextField().setText(Integer.toString(files.get(0).getTag().getYear()));
		else
			dialog.getYearTextField().setText("");
		
		if (comments.size() == 1 && files.get(0).getTag() != null)
			dialog.getCommentTextArea().setText(files.get(0).getTag().getComment());
		else
			dialog.getCommentTextArea().setText("");
		
		if (genres.size() == 1 && files.get(0).getTag() != null) {
			dialog.getGenreComboBox().setSelectedItem(files.get(0).getTag().getGenre());
		}
		else
			dialog.getGenreComboBox().setSelectedIndex(0);
		
		if (images.size() == 1) {
			dialog.getRemovePictureCheckBox().setSelected(true);
			dialog.getRemovePictureCheckBox().setEnabled(true);
			dialog.getPictureButton().setIcon(AudioFilePictureUtils.getInsidePicture(files.get(0), 100,100));
		}
		else if (images.size() == 0) {
			dialog.getPictureButton().setIcon(null);
			dialog.getRemovePictureCheckBox().setSelected(false);
			dialog.getRemovePictureCheckBox().setEnabled(true);
		}
		else {
			dialog.getPictureButton().setIcon(null);
			dialog.getRemovePictureCheckBox().setSelected(true);
			dialog.getRemovePictureCheckBox().setEnabled(true);
		}
		dialog.getPictureButton().setEnabled(dialog.getRemovePictureCheckBox().isEnabled());
		
		dialog.setVisible(true);
	}
	
	protected static final FileFilter getImageFilter() {
		return new FileFilter() {
			public boolean accept(File file) {
				String name = file.getName().toUpperCase();
				return file.isDirectory() || name.endsWith("JPG") || name.endsWith("JPEG") || name.endsWith("GIF");
			}
			public String getDescription() {
				return LanguageTool.getString("PICTURE") + " (JPG, GIF)";
			}
		};
	}

	protected File getSelectedFile() {
		return selectedPictureFile;
	}

	protected void setSelectedFile(File selectedFile) {
		this.selectedPictureFile = selectedFile;
	}
}
