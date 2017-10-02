package TasteProfile;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MusicUserProfile implements Comparable<MusicUserProfile> {

	private String userId;
	private Integer priority = new Integer(0);
	private Map<String, Integer> songMap = new HashMap<String, Integer>();

	@SuppressWarnings("unused")
	private MusicUserProfile() {
	}

	public MusicUserProfile(final String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, Integer> getSongMap() {
		return songMap;
	}

	public void setSongMap(Map<String, Integer> songMap) {
		this.songMap = songMap;
	}

	public void addSong(final String songId, final Integer timesPlayed) {
		if (null != songMap.putIfAbsent(songId, timesPlayed)) {
			songMap.put(songId, Integer.sum(songMap.get(songId), timesPlayed));
		}
		this.priority = this.priority + timesPlayed;
	}

	public void addSong(final String songId) {
		songMap.putIfAbsent(songId, new Integer(0));
		songMap.put(songId, Integer.sum(songMap.get(songId), new Integer(1)));
		this.priority = this.priority + new Integer(1);
	}

	public Integer getTimesPlayedOfSong(final String songId) {
		return songMap.getOrDefault(songId, new Integer(0));
	}

	public Integer getPriority() {
		return this.priority;
	}

	@Override
	public int compareTo(MusicUserProfile compareProfile) {
		return this.getPriority().compareTo(compareProfile.getPriority());
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
