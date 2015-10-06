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

import java.io.Serializable;
import java.util.HashMap;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.utils.RankList;


public class RepositoryStats implements Serializable {

	private static final long serialVersionUID = -3603928907730394504L;

	private int totalPlays;
	private int differentSongsPlayed;
	
	private RankList songsRanking;
	private RankList albumsRanking;
	private RankList artistsRanking;
	
	private HashMap<String, SongStats> songsStats;
	
	protected RepositoryStats() {
		songsRanking = new RankList();
		albumsRanking = new RankList();
		artistsRanking = new RankList();
		songsStats = new HashMap<String, SongStats>();
	}

	public int getDifferentSongsPlayed() {
		return differentSongsPlayed;
	}

	public void setDifferentSongsPlayed(int differentSongsPlayed) {
		this.differentSongsPlayed = differentSongsPlayed;
	}

	public int getTotalPlays() {
		return totalPlays;
	}

	public void setTotalPlays(int totalPlays) {
		this.totalPlays = totalPlays;
	}

	public RankList getAlbumsRanking() {
		return albumsRanking;
	}

	public RankList getArtistsRanking() {
		return artistsRanking;
	}

	public RankList getSongsRanking() {
		return songsRanking;
	}

	public HashMap<String, SongStats> getSongsStats() {
		return songsStats;
	}
	
	public SongStats getStatsForFile(AudioFile song) {
		if (song != null)
			return songsStats.get(song.getAbsolutePath());
		return null;
	}

	public void setSongsStats(HashMap<String, SongStats> songsStats) {
		this.songsStats = songsStats;
	}

	public void setAlbumsRanking(RankList albumsRanking) {
		this.albumsRanking = albumsRanking;
	}

	public void setArtistsRanking(RankList artistsRanking) {
		this.artistsRanking = artistsRanking;
	}

	public void setSongsRanking(RankList songsRanking) {
		this.songsRanking = songsRanking;
	}
}
