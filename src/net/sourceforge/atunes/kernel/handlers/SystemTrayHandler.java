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

package net.sourceforge.atunes.kernel.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;
import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;


public class SystemTrayHandler {

	private static Logger logger = Logger.getLogger(SystemTrayHandler.class);
	
	private boolean trayInitialized;
	
	private boolean trayIconVisible;
	private boolean trayPlayerVisible;
	
	private SystemTray tray;
	Kernel kernel;
	private TrayIcon trayIcon;
	private TrayIcon previousIcon;
	private TrayIcon playIcon;
	private TrayIcon stopIcon;
	private TrayIcon nextIcon;
	
	private JMenuItem playMenu;
	
	JCheckBoxMenuItem mute;
	JCheckBoxMenuItem shuffle;
	JCheckBoxMenuItem repeat;
	JCheckBoxMenuItem showOSD;
	
	public SystemTrayHandler() {
		this.kernel = Kernel.getInstance();
	}
	
	private void initSystemTray() {
		if (!trayInitialized) {
			tray = SystemTray.getDefaultSystemTray();
			trayInitialized = true;
		}
	}
	
	public void initTrayIcon() {
		initSystemTray();
		if (tray != null) {
			trayIconVisible = true;
			trayIcon = new TrayIcon(ImageLoader.APP_ICON, Constants.APP_NAME + ' ' + Constants.APP_VERSION_NUMBER, getMenu());
			tray.addTrayIcon(trayIcon);
			trayIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getVisualHandler().toggleWindowVisibility();
				}
			});
			HandlerProxy.getVisualHandler().setFrameDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
		else {
			logger.error("No system tray supported");
		}
	}
	
	public void initTrayPlayerIcons() {
		initSystemTray();
		if (tray != null) {
			trayPlayerVisible = true;
			nextIcon = new TrayIcon(ImageLoader.NEXT_TRAY);
			nextIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getPlayerHandler().next(false);
				}
			});
			tray.addTrayIcon(nextIcon);
			
			stopIcon = new TrayIcon(ImageLoader.STOP_TRAY);
			stopIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getPlayerHandler().stop();
				}
			});
			tray.addTrayIcon(stopIcon);
						
			playIcon = new TrayIcon(ImageLoader.PLAY_TRAY);
			playIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getPlayerHandler().play(true);
				}
			});
			tray.addTrayIcon(playIcon);
			
			previousIcon = new TrayIcon(ImageLoader.PREVIOUS_TRAY);
			previousIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getPlayerHandler().previous();
				}
			});
			tray.addTrayIcon(previousIcon);
		} else {
			logger.error("No system tray supported");
		}
	}
	
	private void trayIconAdvice() {
		trayIcon.displayMessage(Constants.APP_NAME, LanguageTool.getString("TRAY_ICON_MESSAGE"), TrayIcon.INFO_MESSAGE_TYPE);
	}
	
	private JPopupMenu getMenu() {
		JPopupMenu menu = new JPopupMenu();
		
		playMenu = new JMenuItem(LanguageTool.getString("PLAY"));
		playMenu.setOpaque(false);
		playMenu.setFont(FontSingleton.MENU_FONT);
		playMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HandlerProxy.getPlayerHandler().play(true);
			}
		});
		menu.add(playMenu);

		JMenuItem stop = new JMenuItem(LanguageTool.getString("STOP"));
		stop.setFont(FontSingleton.MENU_FONT);
		stop.setOpaque(false);
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HandlerProxy.getPlayerHandler().stop();
			}
		});
		menu.add(stop);
		
		JMenuItem previous = new JMenuItem(LanguageTool.getString("PREVIOUS"));
		previous.setFont(FontSingleton.MENU_FONT);
		previous.setOpaque(false);
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HandlerProxy.getPlayerHandler().previous();
			}
		});
		menu.add(previous);

		JMenuItem next = new JMenuItem(LanguageTool.getString("NEXT"));
		next.setFont(FontSingleton.MENU_FONT);
		next.setOpaque(false);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HandlerProxy.getPlayerHandler().next(false);
			}
		});
		menu.add(next);
		
		menu.add(new JSeparator());

		mute = new JCheckBoxMenuItem(LanguageTool.getString("MUTE"));
		mute.setFont(FontSingleton.MENU_FONT);
		mute.setOpaque(false);
		mute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HandlerProxy.getControllerHandler().getPlayerControlsController().setMute(mute.isSelected());
			}
		});
		menu.add(mute);
		
		menu.add(new JSeparator());
		
		shuffle = new JCheckBoxMenuItem(LanguageTool.getString("SHUFFLE"));
		shuffle.setFont(FontSingleton.MENU_FONT);
		shuffle.setOpaque(false);
		shuffle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kernel.state.setShuffle(shuffle.isSelected());
				HandlerProxy.getControllerHandler().getPlayerControlsController().setShuffle(shuffle.isSelected());
				HandlerProxy.getPlayerHandler().setShuffle(shuffle.isSelected());
			}
		});
		menu.add(shuffle);
		
		repeat = new JCheckBoxMenuItem(LanguageTool.getString("REPEAT"));
		repeat.setFont(FontSingleton.MENU_FONT);
		repeat.setOpaque(false);
		repeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kernel.state.setRepeat(repeat.isSelected());
				HandlerProxy.getControllerHandler().getPlayerControlsController().setRepeat(repeat.isSelected());
				HandlerProxy.getPlayerHandler().setRepeat(repeat.isSelected());
			}
		});
		menu.add(repeat);
		
		menu.add(new JSeparator());

		showOSD = new JCheckBoxMenuItem(LanguageTool.getString("SHOW_OSD"));
		showOSD.setFont(FontSingleton.MENU_FONT);
		showOSD.setOpaque(false);
		showOSD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kernel.state.setShowOSD(showOSD.isSelected());
				HandlerProxy.getControllerHandler().getMenuController().setShowOSD(showOSD.isSelected());
			}
		});
		menu.add(showOSD);
		
		menu.add(new JSeparator());

		JMenuItem about = new JMenuItem(LanguageTool.getString("ABOUT"));
		about.setFont(FontSingleton.MENU_FONT);
		about.setOpaque(false);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HandlerProxy.getVisualHandler().showAboutDialog();
			}
		});
		menu.add(about);

		menu.add(new JSeparator());
		
		JMenuItem exit = new JMenuItem(LanguageTool.getString("EXIT"));
		exit.setFont(FontSingleton.MENU_FONT);
		exit.setOpaque(false);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				kernel.finish();
			}
		});
		menu.add(exit);
		
		return menu;
	}
	
	public void setTrayIconVisible(boolean visible) {
		if (visible && !trayIconVisible) {
			initTrayIcon();
			if (tray != null)
				trayIconAdvice();
		}
		else {
			if (!visible && trayIconVisible) {
				tray.removeTrayIcon(trayIcon);
				HandlerProxy.getVisualHandler().setFrameDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				trayIconVisible = false;
			}
		}
	}
	
	public void setTrayPlayerVisible(boolean visible) {
		if (visible && !trayPlayerVisible) {
			initTrayPlayerIcons();
		}
		else {
			if (!visible && trayPlayerVisible) {
				tray.removeTrayIcon(previousIcon);
				tray.removeTrayIcon(playIcon);
				tray.removeTrayIcon(stopIcon);
				tray.removeTrayIcon(nextIcon);
				trayPlayerVisible = false;
			}
		}
	}
	
	public void setTrayToolTip(String msg) {
		if (trayIcon != null)
			trayIcon.setCaption(msg);
	}
	
	public void setShuffle(boolean value) {
		if (shuffle != null)
			shuffle.setSelected(value);
	}
	
	public void setRepeat(boolean value) {
		if (repeat != null)
			repeat.setSelected(value);
	}
	
	public void setShowOSD(boolean value) {
		if (showOSD != null)
			showOSD.setSelected(value);
	}

	public void setMute(boolean value) {
		if (mute != null)
			mute.setSelected(value);
	}
	
	public void setPlaying(boolean playing) {
		if (playing) {
			if (trayIcon != null)
				playMenu.setText(LanguageTool.getString("PAUSE"));
			if (playIcon != null)
				playIcon.setIcon(ImageLoader.PAUSE_TRAY);
		}
		else {
			if (trayIcon != null)
				playMenu.setText(LanguageTool.getString("PLAY"));
			if (playIcon != null)
				playIcon.setIcon(ImageLoader.PLAY_TRAY);
		}
		
	}
}
