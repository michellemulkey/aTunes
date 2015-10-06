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

package net.sourceforge.atunes.gui.views.controls;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import net.sourceforge.atunes.gui.model.TransferableArrayList;


public class DragSourceTree extends JTree  implements DragSourceListener, DragGestureListener {
	
	private static final long serialVersionUID = 5130815364968225924L;

	private DragSource dragSource;
	
	public DragSourceTree(TreeModel model) {
		super(model);
		setDragSource();
	}
	
	private void setDragSource() {
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);
	}
	
	public void dragDropEnd(DragSourceDropEvent dsde) {}
	public void dragEnter(DragSourceDragEvent dsde) {}
	public void dragExit(DragSourceEvent dse) {}
	public void dragOver(DragSourceDragEvent dsde) {}
	public void dropActionChanged(DragSourceDragEvent dsde) {}
	
	public void dragGestureRecognized(DragGestureEvent dge) {
		TreePath[] paths = getSelectionPaths();
		ArrayList<Object> itemsToDrag = new ArrayList<Object>();
		for (int i = 0; i < paths.length; i++) {
			Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
			itemsToDrag.add(obj);
		}
		TransferableArrayList items = new TransferableArrayList(itemsToDrag);
		dragSource.startDrag(dge, DragSource.DefaultCopyDrop, items, this);
	}
}
