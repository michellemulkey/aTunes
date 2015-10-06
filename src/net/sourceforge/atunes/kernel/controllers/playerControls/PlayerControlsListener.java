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

package net.sourceforge.atunes.kernel.controllers.playerControls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.panels.PlayerControlsPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;


public class PlayerControlsListener extends MouseAdapter implements ActionListener, ChangeListener {

	private PlayerControlsPanel panel;
	private PlayerControlsController controller;
	
	protected PlayerControlsListener(PlayerControlsPanel panel, PlayerControlsController controller) {
		this.panel = panel;
		this.controller = controller;
	}
	
	public void mouseClicked(MouseEvent e) {
//		if (e.getSource().equals(panel.getProgressBar())) {
//			int widthClicked = e.getPoint().x;
//			int widthOfProgressBar = panel.getProgressBar().getSize().width;
//			double perCentOfSong = (double)widthClicked / (double)widthOfProgressBar;
//			HandlerProxy.getPlayerHandler().seek(perCentOfSong);
//		}
		if (e.getSource().equals(panel.getPlayButton()))
			HandlerProxy.getPlayerHandler().play(true);
		else if (e.getSource().equals(panel.getStopButton()))
			HandlerProxy.getPlayerHandler().stop();
		else if (e.getSource().equals(panel.getNextButton()))
			HandlerProxy.getPlayerHandler().next(false);
		else if (e.getSource().equals(panel.getPreviousButton()))
			HandlerProxy.getPlayerHandler().previous();
	}
		
	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(panel.getProgressBar())) {
//			int value = ((JSlider)e.getSource()).getValue();
//			double perCent = (double) value / ((JSlider)e.getSource()).getMaximum();
			//double perCentOfSong = value * 1000000 / duration;
			int widthClicked = e.getPoint().x;
			int widthOfProgressBar = panel.getProgressBar().getSize().width;
			double perCent = (double)widthClicked / (double)widthOfProgressBar;
			HandlerProxy.getPlayerHandler().seek(perCent);
		}
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(panel.getVolumeSlider())) {
			int value = ((JSlider)e.getSource()).getValue();
			if (!panel.getVolumeButton().isSelected()) {
				if (value > 80)
					panel.getVolumeButton().setIcon(ImageLoader.VOlUME_MAX);
				else if (value > 40)
					panel.getVolumeButton().setIcon(ImageLoader.VOLUME_MED);
				else if (value > 5)
					panel.getVolumeButton().setIcon(ImageLoader.VOLUME_MIN);
				else 
					panel.getVolumeButton().setIcon(ImageLoader.VOLUME_ZERO);
			}
			HandlerProxy.getPlayerHandler().setVolume(value);
			controller.setVolume(value);
		}
		else if (e.getSource().equals(panel.getBalanceSlider())) {
			int value = ((JSlider)e.getSource()).getValue();
			if (value > 4)
				panel.getBalanceLabel().setIcon(ImageLoader.BALANCE_RIGHT);
			else if (value > 0)
				panel.getBalanceLabel().setIcon(ImageLoader.BALANCE_MED_RIGHT);
			else if (value == 0)
				panel.getBalanceLabel().setIcon(ImageLoader.BALANCE_MED);
			else if(value > -4)
				panel.getBalanceLabel().setIcon(ImageLoader.BALANCE_MED_LEFT);
			else
				panel.getBalanceLabel().setIcon(ImageLoader.BALANCE_LEFT);
			
			HandlerProxy.getPlayerHandler().setBalance(value);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(panel.getShuffleButton())) {
			boolean shuffle = panel.getShuffleButton().isSelected();
			Kernel.getInstance().state.setShuffle(shuffle);
			HandlerProxy.getPlayerHandler().setShuffle(shuffle);
			controller.setShuffle(shuffle);
			HandlerProxy.getSystemTrayHandler().setShuffle(shuffle);
		}
		else if (e.getSource().equals(panel.getRepeatButton())) {
			boolean repeat = panel.getRepeatButton().isSelected();
			Kernel.getInstance().state.setRepeat(repeat);
			HandlerProxy.getPlayerHandler().setRepeat(repeat);
			controller.setRepeat(repeat);
			HandlerProxy.getSystemTrayHandler().setRepeat(repeat);
		}
		else if (e.getSource().equals(panel.getVolumeButton())) {
			controller.setMute(panel.getVolumeButton().isSelected());
		}
	}

}
