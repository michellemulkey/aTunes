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

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.atunes.gui.views.dialogs.IndeterminateProgressDialog;
import net.sourceforge.atunes.gui.views.dialogs.RipCdDialog;
import net.sourceforge.atunes.gui.views.dialogs.RipperProgressDialog;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.amazon.AmazonAlbum;
import net.sourceforge.atunes.kernel.modules.amazon.AmazonDisc;
import net.sourceforge.atunes.kernel.modules.amazon.AmazonService;
import net.sourceforge.atunes.kernel.modules.cdripper.CdRipper;
import net.sourceforge.atunes.kernel.modules.cdripper.ProgressListener;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.Cdda2wav;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.NoCdListener;
import net.sourceforge.atunes.kernel.modules.cdripper.cdda2wav.model.CDInfo;
import net.sourceforge.atunes.kernel.modules.cdripper.encoders.Encoder;
import net.sourceforge.atunes.kernel.modules.cdripper.encoders.LameEncoder;
import net.sourceforge.atunes.kernel.modules.cdripper.encoders.OggEncoder;
import net.sourceforge.atunes.kernel.utils.PictureExporter;
import net.sourceforge.atunes.kernel.utils.SystemProperties;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;


public class RipperHandler {

	private static final String FILE_NAME_PATTERN = CdRipper.ARTIST_PATTERN + " - " + CdRipper.ALBUM_PATTERN + 
													" - " + CdRipper.TRACK_TITLE_AND_NUMBER; 
	
	CdRipper ripper;
	boolean interrupted;
	boolean folderCreated;
	Logger logger = Logger.getLogger(RipperHandler.class);
	
	private String albumCoverURL;
	
	ArrayList<String> trackNames;
	
	public RipperHandler() {
	}
	
	public void startCdRipper() {
		if (!testTools())
			return;

		interrupted = false;
		final RipCdDialog dialog = HandlerProxy.getVisualHandler().getRipCdDialog();
		
		final IndeterminateProgressDialog waitDialog = HandlerProxy.getVisualHandler().getIndeterminateProgressDialog();
		
		Thread t = new Thread() {
			public void run() {
				ripper = new CdRipper();
				ripper.setNoCdListener(new NoCdListener() {
					public void noCd() {
						logger.error("No cd inserted");
						interrupted = true;
						waitDialog.setVisible(false);
						HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("NO_CD_INSERTED"));
					}
				});
				final CDInfo cdInfo = ripper.getCDInfo();
				waitDialog.setVisible(false);
				dialog.showCdInfo(cdInfo);
				if (!dialog.isCanceled()) {
					String artist = dialog.getArtist();
					String album = dialog.getAlbum();
					String folder = dialog.getFolder();
					ArrayList<Integer> tracks = dialog.getTracksSelected();
					importToRepository(folder, artist, album, tracks, dialog.getFormat(), dialog.getQuality());
				}
			}
		};
		t.start();
		
