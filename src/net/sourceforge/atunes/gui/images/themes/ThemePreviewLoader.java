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

package net.sourceforge.atunes.gui.images.themes;

import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class ThemePreviewLoader {

	private static HashMap<String, String> previews = new HashMap<String, String>();
	
	static {
		previews.put("Aqua", "AquaPreview.PNG");
		previews.put("Pink", "PinkPreview.PNG");
		previews.put("Green", "GreenPreview.PNG");
		previews.put("Brown", "BrownPreview.PNG");
		previews.put("Charcoal", "CharcoalPreview.PNG");
		previews.put("Creme", "CremePreview.PNG");
		previews.put("DarkViolet", "DarkVioletPreview.PNG");
		previews.put("DesertSand", "DesertSandPreview.PNG");
		previews.put("Ebony", "EbonyPreview.PNG");
		previews.put("JadeForest", "JadeForestPreview.PNG");
		previews.put("LightAqua", "LightAquaPreview.PNG");
		previews.put("LimeGreen", "LimeGreenPreview.PNG");
		previews.put("Olive", "OlivePreview.PNG");
		previews.put("Orange", "OrangePreview.PNG");
		previews.put("Purple", "PurplePreview.PNG");
		previews.put("Raspberry", "RaspberryPreview.PNG");
		previews.put("Sepia", "SepiaPreview.PNG");
		previews.put("SteelBlue", "SteelBluePreview.PNG");
		previews.put("SunGlare", "SunGlarePreview.PNG");
		previews.put("Ultramarine", "UltramarinePreview.PNG");
	}
	
	/**
	 * Returns an image
	 * @param imgName
	 * @return An ImageIcon
	 */
	public static ImageIcon getImage(String imgName) {
		if (!previews.containsKey(imgName))
			return null;
		URL imgURL = ThemePreviewLoader.class.getResource(previews.get(imgName));
		return imgURL != null ? new ImageIcon(imgURL) : null;
	}

}
