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

package net.sourceforge.atunes.kernel.modules.repository.tags.reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.ID3v2Tag;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;



/**
 * @author fleax
 *
 */
public class ID3v2TagReader extends TagReader {

	protected Tag retrieveTag(AudioFile file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(stream);
		byte[] header = new byte[10];
		bis.read(header);
		String tagHeader = new String(header, 0, 3);
		ID3v2Tag result = null;
		if (tagHeader.equalsIgnoreCase("ID3")) {
			ID3v2Tag tag = new ID3v2Tag();
			int versionMajorNumber = header[3];
			//int versionMinorNumber = header[4];
			int flags = header[5];
			
			int size = get4ByteSynchSafeInteger(new byte[] {header[6], header[7], header[8], header[9]});

			boolean hasExtendedHeader = (flags & 0x40) != 0;
			//boolean hasFooter = (flags & 0x10) != 0;

			// Now read all tag (If footer, it is ignored)
			byte[] tagBytes = new byte[size];
			bis.read(tagBytes);
			
			int framePosition = 0;
			if (hasExtendedHeader) {
				// By now, ignore extended header
				framePosition = framePosition + 10;
			}
			
			int frameIDlength = 4;
			if (versionMajorNumber == 2)
				frameIDlength = 3;
				
			// Begin reading frames
			boolean paddingOrFooter = false;
			while (framePosition + frameIDlength <= tagBytes.length && !paddingOrFooter) {
				int pointer = framePosition;
				String frameID = new String(tagBytes, pointer, frameIDlength);
				if (isValidFrameID(frameID)) {
					pointer = pointer + frameIDlength;
					if (pointer + frameIDlength > size) {
						paddingOrFooter = true;
					}
					else {
						int frameSize;
						if (versionMajorNumber <= 2) 
							frameSize = get3BytesInteger(new byte[] {tagBytes[pointer], tagBytes[pointer+1], tagBytes[pointer+2]});
						else
							frameSize = get4BytesInteger(new byte[] {tagBytes[pointer], tagBytes[pointer+1], tagBytes[pointer+2], tagBytes[pointer+3]});
						
						if (frameSize < size && frameSize > 0) {
							pointer = pointer + frameIDlength;
							try {
								if (versionMajorNumber > 2) {
//									int flag1 = tagBytes[pointer];
//									pointer = pointer + 1;
//									int flag2 = tagBytes[pointer];
//									pointer = pointer + 1;
									pointer = pointer + 2;
								}
								if (frameID.equals("APIC")) {
									tag.setPictureBegin(pointer + 10);
									tag.setPictureLength(frameSize);
								}
								else {
//									byte[] frameContent = new byte[frameSize];
//									System.arraycopy(tagBytes, pointer, frameContent, 0, frameSize);
//									for (int i = pointer; i < pointer + frameSize; i++)
//									frameContent[i - pointer] = tagBytes[i];
									parseFrame(frameID, tagBytes, tag, pointer, frameSize);
								}
								framePosition = pointer + frameSize;
							} catch (Exception e) {
								// incorrect frame size or error --> Abort
								paddingOrFooter = true;
								e.printStackTrace();
							}
						}
						else	
							framePosition++;
					}
				}
				else {
					// If seems that frame syncrhonization is lost, go to next byte
					framePosition++;
				}
			}
			result = tag;
		}
		bis.close();
		return result;
	}
		
	private void parseFrame(String frameID, byte[] frameContent, ID3v2Tag tag, int from, int size) {
		int pointer = 0;
		if (!frameID.equals("COMM")) {
			int encoding = frameContent[from + pointer];
			pointer = pointer + 1;
			if (encoding == 0) {
				String data;
				try {
					data = new String(frameContent, from + pointer, size - 1, CHARSET);
				} catch (UnsupportedEncodingException e) {
					data = new String(frameContent, from + pointer, size - 1);
				}
				
				//pointer = pointer + frameContent.length - 1;
				
				if (data.indexOf(0) != -1) {
					data = data.substring(0, data.indexOf(0));
				}
				tag.addFrame(frameID, data);
			}
		}
		else {
//			int encoding = frameContent[pointer];
//			pointer++;
//			String language = new String(frameContent, pointer, 3).intern();
//			pointer = pointer + 3;
			pointer = pointer + 4;
			String content;
			try {
				content = new String(frameContent, from + pointer, size - pointer, CHARSET);
			}
			catch (UnsupportedEncodingException e) {
				content = new String(frameContent, from + pointer, size - pointer);
			}
			//String description = content.substring(0, content.indexOf(0) > 0 ? content.indexOf(0) : content.length());
			String fullText = content.substring(content.indexOf(0)+1);
			tag.setComment(fullText);
		}
	}
	
	private boolean isValidFrameID(String frameID) {
		for (int i = 0; i < frameID.length(); i++) {
			char c = frameID.charAt(i);
			if (!Character.isUpperCase(c) && !Character.isDigit(c)) return false;
		}
		return true;
	}
	
}
