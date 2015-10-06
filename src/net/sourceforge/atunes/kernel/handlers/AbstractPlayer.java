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

import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.controllers.playList.PlayListController;
import net.sourceforge.atunes.model.player.PlayList;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;

public abstract class AbstractPlayer {

	protected static final int PREVIOUS = 1;
	protected static final int PLAY = 2;
	protected static final int STOP = 3;
	protected static final int NEXT = 4;
	protected static final int SEEK = 5;
	
	protected int lastButtonPressed = -1;

	protected boolean muted;

	
	protected Logger logger = Logger.getLogger(AbstractPlayer.class);
	
	protected PlayList currentPlayList;
	
	long currentDuration;
	
	protected int volume = 50; // Per cent
	protected float balance; // -1, +1

	
	protected boolean paused;
	
	protected PlayListController playListController;
	
	public AbstractPlayer() {
		this.playListController = HandlerProxy.getControllerHandler().getPlayListController();
		currentPlayList = new PlayList();
		HandlerProxy.getVisualHandler().updateStatusBar(LanguageTool.getString("STOPPED"));
	}
	
	public abstract void play(boolean buttonPressed);
	public abstract void stop();
	public abstract void next(boolean autoNext);
	public abstract void previous();
	public abstract void seek(double position);
	
	public final PlayList getCurrentPlayList() {
		return currentPlayList;
	}
	
	public abstract void finish();
	
	public final void setPlayListPositionToPlay(int pos) {
		currentPlayList.setNextFile(pos);
	}
	
	public final void setShuffle(boolean enable) {
		currentPlayList.setShuffleMode(enable);
		logger.info("Shuffle enabled: " + enable);

	}
	public final void setRepeat(boolean enable) {
		currentPlayList.setRepeatMode(enable);
		logger.info("Repeat enabled: " + enable);
	}
	
	public final void setTime(long time) {
		HandlerProxy.getControllerHandler().getPlayerControlsController().setTime(time, currentDuration);
	}
	
	public final void setDuration(long time) {
		HandlerProxy.getControllerHandler().getPlayerControlsController().setMaxDuration(time);
	}
	
	public abstract void setVolume(int perCent);
	
	public abstract void setBalance(float value);

	public final void notifyPlayerError(Exception e) {
		logger.error("Player Error: " + e.getMessage());
		HandlerProxy.getVisualHandler().showErrorDialog(e.getMessage());
	}

	public abstract void setMute(boolean mute);

	public final void setCurrentPlayList(PlayList currentPlayList) {
		this.currentPlayList = currentPlayList;
	}

	public final int getVolume() {
		return volume;
	}

	public final void volumeDown() {
		setVolume(getVolume()-5);
		HandlerProxy.getVisualHandler().setVolume(getVolume());
	}
	
	public final void volumeUp() {
		setVolume(getVolume()+5);
		HandlerProxy.getVisualHandler().setVolume(getVolume());
	}
	

	public final boolean isMute() {
		return muted;
	}

	public long getCurrentDuration() {
		return currentDuration;
	}

	public void setCurrentDuration(long currentDuration) {
		this.currentDuration = currentDuration;
	}


}
