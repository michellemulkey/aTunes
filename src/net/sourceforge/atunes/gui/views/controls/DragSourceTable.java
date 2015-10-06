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

import javax.swing.JTable;
import javax.swing.table.TableModel;

import net.sourceforge.atunes.gui.model.TransferableArrayList;


public class DragSourceTable extends JTable implements DragSourceListener, DragGestureListener {
	
	private static final long serialVersionUID = -607346309523708685L;

	private DragSource dragSource;
	
	public DragSourceTable() {
		super();
		setShowGrid(false);
		setDragSource();
	}
	
	public DragSourceTable(TableModel model) {
		this();
		setModel(model);
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
		int[] rows = getSelectedRows();
		ArrayList<Integer> rowsToDrag = new ArrayList<Integer>();
		for (int i = 0; i < rows.length; i++) {
			rowsToDrag.add(rows[i]);
		}
		TransferableArrayList items = new TransferableArrayList(rowsToDrag);
		dragSource.startDrag(dge, DragSource.DefaultCopyDrop, items, this);
	}
}
