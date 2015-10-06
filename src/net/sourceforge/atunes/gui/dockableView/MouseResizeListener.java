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
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * @author fleax
 *
 */
public class MouseResizeListener implements MouseListener, MouseMotionListener {
	
	public static final int NORTH_RESIZE = 0;
	public static final int EAST_RESIZE = 1;
	public static final int SOUTH_RESIZE = 2;
	public static final int WEST_RESIZE = 3;
	
	public static final int NORTH_WEST_RESIZE = 4;
	public static final int NORTH_EAST_RESIZE = 5;
	public static final int SOUTH_WEST_RESIZE = 6;
	public static final int SOUTH_EAST_RESIZE = 7;
	
	private Point location;

	private Window frameToResize;
	private JPanel panelToResize;
	
	private int orientation;
	
	private int minimunWidth;
	private int minimunHeight; 
	
	private static final int resizeStep = 20;
	
	private DockableWindow frame;
	
	public MouseResizeListener(DockableWindow frame, Window frameToResize, JPanel panelToResize, int orientation) {
		this.frame = frame;
		this.frameToResize = frameToResize;
		this.panelToResize = panelToResize;
		this.orientation = orientation;
	}
	
	public void mousePressed(MouseEvent me) {
		location = me.getPoint();
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent me) {
	}
 
	public void mouseDragged(MouseEvent me) {
		Dimension sizeOfFrame = frameToResize.getSize();
		int x = me.getX() - location.x;
		int y = me.getY() - location.y;
		if (!frame.isHidden()) {
			if (x > resizeStep || x < -resizeStep || y > resizeStep || y < - resizeStep) {
				if (orientation == NORTH_RESIZE) {
					if (sizeOfFrame.height - y >= minimunHeight) {
						frameToResize.setSize(sizeOfFrame.width, sizeOfFrame.height - y);
						frameToResize.setLocation(frameToResize.getLocation().x, frameToResize.getLocation().y + y);
					}
				}
				else if (orientation == SOUTH_RESIZE) {
					if (sizeOfFrame.height + y >= minimunHeight) {
						frameToResize.setSize(sizeOfFrame.width, sizeOfFrame.height + y);
					}
				}
				else if (orientation == EAST_RESIZE) {
					if (sizeOfFrame.width + x >= minimunWidth) {
						frameToResize.setSize(sizeOfFrame.width + x, sizeOfFrame.height);
					}
				}
				else if (orientation == WEST_RESIZE) {
					if (sizeOfFrame.width - x >= minimunWidth) {
						frameToResize.setSize(sizeOfFrame.width - x, sizeOfFrame.height);
						frameToResize.setLocation(frameToResize.getLocation().x + x, frameToResize.getLocation().y);
					}
				}
				else if (orientation == NORTH_WEST_RESIZE) {
					if (sizeOfFrame.width + x >= minimunWidth && sizeOfFrame.height - y >= minimunHeight) {
						frameToResize.setSize(sizeOfFrame.width - x, sizeOfFrame.height - y);
						frameToResize.setLocation(frameToResize.getLocation().x + x, frameToResize.getLocation().y + y);
					}
				}
				else if (orientation == NORTH_EAST_RESIZE) {
					if (sizeOfFrame.width + x >= minimunWidth && sizeOfFrame.height - y >= minimunHeight) {
						frameToResize.setSize(sizeOfFrame.width + x, sizeOfFrame.height - y);
						frameToResize.setLocation(frameToResize.getLocation().x, frameToResize.getLocation().y + y);
					}
				}
				else if (orientation == SOUTH_WEST_RESIZE) {
					if (sizeOfFrame.width - x >= minimunWidth && sizeOfFrame.height + y >= minimunHeight) {
						frameToResize.setSize(sizeOfFrame.width - x, sizeOfFrame.height + y);
						frameToResize.setLocation(frameToResize.getLocation().x + x, frameToResize.getLocation().y);
					}
				}
				else if (orientation == SOUTH_EAST_RESIZE) {
					if (sizeOfFrame.width + x >= minimunWidth && sizeOfFrame.height + y >= minimunHeight) {
						frameToResize.setSize(sizeOfFrame.width + x, sizeOfFrame.height + y);
					}
				}
				panelToResize.setSize(frameToResize.getSize());
				frameToResize.validate();
				frameToResize.repaint();
			}
		}
	 }
 
	public void mouseMoved(MouseEvent me) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}

	public void setMinimunHeight(int minimunHeight) {
		this.minimunHeight = minimunHeight;
	}

	public void setMinimunWidth(int minimunWidth) {
		this.minimunWidth = minimunWidth;
	}


}
