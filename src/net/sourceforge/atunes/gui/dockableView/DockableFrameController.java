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

package net.sourceforge.atunes.gui.dockableView;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

public class DockableFrameController {

	public static final int NONE = -1;
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int EAST = 3;
	
	private DockableFrame mainFrame;
	
	private CloseListener closeListener;
	ArrayList<DockableWindow> frames;
	private DockFramePositionListener listener = new DockFramePositionListener() {
		public void positionChanged(DockableWindow frame, java.awt.Rectangle r, int oldXLocation, int oldYLocation) {
			if (frames.size() > 1) {
				for (int i = 0; i < frames.size(); i++) {
					DockableWindow f = frames.get(i);
					if (frame != f) {
						if (!f.getOutsideBounds().intersects(r) || f.getInsideBounds().intersects(r)) {
							frame.setLocation(r.x, r.y);
						}
						else {
							int relX = frame.getBounds().x - f.getBounds().x;
							int relY = frame.getBounds().y - f.getBounds().y;

							int locationX = f.getBounds().x + (relX > 0 ? f.getBounds().width - 1 : - frame.getBounds().width + 1);
							int locationY = f.getBounds().y + (relY > 0 ? f.getBounds().height - 1: - frame.getBounds().height + 1);
							
							int deltaX = locationX - r.x;
							int deltaY = locationY - r.y;
							
							if (Math.abs(deltaX) > Math.abs(deltaY))
								locationX = r.x;
							else
								locationY = r.y;

							frame.setLocation(locationX, locationY);
						}
						break;
					}
				}
			}
			else
				frame.setLocation(r.x, r.y);
		}
	};
	
	public DockableFrameController(CloseListener closeListener) {
		frames = new ArrayList<DockableWindow>();
		this.closeListener = closeListener;
	}

	public DockableFrame getNewFrame(String title, int width, int height, DockableWindow relative, int position, Dimension minSize) {
		DockableFrame frame = new DockableFrame(width, height, minSize, listener, closeListener);
		if (mainFrame == null)
			mainFrame = frame;
		frame.setTitle(title);
		if (frames.isEmpty() || position == NONE)
			frame.setLocationRelativeTo(null);
		else {
			Rectangle relativeFrameBounds = relative.getBounds();
			if (position == NORTH)
				frame.setLocation(relativeFrameBounds.x, relativeFrameBounds.y - height - 1);
			else if (position == SOUTH)
				frame.setLocation(relativeFrameBounds.x, relativeFrameBounds.y + relativeFrameBounds.height - 1);
			else if (position == EAST)
				frame.setLocation(relativeFrameBounds.x + relativeFrameBounds.width - 1, relativeFrameBounds.y);
			else
				frame.setLocation(relativeFrameBounds.x - width + 1, relativeFrameBounds.y);
		}
		
		frames.add(frame);
		return frame;
	}
	
	public DockableDialog getNewDialog(DockableFrame parent, String title, int width, int height, DockableWindow relative, int position, Dimension minSize) {
		DockableDialog frame = new DockableDialog(parent, width, height, minSize, listener);
		frame.setTitle(title);
		if (frames.isEmpty() || position == NONE)
			frame.setLocation(50, 50);
		else {
			Rectangle relativeFrameBounds = relative.getBounds();
			if (position == NORTH)
				frame.setLocation(relativeFrameBounds.x, relativeFrameBounds.y - height - 1);
			else if (position == SOUTH)
				frame.setLocation(relativeFrameBounds.x, relativeFrameBounds.y + relativeFrameBounds.height - 1);
			else if (position == EAST)
				frame.setLocation(relativeFrameBounds.x + relativeFrameBounds.width - 1, relativeFrameBounds.y);
			else
				frame.setLocation(relativeFrameBounds.x - width + 1, relativeFrameBounds.y);
		}
		
		frames.add(frame);
		return frame;
	}
	
	
}
