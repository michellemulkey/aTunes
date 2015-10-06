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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;


public abstract class DockableWindow implements MouseMotionListener, MouseListener {
	
	protected Point location;
	protected MouseEvent pressed;
	
	protected JPanel container;
	protected Window frame;
	protected JPanel panel;
	protected JLabel title;
	protected JPanel topBar;
	
	protected static int resizableBorder = 2;
	
	protected static int dockableBounds = 10;
	
	protected DockFramePositionListener listener;
	
	protected boolean hidden;
	protected int height;
	
	protected JPanel northPanel;
	protected JPanel nePanel;
	protected JPanel nwPanel;
	protected JPanel eastPanel;
	protected JPanel westPanel;
	protected JPanel southPanel;
	protected JPanel sePanel;
	protected JPanel swPanel;
	protected JPanel titlePanel;
	
	protected JPopupMenu menu;
	
	private Dimension minimumSize;
	
	private static final int defaultWidthAndHeight = 150;
	
	public DockableWindow(DockFramePositionListener listener, Dimension minimumSize) {
		this.listener = listener;
		this.minimumSize = minimumSize;
		panel = new JPanel(new GridBagLayout());
		container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		container.add(panel, BorderLayout.CENTER);
	}
	
	protected void addResizableBorders() {
		addNorthResizableBorder();
		addEastResizableBorder();
		addSouthResizableBorder();
		addWestResizableBorder();
	}
	
	protected void addNorthResizableBorder() {
		northPanel = new JPanel();
		northPanel.setPreferredSize(new Dimension(10,2));
		
		titlePanel = new JPanel(new BorderLayout());
		title = new JLabel();
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
		titlePanel.add(title, BorderLayout.CENTER);
				
		MouseResizeListener northResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.NORTH_RESIZE);
		northResizeListener.setMinimunWidth(getMinimumWidth());
		northResizeListener.setMinimunHeight(getMinimumHeight());
		northPanel.addMouseListener(northResizeListener);
		northPanel.addMouseMotionListener(northResizeListener);
		
		nwPanel = new JPanel();
		nwPanel.setPreferredSize(new Dimension(resizableBorder, resizableBorder));
		
