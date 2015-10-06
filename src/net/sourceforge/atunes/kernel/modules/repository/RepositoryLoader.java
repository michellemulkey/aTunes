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

package net.sourceforge.atunes.kernel.modules.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.modules.repository.tags.tag.Tag;
import net.sourceforge.atunes.kernel.utils.AudioFilePictureUtils;
import net.sourceforge.atunes.model.info.Album;
import net.sourceforge.atunes.model.info.Artist;
import net.sourceforge.atunes.model.info.Folder;
import net.sourceforge.atunes.utils.StringUtils;
import net.sourceforge.atunes.utils.Timer;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;



public class RepositoryLoader extends Thread {
	
	private static Logger logger = Logger.getLogger(RepositoryLoader.class);
	
	private RepositoryLoaderListener listener;
	
	private File pathFile;
	private boolean refresh;
	private boolean device;
	private boolean interrupt;
	
	private Repository repository;
	
	private int totalFilesToLoad;
	private int filesLoaded;
	private long startReadTime;
	
	// Some attributes to speed up populate info process
	private static final String unknownArtist = LanguageTool.getString("UNKNOWN_ARTIST");
	private static final String unknownAlbum = LanguageTool.getString("UNKNOWN_ALBUM");
	private String fastRepositoryPath;
	private int fastFirstChar;
	
	public RepositoryLoader(File path, boolean refresh, boolean device) {
		this.refresh = refresh;
		this.device = device;
		pathFile = path;
		fastRepositoryPath = pathFile.getAbsolutePath().replace('\\','/');
		if (fastRepositoryPath.endsWith("/"))
			fastRepositoryPath = fastRepositoryPath.substring(0, fastRepositoryPath.length()-2);
		fastFirstChar = fastRepositoryPath.length() + 1;
		repository = new Repository(path);
		setPriority(Thread.MAX_PRIORITY);
	}

	public void addRepositoryLoaderListener(RepositoryLoaderListener listener) {
		this.listener = listener;
	}
	
	public void run() {
		super.run();
		logger.info("Starting repository read");
		long t0 = System.currentTimeMillis();
		if (pathFile.isDirectory()) {
			loadRepository();
		} else {
			logger.error("Repository '" + pathFile + "' is not a directory");
		}
		if (!interrupt) {
			long t1 = System.currentTimeMillis();
			double timeInSeconds = (t1 - t0) / 1000.0;
			long files = repository.countFiles();
			double averageFileTime = timeInSeconds / files;
			logger.info("Read repository process DONE (" + files + " files, " + timeInSeconds + " seconds, " + StringUtils.toString(averageFileTime, 4) + " seconds / file)");
			notifyFinish();
		}
	}

	private void notifyFinish() {
		if (listener == null)
			return;
		if (device) {
			if (refresh)
				listener.notifyFinishDeviceRefresh(this);
			else
				listener.notifyFinishDeviceRead(this);
		}
		else if (refresh)
			listener.notifyFinishRepositoryRefresh(this);
		else
			listener.notifyFinishRepositoryRead(this);
	}
	
