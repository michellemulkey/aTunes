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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v2Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.writer.TagModifier;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class ChangeTagsProcess implements Runnable { 
	
	//private Logger logger = Logger.getLogger(ChangeTagsProcess.class);
	
	private ArrayList<AudioFile> filesToEdit;
	private HashMap<String, ?> properties;
	
	public ChangeTagsProcess(ArrayList<AudioFile> filesToEdit, HashMap<String, ?> properties) {
		super();
		this.filesToEdit = filesToEdit;
		this.properties = properties;
	}
	
	public void run() {
		HandlerProxy.getVisualHandler().showIndeterminateProgressDialog(LanguageTool.getString("PERFORMING_CHANGES") + "...");
		
		boolean hasPicture = (Boolean) properties.get("HAS_PICTURE");
		File picture = (File) properties.get("PICTURE");
		for (int i = 0; i < filesToEdit.size(); i++) {
			Tag newTag = AudioFile.getNewTag(filesToEdit.get(i), properties);
			Tag oldTag = (filesToEdit.get(i)).getTag();
			if (filesToEdit.size() > 1) {
				if (oldTag != null) {
					newTag.setTitle(filesToEdit.get(i).getTag().getTitle());
					if (oldTag instanceof ID3v2Tag)
						((ID3v2Tag)newTag).setTrackNumber(((ID3v2Tag)oldTag).getTrackNumber());
				}
				else {
					newTag.setTitle(null);
				}
			}
			TagModifier.setInfo(filesToEdit.get(i), newTag, picture, hasPicture);
		}

		TagModifier.refreshAfterTagModify(filesToEdit);
		HandlerProxy.getVisualHandler().hideIndeterminateProgressDialog();
	}
}