		waitDialog.setVisible(true);
	}
	
	private boolean testTools() {
		if (!Cdda2wav.testTool()) {
			logger.error("Error testing \"cdda2wav\". Check program is installed");
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("CDDA2WAV_NOT_FOUND"));
			return false;
		}
		
		if (!LameEncoder.testTool()) {
			logger.error("Error testing \"lame\". Check program is installed");
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("LAME_NOT_FOUND"));
			return false;
		}
		
		if (!OggEncoder.testTool()) {
			logger.error("Error testing \"oggenc\". Check program is installed");
			HandlerProxy.getVisualHandler().showErrorDialog(LanguageTool.getString("OGGENC_NOT_FOUND"));
			return false;
		}

		return true;
	}
	
	void importToRepository(String folder, final String artist, final String album, final ArrayList<Integer> tracks, final String format, final String quality) {
		// Disable import cd option in menu
		HandlerProxy.getControllerHandler().getMenuController().setRipCDEnabled(false);
		
		final File folderFile = new File(HandlerProxy.getRepositoryHandler().getRepositoryPath() + '/' + folder);
		if (!folderFile.exists()) {
			if (folderFile.mkdirs())
				folderCreated = true;
			else {
				logger.error("Folder could not be created");
				return;
			}
		}
		
		Encoder encoder;
		if (format.equals("OGG"))
			encoder = new OggEncoder();
		else
			encoder = new LameEncoder();
		encoder.setQuality(quality);
		ripper.setEncoder(encoder);
		ripper.setArtist(artist);
		ripper.setAlbum(album);
		ripper.setFileNamePattern(FILE_NAME_PATTERN);

		final RipperProgressDialog dialog = HandlerProxy.getVisualHandler().getRipperProgressDialog();

		// Get image from amazon if necessary
		if (albumCoverURL != null) {
			Image cover = AmazonService.getAmazonImage(albumCoverURL);
			dialog.setCover(cover);
			savePicture(cover, folderFile, artist, album);
		}
		
		dialog.setArtistAndAlbum(artist, album);
		
		dialog.setTotalProgressBarLimits(0, tracks.size());
		dialog.setDecodeProgressBarLimits(0, 100);
		dialog.setEncodeProgressBarLimits(0, 100);

		final ArrayList<File> filesImported = new ArrayList<File>();
		
		ripper.setDecoderListener(new ProgressListener() {
			public void notifyProgress(int percent) {
				dialog.setDecodeProgressValue(percent);
				dialog.setDecodeProgressValue(percent + "%");
			}
			public void notifyFileFinished(File f) {}
		});
		
		ripper.setEncoderListener(new ProgressListener() {
			public void notifyProgress(int percent) {
				dialog.setEncodeProgressValue(percent);
				dialog.setEncodeProgressValue(percent + "%");
			}
			public void notifyFileFinished(File f) {}
		});
		
		ripper.setTotalProgressListener(new ProgressListener() {
			public void notifyProgress(int value) {
				dialog.setTotalProgressValue(value);
				dialog.setTotalProgressValue(value + " / " + tracks.size());
				dialog.setDecodeProgressValue(0);
				dialog.setDecodeProgressValue(0 + "%");
				dialog.setEncodeProgressValue(0);
				dialog.setEncodeProgressValue(0 + "%");
			}
			public void notifyFileFinished(File f) {
				filesImported.add(f);
			}
		});

		Thread t = new Thread() {
			public void run() {
				ripper.ripTracks(tracks, trackNames, folderFile);
				notifyFinishImport(filesImported, folderFile);
				
				// Enable import cd option in menu
				HandlerProxy.getControllerHandler().getMenuController().setRipCDEnabled(true);
			}
		};
		
		t.start();
		dialog.setVisible(true);
	}
	
	public void notifyFinishImport(final ArrayList<File> filesImported, final File folder) {
		HandlerProxy.getVisualHandler().getRipperProgressDialog().setVisible(false);
		if (interrupted) { // If process is interrupted delete all imported files
			Thread deleter = new Thread() {
				public void run() {
					for (File f : filesImported)
						f.delete();
					
					// Wait two seconds to assure filesImported are deleted
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {}
					if (folderCreated)
						folder.delete();
				}
			};
			deleter.start();
		} else
			addFilesToRepositoryAndRefresh(filesImported, folder);
	}
	
	/**
	 * Add files to existing repository. This method is used after an import operation
	 */
	private void addFilesToRepositoryAndRefresh(ArrayList<File> files, File folder) {
		HandlerProxy.getVisualHandler().setCenterStatusBarText(LanguageTool.getString("REFRESHING") + "...");
		HandlerProxy.getVisualHandler().showProgressBar(true);
		HandlerProxy.getRepositoryHandler().addAndRefresh(files, folder);
	}
	
	public void cancelProcess() {
		interrupted = true;
		ripper.stop();
		logger.info("Process cancelled");
	}
	
	public void fillSongsFromAmazon(final String artist, final String album) {
		AmazonAlbum amazonAlbum = AmazonService.getAlbum(artist, album);
		if (amazonAlbum != null) {
			albumCoverURL = amazonAlbum.getImageURL();
			ArrayList<String> tracks = new ArrayList<String>();
			for (Iterator<AmazonDisc> it = amazonAlbum.getDiscs().iterator(); it.hasNext(); ) {
				AmazonDisc disc = it.next();
				tracks.addAll(disc.getTracks());
			}
			HandlerProxy.getVisualHandler().getRipCdDialog().updateTrackNames(tracks);
		}
	}
	
	private void savePicture(Image image, File path, String artist, String album) {
		String imageFileName = path.getAbsolutePath() + SystemProperties.fileSeparator + artist + '_' + album + "_Cover.jpg";
		try {
			PictureExporter.savePicture(image, imageFileName);
		} catch (IOException e) {
		}				
	}
}
