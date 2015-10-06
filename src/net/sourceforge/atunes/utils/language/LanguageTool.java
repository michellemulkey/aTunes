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

package net.sourceforge.atunes.utils.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;


/**
 * @author fleax
 *
 */
public class LanguageTool {

	public static final String TRANSLATIONS_DIR = "translations";
	public static final String DEFAULT_LANGUAGE_FILE = "english.properties";
	
	private static PropertyResourceBundle languageBundle;
	
	private static PropertyResourceBundle defaultLanguageBundle = getLanguageFile(TRANSLATIONS_DIR + '/' + DEFAULT_LANGUAGE_FILE);
	
	private static LanguageSelectorDialog dialog;
	
	private static String languageSelected;
	
	public static void setLanguage(String fileName) {
		if (fileName != null)
			languageBundle = getLanguageFile(TRANSLATIONS_DIR + '/' + fileName);
		else
			languageBundle = getLanguageFile(TRANSLATIONS_DIR + '/' + DEFAULT_LANGUAGE_FILE);
	}
	
	private static PropertyResourceBundle getLanguageFile(String fileName) {
		try {
			if (fileName == null)
				return new PropertyResourceBundle(new FileInputStream(TRANSLATIONS_DIR + '/' + DEFAULT_LANGUAGE_FILE));
			PropertyResourceBundle bundle = new PropertyResourceBundle(new FileInputStream(fileName));
			String name = fileName.substring(fileName.indexOf('/')+1, fileName.indexOf('.'));
			languageSelected = Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
			return bundle;
		} catch (IOException e) {
			try {
				String name = DEFAULT_LANGUAGE_FILE.substring(0, DEFAULT_LANGUAGE_FILE.indexOf('.'));
				languageSelected = Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
				return new PropertyResourceBundle(new FileInputStream(TRANSLATIONS_DIR + '/' + DEFAULT_LANGUAGE_FILE));
			} catch (Exception e1) {
				return null;
			}
		}
	}
	
	public static String getString(String key) {
		if (languageBundle != null) {
			String result;
			try {
				result = languageBundle.getString(key);
			} catch (MissingResourceException e) {
				try {
					result = defaultLanguageBundle.getString(key);
				} catch (MissingResourceException e1) {
					return key;
				}
			}
			return result;
		}
		return key;
	}
	
	public static void compareTwoTranslations(String langFile1, String langFile2) {
		int errors = 0;
		PropertyResourceBundle lang1 = getLanguageFile(langFile1 + ".properties");
		PropertyResourceBundle lang2 = getLanguageFile(langFile2 + ".properties");
		
		ArrayList<String> keys1 = new ArrayList<String>();
		Enumeration<String> enum1 = lang1.getKeys();
		while (enum1.hasMoreElements()) {
			keys1.add(enum1.nextElement());
		}
		
		ArrayList<String> keys2 = new ArrayList<String>();
		Enumeration<String> enum2 = lang2.getKeys();
		while (enum2.hasMoreElements()) {
			keys2.add(enum2.nextElement());
		}
		
		errors = errors + compareTwoTranslations(langFile1, keys1, keys2);
		errors = errors + compareTwoTranslations(langFile2, keys2, keys1);
	}
	
	private static int compareTwoTranslations(String langCode, ArrayList<String> keys, ArrayList<String> keysToFind) {
		int errors = 0;
		ArrayList<String> keysNotFound = new ArrayList<String>();
		for (Iterator it = keysToFind.iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			if (!keys.contains(key)) {
				keysNotFound.add(key);
				errors++;
			}
		}
		String[] keysArray = keysNotFound.toArray(new String[errors]);
		Arrays.sort(keysArray);
		for (int i = 0; i < keysArray.length; i++) {
			System.out.println("Key \"" + keysArray[i] + "\" not found in \"" + langCode + "\"");
		}
		
		return errors;
	}
	
	public static ArrayList<String> getLanguages() {
		File transDir = new File(TRANSLATIONS_DIR);
		File[] files = transDir.listFiles();
		ArrayList<String> translations = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			if (!files[i].isDirectory() && fileName.toLowerCase().endsWith("properties")) {
				String name = fileName.substring(0, fileName.lastIndexOf('.'));
				String description = Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
				translations.add(description);
			}
		}
		return translations;
	}
	
	public static String getLanguageFileByName(String language) {
		return language.toLowerCase() + ".properties";
	}
	
	public static String showLanguageSelector() {
		ArrayList<String> translations = getLanguages();
		if (translations.size() == 1) {
			return translations.get(0).toLowerCase() + ".properties";
		}
		dialog = new LanguageSelectorDialog(translations.toArray(new String[translations.size()]));
		String sel = dialog.getSelection() != null ? dialog.getSelection().toLowerCase() + ".properties" : null;
		dialog.dispose();
		return sel == null ? DEFAULT_LANGUAGE_FILE : sel;
	}

	public static void main(String[] args) {
		if (args.length < 2)
			System.out.println("USAGE: LanguageTool <langFile1> <langFile2>");
		else
			compareTwoTranslations(args[0], args[1]);
	}

	public static String getLanguageSelected() {
		return languageSelected;
	}
}
