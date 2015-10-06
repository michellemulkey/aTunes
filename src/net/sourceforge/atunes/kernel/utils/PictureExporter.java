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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.language.LanguageTool;

import com.sun.image.codec.jpeg.JPEGCodec;

public class PictureExporter {

	public static void exportPicture(AudioFile song) throws FileNotFoundException, IOException {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File f) { return f.isDirectory() || f.getName().toUpperCase().endsWith("JPG") || f.getName().toUpperCase().endsWith("JPEG");}
			public String getDescription() { return "JPEG files";}
		};
		fileChooser.setFileFilter(filter);
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.getName().toUpperCase().endsWith("JPG") || !file.getName().toUpperCase().endsWith("JPEG"))
				file = new File(file.getAbsolutePath() + ".jpg");
			if (!file.exists() || (file.exists() && JOptionPane.showConfirmDialog(null, LanguageTool.getString("OVERWRITE_FILE")) == JOptionPane.OK_OPTION)) {
				doExport(song, file);
			}
		}
	}
	
	public static void savePicture(Image image, String fileName) throws IOException {
		ImageIcon img = new ImageIcon(image);
		BufferedImage buf = new BufferedImage(img.getIconWidth(), img.getIconHeight(),BufferedImage.TYPE_INT_BGR);
		Graphics g = buf.createGraphics();
		g.drawImage(image,0,0,null);
		JPEGCodec.createJPEGEncoder(new FileOutputStream(fileName)).encode(buf);
	}
	
	private static void doExport(AudioFile song, File file) throws FileNotFoundException, IOException {
		ImageIcon image = AudioFilePictureUtils.getInsidePicture(song, -1,-1);
		Image theImage = image.getImage();
		
		BufferedImage buf = new BufferedImage(image.getIconWidth(), image.getIconHeight(),BufferedImage.TYPE_INT_BGR);
		Graphics g = buf.createGraphics();
		g.drawImage(theImage,0,0,null);
		JPEGCodec.createJPEGEncoder(new FileOutputStream(file)).encode(buf);
	}
}
