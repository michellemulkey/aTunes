package net.sourceforge.atunes.model.player;

import java.util.Comparator;

import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;

public class PlayListDurationComparator implements Comparator {

	public static PlayListDurationComparator comparator = new PlayListDurationComparator();
	
	@Override
	public int compare(Object o1, Object o2) {
		
		AudioFile f1 = (AudioFile) o1;
		AudioFile f2 = (AudioFile) o2;	
		
		long duration1 = f1.getDuration();
		long duration2 = f2.getDuration();
		
		if (duration1 == duration2)
			return 0;
		
		return duration1 > duration2 ? 1 : -1;
	}

}
