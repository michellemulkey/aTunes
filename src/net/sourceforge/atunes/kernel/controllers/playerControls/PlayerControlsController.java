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

import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.panels.PlayerControlsPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.model.PanelController;
import net.sourceforge.atunes.utils.TimeUtils;

public class PlayerControlsController extends PanelController {

	public PlayerControlsController(PlayerControlsPanel panel) {
		super(panel);
		addBindings();
		addStateBindings();
	}
	
	protected void addBindings() {
		PlayerControlsPanel panel = (PlayerControlsPanel) panelControlled;
		PlayerControlsListener listener = new PlayerControlsListener(panel, this);
		
		panel.getProgressBar().addMouseListener(listener);
		panel.getShuffleButton().addActionListener(listener);
		panel.getRepeatButton().addActionListener(listener);
		panel.getPlayButton().addMouseListener(listener);
		panel.getStopButton().addMouseListener(listener);
		panel.getNextButton().addMouseListener(listener);
		panel.getPreviousButton().addMouseListener(listener);
		panel.getVolumeButton().addActionListener(listener);
		panel.getVolumeSlider().addChangeListener(listener);
		panel.getBalanceSlider().addChangeListener(listener);
	}
	
	protected void addStateBindings() {
		((PlayerControlsPanel)panelControlled).getShuffleButton().setSelected(Kernel.getInstance().state.isShuffle());
		((PlayerControlsPanel)panelControlled).getRepeatButton().setSelected(Kernel.getInstance().state.isRepeat());
	}

	protected void notifyReload() {}
	
	public void setTime(long time, long totalTime) {
		long remainingTime = totalTime - time;
		((PlayerControlsPanel)panelControlled).getTime().setText(TimeUtils.milliseconds2String(time));
		if (time == 0)
			((PlayerControlsPanel)panelControlled).getRemainingTime().setText(TimeUtils.milliseconds2String(time));
		else
			((PlayerControlsPanel)panelControlled).getRemainingTime().setText("- " + TimeUtils.milliseconds2String(remainingTime));
		((PlayerControlsPanel)panelControlled).getProgressBar().setValue((int) time);
	}
	
	public void setMaxDuration(long maxDuration) {
		((PlayerControlsPanel)panelControlled).getProgressBar().setMaximum((int)maxDuration);
	}
	
	protected void setVolume(int value) {
		((PlayerControlsPanel)panelControlled).getVolumeSlider().setValue(value);
	}
	
	public void setShuffle(boolean shuffle) {
		((PlayerControlsPanel)panelControlled).getShuffleButton().setSelected(shuffle);
	}
	
	public void setRepeat(boolean repeat) {
		((PlayerControlsPanel)panelControlled).getRepeatButton().setSelected(repeat);
	}
	
	public void setMute(boolean mute) {
		((PlayerControlsPanel)panelControlled).getVolumeButton().setSelected(mute);
		if (mute) {
			((PlayerControlsPanel)panelControlled).getVolumeButton().setIcon(ImageLoader.VOLUME_MUTE);
		}
		else {
			int value = ((PlayerControlsPanel)panelControlled).getVolumeSlider().getValue();
			if (value > 80)
				((PlayerControlsPanel)panelControlled).getVolumeButton().setIcon(ImageLoader.VOlUME_MAX);
			else if (value > 40)
				((PlayerControlsPanel)panelControlled).getVolumeButton().setIcon(ImageLoader.VOLUME_MED);
			else if (value > 5)
				((PlayerControlsPanel)panelControlled).getVolumeButton().setIcon(ImageLoader.VOLUME_MIN);
			else 
				((PlayerControlsPanel)panelControlled).getVolumeButton().setIcon(ImageLoader.VOLUME_ZERO);
		}
		HandlerProxy.getSystemTrayHandler().setMute(mute);
		HandlerProxy.getPlayerHandler().setMute(mute);
	}
	
	public void setPlaying(boolean playing) {
		((PlayerControlsPanel)panelControlled).setPlaying(playing);
	}
}
