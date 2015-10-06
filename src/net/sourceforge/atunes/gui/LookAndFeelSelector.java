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

package net.sourceforge.atunes.gui;

import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.UIManager;

import net.sourceforge.atunes.kernel.Kernel;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.watermark.SubstanceStripeWatermark;

public class LookAndFeelSelector {

	public static final String DEFAULT_THEME = "Ebony";
	
	private static HashMap<String, String> themes = setListOfThemes();
	
	private static HashMap<String, String> setListOfThemes() {
		HashMap<String, String> result = new HashMap<String, String>();
		
		result.put("Aqua", "org.jvnet.substance.theme.SubstanceAquaTheme");
		result.put("Pink", "org.jvnet.substance.theme.SubstanceBarbyPinkTheme");
		result.put("Green", "org.jvnet.substance.theme.SubstanceBottleGreenTheme");
		result.put("Brown", "org.jvnet.substance.theme.SubstanceBrownTheme");
		result.put("Charcoal", "org.jvnet.substance.theme.SubstanceCharcoalTheme");
		result.put("Creme", "org.jvnet.substance.theme.SubstanceCremeTheme");
		result.put("DarkViolet", "org.jvnet.substance.theme.SubstanceDarkVioletTheme");
		result.put("DesertSand", "org.jvnet.substance.theme.SubstanceDesertSandTheme");
		result.put("Ebony", "org.jvnet.substance.theme.SubstanceEbonyTheme");
		result.put("JadeForest", "org.jvnet.substance.theme.SubstanceJadeForestTheme");
		result.put("LightAqua", "org.jvnet.substance.theme.SubstanceLightAquaTheme");
		result.put("LimeGreen", "org.jvnet.substance.theme.SubstanceLimeGreenTheme");
		result.put("Olive", "org.jvnet.substance.theme.SubstanceOliveTheme");
		result.put("Orange", "org.jvnet.substance.theme.SubstanceOrangeTheme");
		result.put("Purple", "org.jvnet.substance.theme.SubstancePurpleTheme");
		result.put("Raspberry", "org.jvnet.substance.theme.SubstanceRaspberryTheme");
		result.put("Sepia", "org.jvnet.substance.theme.SubstanceSepiaTheme");
		result.put("SteelBlue", "org.jvnet.substance.theme.SubstanceSteelBlueTheme");
		result.put("SunGlare", "org.jvnet.substance.theme.SubstanceSunGlareTheme");
		result.put("Ultramarine", "org.jvnet.substance.theme.SubstanceUltramarineTheme");
		
		return result;
	}
	
	public static void setLookAndFeel(String theme) {
		try {			
			// Use Business as base Look And Feel
			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessLookAndFeel");
			
			if (themes.containsKey(theme))
				SubstanceLookAndFeel.setCurrentTheme(themes.get(theme));
			else
				SubstanceLookAndFeel.setCurrentTheme(themes.get(DEFAULT_THEME));

			SubstanceLookAndFeel.setCurrentWatermark(new SubstanceStripeWatermark());
			UIManager.put(SubstanceLookAndFeel.NO_EXTRA_ELEMENTS,Boolean.TRUE);
			JFrame.setDefaultLookAndFeelDecorated(!Kernel.getInstance().state.isMultipleWindow());
		} catch (Exception e) {
		}
	}

	public static Object[] getListOfThemes() {
		Object[] result = themes.keySet().toArray();
		Arrays.sort(result);
		return result;
	}
}
