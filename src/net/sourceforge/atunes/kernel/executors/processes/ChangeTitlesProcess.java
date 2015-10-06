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

package net.sourceforge.atunes.kernel.executors.processes;

import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.writer.TagModifier;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class ChangeTitlesProcess implements Runnable {

	//private Logger logger = Logger.getLogger(ChangeTitlesProcess.class);
	private HashMap<AudioFile, String> filesAndTitles;
	
	public ChangeTitlesProcess(HashMap<AudioFile, String> filesAndTitles) {
		super();
		this.filesAndTitles = filesAndTitles;
	}
	
	public void run() {
		ArrayList<AudioFile> filesToEdit = new ArrayList<AudioFile>();
		HandlerProxy.getVisualHandler().showIndeterminateProgressDialog(LanguageTool.getString("PERFORMING_CHANGES") + "...");
		for (AudioFile file : filesAndTitles.keySet()) {
			String newTitle = filesAndTitles.get(file);
			TagModifier.setTitles(file, newTitle);
			filesToEdit.add(file);
		}
		TagModifier.refreshAfterTagModify(filesToEdit);
		HandlerProxy.getVisualHandler().hideIndeterminateProgressDialog();
	}
}
