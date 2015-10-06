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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.utils.AudioFilePictureUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;



/**
 * @author fleax
 *
 */
public class ImageDialog extends JFrame {
 
	private static final long serialVersionUID = -5163960681035103913L;

	JLabel imageLabel;
	
	public ImageDialog(JFrame parent, AudioFile file) {
		super();
		setIconImage(ImageLoader.APP_ICON.getImage());
		
		ImageIcon[] images = getPicturesForFile(file, -1, -1);
		ImageIcon[] thumbs = getPicturesForFile(file, Constants.IMAGE_WIDTH / 2, Constants.IMAGE_HEIGHT / 2);
		if (images.length > 1) {
			setSize(Constants.THUMBS_WINDOW_WIDTH, Constants.THUMBS_WINDOW_HEIGHT);
			setTitle(Constants.APP_NAME + " - " + LanguageTool.getString("PICTURES_OF_FILE") + ' ' + file.getName());
		}
		else {
			setSize(images[0].getIconWidth() + 50, images[0].getIconHeight() + 50);
			setTitle(Constants.APP_NAME + " - " + LanguageTool.getString("PICTURE_OF_FILE") + ' ' + file.getName());
		}
		
		setResizable(images.length > 1);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
		
		addContent(images, thumbs);
		setVisible(true);
	}
	
	private ImageIcon[] getPicturesForFile(AudioFile file, int width, int height) {
		int size = 0;
		ImageIcon image = AudioFilePictureUtils.getInsidePicture(file, -1,-1);
		if (image != null)
			size++;
		size = size + file.getExternalPicturesCount();
		
		ImageIcon[] result = new ImageIcon[size];
		int firstExternalIndex = 0;
		if (image != null) {
			result[0] = image;
			firstExternalIndex++;
		}
		for (int i = firstExternalIndex; i < size; i++)
			result[i] = AudioFilePictureUtils.getExternalPicture(file, i, width, height);
		
		return result;
	}
	
	private void addContent(ImageIcon[] images, ImageIcon[] thumbs) {
		JPanel panel = new JPanel(new BorderLayout());
		imageLabel = new JLabel(images[0]);
		JScrollPane scrollPane = new JScrollPane(imageLabel);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		if (images.length > 1) {
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 10, 25);
			JPanel thumbsPanel = new JPanel(layout);
			for (int i = 0; i < thumbs.length; i++) {
				final ImageIcon imageToShow = images[i];
				if (imageToShow != null) {
					JButton thumb = new JButton(thumbs[i]);
					thumb.setPreferredSize(new Dimension(thumbs[i].getIconWidth(), thumbs[i].getIconHeight()));
					thumb.setSize(thumb.getSize());
					thumb.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							imageLabel.setIcon(imageToShow);
						}
					});
					thumbsPanel.add(thumb);
				}
			}
			JScrollPane scrollPane2 = new JScrollPane(thumbsPanel);
			scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			panel.add(scrollPane2, BorderLayout.SOUTH);
		}
		setContentPane(panel);
	}
}
