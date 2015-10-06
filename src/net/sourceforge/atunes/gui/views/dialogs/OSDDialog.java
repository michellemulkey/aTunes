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

package net.sourceforge.atunes.gui.views.dialogs;

import java.awt.Font;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.sourceforge.atunes.gui.ColorDefinitions;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.utils.ImageUtils;



/**
 * @author fleax
 *
 */
public class OSDDialog extends CustomDialog {

	private static final int step = 4;
	
	private static final long serialVersionUID = 8991547440913162267L;
	static int IMAGE_SIZE = 70;
	
	int width = this.getToolkit().getScreenSize().width / 3;
	static int height = 84;
	
	Point locationToStay = new Point(width, this.getToolkit().getScreenSize().height - (int)(height*1.7));
	private Point hiddenLocation = new Point(locationToStay.x, this.getToolkit().getScreenSize().height + 4);
	
	JLabel line1;
	JLabel line2;
	JLabel line3;
	JLabel image;
	
	boolean interrupt;
	
	public OSDDialog() {
		super(null, 0, 0, false);
		setSize(width, height);
		setFocusableWindowState(false);
		setAlwaysOnTop(true);
		setContent(getContent());
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel(null);
		panel.setSize(width, height);
		image = new JLabel();
		image.setBorder(BorderFactory.createLineBorder(ColorDefinitions.GENERAL_BORDER_COLOR));
		image.setOpaque(true);
		line1 = new JLabel();
		line1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		line2 = new JLabel();
		line2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		line3 = new JLabel();
		line3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		
		panel.add(image);
		panel.add(line1);
		panel.add(line2);
		panel.add(line3);
		return panel;
	}
	
	public void showDialog(final ImageIcon img, final String l1, final String l2, final String l3, final long millis) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					if (isVisible()) {
						interrupt = true;
						// Wait until not visible
						while (isVisible()) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
						}
					}

					if (img != null) {
						ImageIcon img2 = ImageUtils.resize(img, IMAGE_SIZE, IMAGE_SIZE);
						image.setIcon(img2);
						image.setSize(img2.getIconWidth(), img2.getIconHeight());
						image.setLocation(10, (height - IMAGE_SIZE) / 2);
					}
					else
						image.setSize(0,0);


					line1.setText(l1);
					if (img != null) {
						line1.setSize(width - 100, 20);
						line1.setLocation(90, 10);
					}
					else {
						line1.setSize(width - 20, 20);
						line1.setLocation(10, 10);
					}
					line1.setHorizontalAlignment(SwingConstants.CENTER);

					line2.setText(l2);
					if (img != null) {
						line2.setSize(width - 100, 20);
						line2.setLocation(90, 32);
					}
					else {
						line2.setSize(width - 20, 20);
						line2.setLocation(10, 32);
					}
					line2.setHorizontalAlignment(SwingConstants.CENTER);

					line3.setText(l3);
					if (img != null) {
						line3.setSize(width - 100, 20);
						line3.setLocation(90, 55);
					}
					else {
						line3.setSize(width - 20, 20);
						line3.setLocation(10, 55);
					}
					line3.setHorizontalAlignment(SwingConstants.CENTER);

					image.setVisible(img != null);
				}
			});
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
		}
		
		if (!interrupt)
			showAnimation(millis);
		
		interrupt = false;
	}
	
	private void showAnimation(long millis) {
		setLocation(Kernel.getInstance().state.ANIMATE_OSD ? hiddenLocation : locationToStay);
		setVisible(true);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					invalidate();
					validate();
					repaint();
				}
			});
		} catch (InterruptedException e1) {
		} catch (InvocationTargetException e1) {
		}
		
		// Animation to show
		if (Kernel.getInstance().state.ANIMATE_OSD) {
			try {
				while (getLocation().y > locationToStay.y && !interrupt) {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							setLocation(locationToStay.x, getLocation().y - step);
						}
					});
					Thread.sleep(10);
				}
			} catch (Exception e) {
			}
		}

		// Show
		try {
			long sleeps = millis / 10;
			for (int i = 0; i < sleeps && !interrupt; i++)
				Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		
		// Animation to hide
		if (Kernel.getInstance().state.ANIMATE_OSD) {
			try {
				while (getLocation().y < hiddenLocation.y && !interrupt) {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							setLocation(locationToStay.x, getLocation().y + step);
						}
					});
					Thread.sleep(10);
				}
			} catch (Exception e) {
			}
		}
		
		setVisible(false);
	}
	
	public static void main(String[] args) {
		new OSDDialog().showDialog(null, "Test", "Test 2", "Test 3", 3000);
	}
}