		MouseResizeListener nwResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.NORTH_WEST_RESIZE);
		nwResizeListener.setMinimunWidth(getMinimumWidth());
		nwResizeListener.setMinimunHeight(getMinimumHeight());
		nwPanel.addMouseListener(nwResizeListener);
		nwPanel.addMouseMotionListener(nwResizeListener);
		
		nePanel = new JPanel();
		nePanel.setPreferredSize(new Dimension(resizableBorder, resizableBorder));
		
		MouseResizeListener neResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.NORTH_EAST_RESIZE);
		neResizeListener.setMinimunWidth(getMinimumWidth());
		neResizeListener.setMinimunHeight(getMinimumHeight());
		nePanel.addMouseListener(neResizeListener);
		nePanel.addMouseMotionListener(neResizeListener);

		topBar = new JPanel(new BorderLayout());
		topBar.add(nwPanel, BorderLayout.WEST);
		topBar.add(northPanel, BorderLayout.NORTH);
		topBar.add(nePanel, BorderLayout.EAST);
		topBar.add(titlePanel, BorderLayout.CENTER);
		nwPanel.setOpaque(false);
		northPanel.setOpaque(false);
		nePanel.setOpaque(false);
		titlePanel.setOpaque(false);
		topBar.setPreferredSize(new Dimension(20,12));
		
		topBar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.gray));
		container.add(topBar, BorderLayout.NORTH);
	}

	private void addEastResizableBorder() {
		eastPanel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(resizableBorder,10));
		
		container.add(eastPanel, BorderLayout.EAST);
		
		MouseResizeListener eastResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.EAST_RESIZE);
		eastResizeListener.setMinimunWidth(getMinimumWidth());
		eastResizeListener.setMinimunHeight(getMinimumHeight());
		
		eastPanel.addMouseListener(eastResizeListener);
		eastPanel.addMouseMotionListener(eastResizeListener);
		
		eastPanel.setOpaque(true);
	}
	
	private void addWestResizableBorder() {
		westPanel = new JPanel();
		westPanel.setPreferredSize(new Dimension(resizableBorder,10));
		
		container.add(westPanel, BorderLayout.WEST);

		MouseResizeListener westResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.WEST_RESIZE);
		westResizeListener.setMinimunWidth(getMinimumWidth());
		westResizeListener.setMinimunHeight(getMinimumHeight());
		westPanel.addMouseListener(westResizeListener);
		westPanel.addMouseMotionListener(westResizeListener);
		
		westPanel.setOpaque(true);
	}
	
	private void addSouthResizableBorder() {
		southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(10,resizableBorder));
		

		MouseResizeListener southResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.SOUTH_RESIZE);
		southResizeListener.setMinimunWidth(getMinimumWidth());
		southResizeListener.setMinimunHeight(getMinimumHeight());
		
		southPanel.addMouseListener(southResizeListener);
		southPanel.addMouseMotionListener(southResizeListener);
		
		swPanel = new JPanel();
		swPanel.setPreferredSize(new Dimension(resizableBorder, resizableBorder));
		
		MouseResizeListener swResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.SOUTH_WEST_RESIZE);
		swResizeListener.setMinimunWidth(getMinimumWidth());
		swResizeListener.setMinimunHeight(getMinimumHeight());
		swPanel.addMouseListener(swResizeListener);
		swPanel.addMouseMotionListener(swResizeListener);
		
		sePanel = new JPanel();
		sePanel.setPreferredSize(new Dimension(resizableBorder, resizableBorder));
		
		MouseResizeListener seResizeListener = new MouseResizeListener(this, frame, container, MouseResizeListener.SOUTH_EAST_RESIZE);
		seResizeListener.setMinimunWidth(getMinimumWidth());
		seResizeListener.setMinimunHeight(getMinimumHeight());
		sePanel.addMouseListener(seResizeListener);
		sePanel.addMouseMotionListener(seResizeListener);

		JPanel auxPanel = new JPanel(new BorderLayout());
		auxPanel.add(swPanel, BorderLayout.WEST);
		auxPanel.add(southPanel, BorderLayout.CENTER);
		auxPanel.add(sePanel, BorderLayout.EAST);
		
		container.add(auxPanel, BorderLayout.SOUTH);
		
		auxPanel.setOpaque(false);
	}
	
	private int getMinimumWidth() {
		return minimumSize == null ? defaultWidthAndHeight : minimumSize.width;
	}
	
	private int getMinimumHeight() {
		return minimumSize == null ? defaultWidthAndHeight : minimumSize.height;
	}
	
	public void addContent(JPanel panel) {
		panel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.BOTH; c.weightx = 1; c.weighty = 1;
		this.panel.add(panel, c);
	}

	public void setTitleBarBackground(Color c) {
		topBar.setBackground(c);
	}
	
	public void setBackground(Color c) {
		panel.setBackground(c);
		eastPanel.setBackground(c);
		westPanel.setBackground(c);
		southPanel.setBackground(c);
		swPanel.setBackground(c);
		sePanel.setBackground(c);
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public abstract void setTitle(String s);
	
	public abstract void setIcon(Image i);

	public boolean isHidden() {
		return hidden;
	}
	
	protected void setHidden(boolean value) {
		hidden = value;
		if (hidden) {
			northPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			nwPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			nePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			eastPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			westPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			southPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			swPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			sePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		else {
			northPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			nwPanel.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			nePanel.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			eastPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			westPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			southPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			swPanel.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			sePanel.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		}
	}

	public Rectangle getInsideBounds() {
		Rectangle r = frame.getBounds();
		r.x = r.x + dockableBounds;
		r.y = r.y + dockableBounds;
		r.width = r.width - dockableBounds*2;
		r.height = r.height - dockableBounds*2;
		return r;
	}
	
	public Rectangle getOutsideBounds() {
		Rectangle r = frame.getBounds();
		r.x = r.x - dockableBounds;
		r.y = r.y - dockableBounds;
		r.width = r.width + dockableBounds * 2;
		r.height = r.height + dockableBounds * 2;
		return r;
	}
	
	public Point getLocation() {
		return frame.getLocation();
	}
	
	public boolean isVisible() {
		return frame.isVisible();
	}

	public abstract void setLocationRelativeTo(Component c);
	
	public abstract void setDefaultCloseOperation(int op);
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	public Rectangle getBounds() {
		return frame.getBounds();
	}

	public void setLocation(int x, int y) {
		frame.setLocation(x, y);
	}
	
	public void mouseDragged(MouseEvent me) {
		location = frame.getLocation(location);
		int x = location.x - pressed.getX() + me.getX();
		int y = location.y - pressed.getY() + me.getY();
		
		// Sticky borders
		int screenWidth = frame.getToolkit().getScreenSize().width;
		int screenHeight = frame.getToolkit().getScreenSize().height;
		x = (x < 10) ? 0 : x;
		x = (screenWidth - (x + frame.getSize().width) < 10) ? screenWidth - frame.getSize().width : x; 
		y = (y < 10) ? 0 : y;
		y = (screenHeight - (y + frame.getSize().height) < 10) ? screenHeight - frame.getSize().height : y;
			
		if (listener != null)
			listener.positionChanged(this, new Rectangle(x, y, frame.getWidth(), frame.getHeight()), location.x, location.y);
		else
			frame.setLocation(x, y);
	}

	public void mousePressed(MouseEvent e) {
		pressed = e;
	}
	
	public void mouseMoved(MouseEvent e) {
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			setHidden(!hidden);
			panel.setVisible(!hidden);
			if (hidden) {
				height = frame.getHeight();
				frame.setSize(frame.getWidth(), 13);
			}
			else {
				frame.setSize(frame.getWidth(), height);
			}
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (menu != null)
				menu.show(frame, e.getX(), e.getY());
		}
	}

	public void setMenu(JPopupMenu menu) {
		northPanel.add(menu);
		this.menu = menu;
	}

}
