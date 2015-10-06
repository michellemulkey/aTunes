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

package net.sourceforge.atunes.kernel.modules.mplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable.PlayState;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.handlers.AbstractPlayer;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.utils.SystemProperties;
import net.sourceforge.atunes.utils.language.LanguageTool;

public class MPlayerHandler extends AbstractPlayer {

	public static final boolean GAP = false;
	
	private static final String LINUX_COMMAND = "mplayer";
	private static final String WIN_COMMAND = "win_tools/mplayer.exe";
	
	private static final String QUIET = "-quiet";
	private static final String SLAVE = "-slave";
	
	private Process process;

	public MPlayerHandler() {
		super();
		if (!testMPlayerAvailability()) {
			notifyPlayerError(new Exception(LanguageTool.getString("MPLAYER_NOT_FOUND")));
		}
		else {
			MPlayerPositionThread positionThread = new MPlayerPositionThread(this);
			positionThread.start();
		}
	}
	
	private static boolean testMPlayerAvailability() {
		if (!SystemProperties.hostIsWindows()) {
			try {
				Process p = Runtime.getRuntime().exec(new String[] {LINUX_COMMAND});
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while (stdInput.readLine() != null) {
				}			

				int code = p.waitFor();
				if (code != 0) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
	private void play(AudioFile f) throws IOException {
		process = Runtime.getRuntime().exec(new String[] {SystemProperties.hostIsWindows() ? WIN_COMMAND : LINUX_COMMAND, QUIET, SLAVE, f.getAbsolutePath()});
		MPlayerOutputReader outputReader = new MPlayerOutputReader(this, process);
		outputReader.start();
		sendGetDurationCommand();
		
		setVolume(volume);
		setBalance(balance);
		if (Kernel.getInstance().state.isShowOSD())
			HandlerProxy.getVisualHandler().showOSD(currentPlayList.getCurrentFile());
		// Update stats
		HandlerProxy.getRepositoryHandler().setSongStatistics(currentPlayList.getCurrentFile());
		if (HandlerProxy.getVisualHandler().getStatsDialog().isVisible())
			HandlerProxy.getControllerHandler().getStatsDialogController().updateStats();
	}
	
	@Override
	public void finish() {
		if (process != null)
			process.destroy();
		logger.info("Stopping player");
	}
	
	@Override
	public void next(boolean autoNext) {
		boolean wasStopped = false;
		AudioFile nextFile = currentPlayList.getNextFileToPlay();
		if (nextFile != null) {
			try {
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(nextFile);
				if (process != null || autoNext) {
					stop();
					if (!paused) {
						play(nextFile);
						if (muted)
							setMute(true);
						else
							setVolume(volume);
						setBalance(balance);
						logger.info("Started play of file " + nextFile);
					}
					else {
						setTime(0);
						wasStopped = true;
					}
				}
				else {
					wasStopped = true;
					setTime(0);
				}
				if (wasStopped)
					playListController.setSelectedSong(currentPlayList.getNextFile());
				else {
					HandlerProxy.getVisualHandler().updateStatusBar(nextFile);
					HandlerProxy.getVisualHandler().updateTitleBar(nextFile);
					playListController.setSelectedSong(currentPlayList.getNextFile());
				}
			} catch (Exception e) {
				notifyPlayerError(e);
				stop();
			}
		}
		else { // End of play list. Go to first song and stop
			stop();
			currentPlayList.setNextFile(0);
			playListController.setSelectedSong(currentPlayList.getNextFile());
			HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(currentPlayList.getCurrentFile());
		}
	}

	public void nextWithNoGap() {
		AudioFile nextFile = currentPlayList.getNextFileToPlay();
		if (nextFile != null) {
			try {
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(nextFile);
				play(nextFile);
				if (muted)
					setMute(true);
				else
					setVolume(volume);
				setBalance(balance);
				logger.info("Started play of file " + nextFile);
				HandlerProxy.getVisualHandler().updateStatusBar(nextFile);
				HandlerProxy.getVisualHandler().updateTitleBar(nextFile);
				playListController.setSelectedSong(currentPlayList.getNextFile());
			} catch (Exception e) {
				notifyPlayerError(e);
				stop();
			}
		}
		else { // End of play list. Go to first song and stop
			stop();
			currentPlayList.setNextFile(0);
			playListController.setSelectedSong(currentPlayList.getNextFile());
			HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(currentPlayList.getCurrentFile());
		}
	}
	
	@Override
	public void play(boolean buttonPressed) {
		if (isPlaying() && buttonPressed) { // Pause
			HandlerProxy.getVisualHandler().setPlaying(false);
			try {
				sendPauseCommand();
				HandlerProxy.getVisualHandler().updateStatusBar(LanguageTool.getString("PAUSED"));
				HandlerProxy.getVisualHandler().updateTitleBar("");
				HandlerProxy.getVisualHandler().updatePlayState(PlayState.PAUSED);
				logger.info("Pause");
			} catch (Exception e) {
				notifyPlayerError(e);
				stop();
			}
			paused = true;
		}
		else {
			HandlerProxy.getVisualHandler().setPlaying(currentPlayList.size() > 0); // If list is empty, is not playing

			AudioFile nextFile = null;
			try {
				if (paused && buttonPressed) {
					paused = false;
					sendResumeCommand();
					if (!currentPlayList.isEmpty()) {
						HandlerProxy.getVisualHandler().updateStatusBar(currentPlayList.get(currentPlayList.getNextFile()));
						HandlerProxy.getVisualHandler().updateTitleBar(currentPlayList.get(currentPlayList.getNextFile()));
						HandlerProxy.getVisualHandler().updatePlayState(PlayState.PAUSED);
						logger.info("Resumed paused song");
					}
				}
				else {
					nextFile = currentPlayList.getCurrentFile();
					if (nextFile != null) {
						if (isPlaying())
							stop();
						HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(nextFile);
						play(nextFile);

						// Setting volume and balance
						if (muted)
							setMute(true);
						else
							setVolume(volume);
						setBalance(balance);

						HandlerProxy.getVisualHandler().updateStatusBar(nextFile);
						HandlerProxy.getVisualHandler().updateTitleBar(nextFile);
						playListController.setSelectedSong(currentPlayList.getNextFile());
						logger.info("Started play of file " + nextFile);
					}
				}
				HandlerProxy.getVisualHandler().updatePlayState(PlayState.PLAYING);
			} catch (Exception e) {
				notifyPlayerError(e);
				stop();
			}
		}
	}
	
	@Override
	public void previous() {
		boolean wasStopped = false;
		AudioFile previousFile = currentPlayList.getPreviousFileToPlay();
		if (previousFile != null) {
			try {
				HandlerProxy.getPlayListHandler().getPlayListListener().selectedSongChanged(previousFile);
				if (process != null) {
					stop();
					if (!paused) {
						play(previousFile);
						if (muted)
							setMute(true);
						else
							setVolume(volume);
						setBalance(balance);
						logger.info("Started play of file " + previousFile);
					}
					else {
						setTime(0);
						wasStopped = true;
					}
				}
				else {
					wasStopped = true;
					setTime(0);
				}
				playListController.setSelectedSong(currentPlayList.getNextFile());
				if (wasStopped)
					playListController.setSelectedSong(currentPlayList.getNextFile());
				else {
					HandlerProxy.getVisualHandler().updateStatusBar(previousFile);
					HandlerProxy.getVisualHandler().updateTitleBar(previousFile);
					playListController.setSelectedSong(currentPlayList.getNextFile());
				}
			} catch (Exception e) {
				notifyPlayerError(e);
				stop();
			}
		}
	}
	
	@Override
	public void seek(double position) {
		sendSeekCommand(position);
		//System.out.println("POS = " + position);
	}
	@Override
	public void setBalance(float value) {
	}
	
	@Override
	public void setMute(boolean mute) {
		muted = mute;
		sendMuteCommand();
	}
	
	@Override
	public void setVolume(int perCent) {
		if (perCent < 0)
			volume = 0;
		else if (perCent > 100)
			volume = 100;
		else
			volume = perCent;
		sendVolumeCommand(volume);
	}
	
	@Override
	public void stop() {
		HandlerProxy.getVisualHandler().setPlaying(false);
		if (paused)
			paused = false;
		try {
			sendStopCommand();

			process = null;
			
			setTime(0);
			HandlerProxy.getVisualHandler().updateStatusBar(LanguageTool.getString("STOPPED"));
			HandlerProxy.getVisualHandler().updateTitleBar("");
			HandlerProxy.getVisualHandler().updatePlayState(PlayState.STOPPED);
			logger.info("Stop");
		} catch (Exception e) {
			notifyPlayerError(e);
		}
	}
	
	private void sendCommand(String command) {
		if (process != null) {
			PrintStream out = new PrintStream(process.getOutputStream());
			out.print(command + '\n'); 
			out.flush();
		}
	}
	
	void sendGetPositionCommand() {
		sendCommand("get_time_pos");
	}
	
	private void sendPauseCommand() {
		sendCommand("pause");
	}

	private void sendResumeCommand() {
		sendCommand("pause");
	}
	
	private void sendStopCommand() {
		sendCommand("quit");
	}
	
	private void sendMuteCommand() {
		sendCommand("mute");
	}
	
	private void sendVolumeCommand(int perCent) {
		sendCommand("volume " + perCent + " 1");
	}
	
	private void sendSeekCommand(double perCent) {
		sendCommand("seek " + perCent * 100 + " 1");
	}
	
	private boolean isPlaying() {
		return process != null && !paused;
	}
	
	private void sendGetDurationCommand() {
		sendCommand("get_time_length");
	}

}
