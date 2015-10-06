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

package net.sourceforge.atunes.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;

import javax.swing.ImageIcon;

public class ImageUtils {

	public static ImageIcon resize(ImageIcon image, int width, int height) {
		if (width == -1 || height == -1) {
			return image;
		}
		int maxSize = (image.getIconWidth() > image.getIconHeight()) ? image.getIconWidth() : image.getIconHeight();
		int newWidth = (int) ((float) image.getIconWidth() / (float) maxSize * width);
		int newHeight = (int) ((float) image.getIconHeight() / (float) maxSize * height);
		BufferedImage resizedImage = ImageUtils.scaleImage(image.getImage(), newWidth, newHeight);
		return resizedImage != null ? new ImageIcon(resizedImage) : null;
	}
	
    public static BufferedImage scaleImage(Image sourceImage, int width, int height)
    {
    	if (sourceImage == null)
    		return null;
        ImageFilter filter = new ReplicateScaleFilter(width,height);
        ImageProducer producer = new FilteredImageSource(sourceImage.getSource(),filter);
        Image resizedImage = Toolkit.getDefaultToolkit().createImage(producer);

        return toBufferedImage(resizedImage);
    }
    
    private static BufferedImage toBufferedImage(Image image)
    {
    	BufferedImage bufferedImage;
    	try {
    		image = new ImageIcon(image).getImage();
    		bufferedImage = new BufferedImage(image.getWidth(null)
    				,image.getHeight(null),BufferedImage.TYPE_INT_RGB);
    		Graphics2D g = bufferedImage.createGraphics();
    		g.setColor(Color.white);
    		g.fillRect(0,0,image.getWidth(null),image.getHeight(null));
    		g.drawImage(image,0,0,null);
    		g.dispose();
    	} catch (IllegalArgumentException e) {
    		return null;
    	}

        return bufferedImage;
    }

}