	private void loadRepository() {
		totalFilesToLoad = countFilesInDir(pathFile);
		if (listener != null)
			listener.notifyFilesInRepository(totalFilesToLoad);
		startReadTime = System.currentTimeMillis();
		try {
			navigateDir(pathFile);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
	}
	
	private int countFilesInDir(File pathFile) {
		int files = 0;
		if (!interrupt) {
			File[] list = pathFile.listFiles();
			for (int i = 0; i < list.length; i++) {
				if (AudioFile.isValidAudioFile(list[i])) {
					files++;
				}
				else if (list[i].isDirectory())
					files = files + countFilesInDir(list[i]);
			}
		}
		return files;
	}
	
	public static int countFilesInRepositoryPath(File path) {
		int files = 0;
		File[] list = path.listFiles();
		for (int i = 0; i < list.length; i++) {
			if (AudioFile.isValidAudioFile(list[i])) {
				files++;
			}
			else if (list[i].isDirectory())
				files = files + countFilesInRepositoryPath(list[i]);
		}
		return files;
	}
	
	private void navigateDir(File dir) throws FileNotFoundException {
		if (!interrupt) {
			String pathToFile = dir.getAbsolutePath().replace('\\','/');
			
			int lastChar = pathToFile.lastIndexOf('/') + 1;
			String relativePath;
			if (fastFirstChar <= lastChar)
				relativePath = pathToFile.substring(fastFirstChar);
			else
				relativePath = ".";
						
			if (listener != null)
				listener.notifyCurrentPath(relativePath);
			
			File[] list = dir.listFiles();
			ArrayList<File> pictures = new ArrayList<File>();
			ArrayList<File> audioFiles = new ArrayList<File>();
			ArrayList<File> dirs = new ArrayList<File>();
			if (list != null) {
				//First find pictures, audio and files
				for (int i = 0; i < list.length; i++) {
					if (AudioFile.isValidAudioFile(list[i])) {
						audioFiles.add(list[i]);
					}
					else if (list[i].isDirectory()) {
						dirs.add(list[i]);
					} 
					else if (list[i].getName().toUpperCase().endsWith("JPG")) {
						pictures.add(list[i]);
					}
				}
				
				HashMap<String, AudioFile> repositoryFiles = repository.getFiles();
				for (int i = 0; i < audioFiles.size() && !interrupt; i++) {
					AudioFile audioFile = null;
					audioFile = new AudioFile(audioFiles.get(i).getAbsolutePath());
					audioFile.setExternalPictures(pictures);
					if (listener != null)
						listener.notifyFileLoaded();
					filesLoaded++;
					repositoryFiles.put(audioFile.getAbsolutePath(), audioFile);
					populateInfo(repository, audioFile);
					populateFolderTree(repository, relativePath, audioFile);
					repository.setTotalSizeInBytes(repository.getTotalSizeInBytes() + audioFile.length());
					repository.addDurationInSeconds(audioFile.getDuration());

					if (filesLoaded % 50 == 0) {
						long t1 = System.currentTimeMillis();
						long remainingTime = filesLoaded != 0 ? (totalFilesToLoad - filesLoaded) * (t1 - startReadTime) / filesLoaded : 0;

						if (listener != null)
							listener.notifyRemainingTime(remainingTime);
					}
				}
				
				for (int i = 0; i < dirs.size(); i++) {
					navigateDir(dirs.get(i));
				}
			}
		}
	}

	private static void populateFolderTree(Repository repository, String relativePath, AudioFile file) {
		StringTokenizer st = new StringTokenizer(relativePath, "/");
		Folder parentFolder = null;
		while (st.hasMoreTokens()) {
			String folderName = st.nextToken();
			Folder f;
			if (parentFolder != null) {
				if (parentFolder.containsFolder(folderName))
					f = parentFolder.getFolder(folderName);
				else {
					f = new Folder(folderName);
					parentFolder.addFolder(f);
				}
			}
			else {
				if (repository.getStructure().getFolderStructure().containsKey(folderName))
					f =  repository.getStructure().getFolderStructure().get(folderName);
				else {
					f = new Folder(folderName);
					repository.getStructure().getFolderStructure().put(f.getName(), f);
				}
			}
			parentFolder = f;
		}
		if (parentFolder == null)
			parentFolder = new Folder(".");
		parentFolder.addFile(file);
	}
	

	private static void populateInfo(Repository repository, AudioFile audioFile) {
		Tag tag = audioFile.getTag();
		String artist = null;
		String album = null;
		if (tag != null) {
			artist = StringUtils.format(tag.getArtist());
			album = StringUtils.format(tag.getAlbum());
		}
		if (artist == null || artist.equals(""))
			artist = unknownArtist;
		if (album == null || album.equals(""))
			album = unknownAlbum;

		if (repository.getStructure().getTreeStructure().containsKey(artist)) {
			Artist a = repository.getStructure().getTreeStructure().get(artist);
			Album alb = a.getAlbum(album);
			if (alb != null) {
				alb.addSong(audioFile);
			}
			else {
				alb = new Album(album);
				alb.addSong(audioFile);
				alb.setArtist(a.getName());
				addAlbumPicture(alb, audioFile);
				a.addAlbum(alb);
			}
		} else {
			Artist a = new Artist(artist);
			Album alb = new Album(album);
			alb.addSong(audioFile);
			alb.setArtist(artist);
			addAlbumPicture(alb, audioFile);
			a.addAlbum(alb);
			repository.getStructure().getTreeStructure().put(artist, a);
		}
	}
	
	private static void addAlbumPicture(Album a, AudioFile audioFile) {
		ImageIcon img = AudioFilePictureUtils.getInsidePicture(audioFile, Constants.TOOLTIP_IMAGE_WIDTH, Constants.TOOLTIP_IMAGE_HEIGHT); 
		if (img == null)
			img = AudioFilePictureUtils.getExternalPicture(audioFile, Constants.TOOLTIP_IMAGE_WIDTH, Constants.TOOLTIP_IMAGE_HEIGHT);
		if (img != null)
			a.setPicture(img);
	}

	public static void refreshFile(Repository repository, AudioFile file) {
		Tag tag = file.getTag();
		String artist = null;
		String album = null;
		if (tag != null) {
			artist = StringUtils.format(tag.getArtist());
			album = StringUtils.format(tag.getAlbum());
		}
		if (artist == null || artist.equals(""))
			artist = unknownArtist;
		if (album == null || album.equals(""))
			album = unknownAlbum;

		Artist a = repository.getStructure().getTreeStructure().get(artist);
		if (a != null) {
			Album alb = a.getAlbum(album);
			if (alb != null) {
				if (alb.getSongs().size() == 1)
					a.removeAlbum(alb);
				else
					alb.removeSong(file);
				
				if (a.getSongs().size() <= 1)
					repository.getStructure().getTreeStructure().remove(a.getName());
			}
		}
		file.refreshTag();
		populateInfo(repository, file);
	}

	public void interruptLoad() {
		logger.info("Load interrupted");
		interrupt = true;
	}

	public static ArrayList<AudioFile> getSongsForDir(File dir) {
		ArrayList<AudioFile> result = new ArrayList<AudioFile>();
		
		File[] list = dir.listFiles();
		ArrayList<File> pictures = new ArrayList<File>();
		ArrayList<File> files = new ArrayList<File>();
		ArrayList<File> dirs = new ArrayList<File>();
		if (list != null) {
			//First find pictures, audio and files
			for (int i = 0; i < list.length; i++) {
				if (AudioFile.isValidAudioFile(list[i])) {
					files.add(list[i]);
				}
				else if (list[i].isDirectory()) {
					dirs.add(list[i]);
				} 
				else if (list[i].getName().toUpperCase().endsWith("JPG")) {
					pictures.add(list[i]);
				}
			}
			
			for (int i = 0; i < files.size(); i++) {
				AudioFile mp3 = null;
				mp3 = new AudioFile(files.get(i).getAbsolutePath());
				mp3.setExternalPictures(pictures);
				result.add(mp3);
			}
			
			for (int i = 0; i < dirs.size(); i++) {
				getSongsForDir(dirs.get(i));
			}
		}
		return result;
	}

	public static void fillStats(Repository repository, AudioFile song) {
		String songPath = song.getAbsolutePath();
		if (repository.getFiles().containsKey(songPath)) {
			repository.getStats().setTotalPlays(repository.getStats().getTotalPlays()+1);
			
			SongStats stats = repository.getStats().getSongsStats().get(songPath);
			if (stats != null) {
				stats.setLastPlayed(new Date());
				stats.increaseTimesPlayed();
			}
			else {
				stats = new SongStats();
				repository.getStats().getSongsStats().put(songPath, stats);
				repository.getStats().setDifferentSongsPlayed(repository.getStats().getDifferentSongsPlayed()+1);
			}
			repository.getStats().getSongsRanking().addItem(song);
			
			String artist = song.getArtist();
			repository.getStats().getArtistsRanking().addItem(artist);
			
			String album = song.getAlbum();
			repository.getStats().getAlbumsRanking().addItem(album);
		}
	}

	public Repository getRepository() {
		return repository;
	}
	
	public static void addToRepository(Repository rep, ArrayList<File> files, File folder) {
		String repositoryPath = rep.getPathFile().getAbsolutePath().replace('\\','/');
		if (repositoryPath.endsWith("/"))
			repositoryPath = repositoryPath.substring(0, repositoryPath.length()-2);
		int firstChar = repositoryPath.length() + 1;

		File[] list = folder.listFiles();
		ArrayList<File> pictures = new ArrayList<File>();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].getName().toUpperCase().endsWith("JPG")) {
					pictures.add(list[i]);
				}
			}
		}
		
		HashMap<String, AudioFile> repositoryFiles = rep.getFiles();
		for (File f: files) {
			AudioFile audioFile = null;
			audioFile = new AudioFile(f.getAbsolutePath());
			audioFile.setExternalPictures(pictures);
			repositoryFiles.put(audioFile.getAbsolutePath(), audioFile);
			populateInfo(rep, audioFile);

			String pathToFile = audioFile.getAbsolutePath().replace('\\','/');
			int lastChar = pathToFile.lastIndexOf('/') + 1;
			String relativePath;
			if (firstChar < lastChar)
				relativePath = pathToFile.substring(firstChar, lastChar);
			else
				relativePath = ".";
			populateFolderTree(rep, relativePath, audioFile);
			rep.setTotalSizeInBytes(rep.getTotalSizeInBytes() + audioFile.length());
			rep.addDurationInSeconds(audioFile.getDuration());
		}
	}
	
	public static void main(String[] args) {
		Timer.start();
		RepositoryLoader loader = new RepositoryLoader(new File("d:/musica"), false, false);
		loader.start();
		try {
			loader.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(StringUtils.toString(Timer.stop(), 3));
	}
}
