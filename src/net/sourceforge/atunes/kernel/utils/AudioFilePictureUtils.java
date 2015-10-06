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

package net.sourceforge.atunes.kernel.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.ImageIcon;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v2Tag;
import net.sourceforge.atunes.utils.ImageUtils;


public class AudioFilePictureUtils {

	public static ImageIcon getExternalPicture(AudioFile file, int width, int height) {
		return getExternalPicture(file, 0, width, height);
	}
	
	public static ImageIcon getExternalPicture(AudioFile file, int index, int width, int height) {
		if (file != null && file.getExternalPictures() != null && file.getExternalPictures().size() > index) {
			File firstPicture = (File) file.getExternalPictures().get(index);
			ImageIcon image = new ImageIcon(firstPicture.getAbsolutePath()); 
			if (width == -1 || height == -1) {
				return image;
			}
			int maxSize = (image.getIconWidth() > image.getIconHeight()) ? image.getIconWidth() : image.getIconHeight();
			int newWidth = (int) ((float) image.getIconWidth() / (float) maxSize * width);
			int newHeight = (int) ((float) image.getIconHeight() / (float) maxSize * height);
			BufferedImage resizedImage = ImageUtils.scaleImage(image.getImage(), newWidth, newHeight);
			return resizedImage != null ? new ImageIcon(resizedImage) : null;
		}
		return null;
	}
	
	public static ImageIcon getInsidePicture(AudioFile file, int width, int height) {
		try {
			if (file.getTag() instanceof ID3v2Tag) {
				long pictureBegin = ((ID3v2Tag)file.getTag()).getPictureBegin();
				long pictureLength = ((ID3v2Tag)file.getTag()).getPictureLength();
				FileInputStream stream = new FileInputStream(file);
				stream.skip(pictureBegin+1);
				byte[] image = new byte[(int)pictureLength];
				stream.read(image);
				
				if (image.length > 3) {
					int pointer = 0;
					while (image[pointer] != 0)
						pointer++; // Skip mime type
					pointer++; // skip picture type
					while (image[pointer] != 0)
						pointer++; // Skip description
					pointer++;
					while (image[pointer] == 0)
						pointer++;
					byte[] picture = new byte[(int)pictureLength - pointer + 1];
					for (int i = 0; pointer + i < image.length; i++) {
						picture[i] = image[pointer + i];
					}
					
					ImageIcon imageIcon = new ImageIcon(picture);
					
					if (width == -1 || height == -1) {
						return imageIcon;
					}
					int maxSize = (imageIcon.getIconWidth() > imageIcon.getIconHeight()) ? imageIcon.getIconWidth() : imageIcon.getIconHeight();
					int newWidth = (int) ((float) imageIcon.getIconWidth() / (float) maxSize * width);
					int newHeight = (int) ((float) imageIcon.getIconHeight() / (float) maxSize * height);
					BufferedImage resizedImage = ImageUtils.scaleImage(imageIcon.getImage(), newWidth, newHeight);
					if (resizedImage != null)
						return new ImageIcon(resizedImage);
					return null;
				}
				return null;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static BufferedImage getInsidePictureAsBuffer(AudioFile file, int width, int height) {
		try {
			if (file.getTag() instanceof ID3v2Tag) {
				int pictureBegin = ((ID3v2Tag)file.getTag()).getPictureBegin();
				int pictureLength = ((ID3v2Tag)file.getTag()).getPictureLength();
				FileInputStream stream = new FileInputStream(file);
				stream.skip(pictureBegin+1);
				byte[] image = new byte[pictureLength];
				stream.read(image);
				
				if (image.length > 3) {
					int pointer = 0;
					while (image[pointer] != 0)
						pointer++; // Skip mime type
					pointer++; // skip picture type
					while (image[pointer] != 0)
						pointer++; // Skip description
					pointer++;
					while (image[pointer] == 0)
						pointer++;
					byte[] picture = new byte[pictureLength - pointer + 1];
					for (int i = 0; pointer + i < image.length; i++) {
						picture[i] = image[pointer + i];
					}
					
					ImageIcon imageIcon = new ImageIcon(picture);
					
					if (width == -1 || height == -1) {
						return ImageUtils.scaleImage(imageIcon.getImage(), imageIcon.getIconWidth(), imageIcon.getIconHeight());
					}
					int maxSize = (imageIcon.getIconWidth() > imageIcon.getIconHeight()) ? imageIcon.getIconWidth() : imageIcon.getIconHeight();
					int newWidth = (int) ((float) imageIcon.getIconWidth() / (float) maxSize * width);
					int newHeight = (int) ((float) imageIcon.getIconHeight() / (float) maxSize * height);
					BufferedImage resizedImage = ImageUtils.scaleImage(imageIcon.getImage(), newWidth, newHeight);
					return resizedImage;
				}
				return null;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
