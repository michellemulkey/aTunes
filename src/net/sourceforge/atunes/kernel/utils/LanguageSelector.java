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

import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;


/**
 * This class is responsible of setting the language
 * @author alex
 *
 */
public class LanguageSelector {

	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(LanguageSelector.class);
	
	/**
	 * Sets application language. If a language is defined in the state, it's used. 
	 * If not, a dialog is shown to let the user choose. The language selected is used and stored in state
	 */
	public static void setLanguage() {
		String lang = Kernel.getInstance().state.getLanguage();
		if (lang != null) {
			LanguageTool.setLanguage(lang);
			logger.info("Setting language: " + lang);
		}
		else {
			logger.info("Language not configured");
			String languageSelected = LanguageTool.showLanguageSelector();
			LanguageTool.setLanguage(languageSelected);
			logger.info("Setting language: " + languageSelected);
			Kernel.getInstance().state.setLanguage(languageSelected);
		}
	}
}
