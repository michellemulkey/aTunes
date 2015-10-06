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

package net.sourceforge.atunes.gui.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

public class TransferableArrayList implements Transferable {
	
	public static final String mimeType = "aTunes/objects; class=java.io.InputStream";
	
	private ArrayList list;
	
	public TransferableArrayList(ArrayList list) {
		this.list = list;
	}
	
	public ArrayList getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return list;
	}
	
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = new DataFlavor[1];
		try {
			flavors[0] = new DataFlavor(mimeType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return flavors;
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.getMimeType().equalsIgnoreCase(mimeType);
	}
}
