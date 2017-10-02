package TasteProfile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MusicUserProfileCache {

	final private Set<MusicUserProfile> profileCache;
	final Integer MAX_SIZE;

	public MusicUserProfileCache(int maxSize) {
		this.profileCache = new HashSet<MusicUserProfile>(maxSize);
		this.MAX_SIZE = maxSize;
	}

	public void add(final MusicUserProfile userProfile) {
		profileCache.add(userProfile);
		if (profileCache.size() > MAX_SIZE) {
			this.evict();
		}
	}

	public void addToProfile(String strUserId, String strSongId, Integer timesPlayed) {
		MusicUserProfile userProfile = this.getUserProfile(strUserId);
		if (null == userProfile) {
			userProfile = new MusicUserProfile(strUserId);
			userProfile.addSong(strSongId, timesPlayed);
			this.add(userProfile);
		} else {
			userProfile.addSong(strSongId, timesPlayed);
		}
	}

	public MusicUserProfile getUserProfile(final String userId) {
		Optional<MusicUserProfile> option = profileCache.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
		return option.isPresent() ? option.get() : null;
	}

	public boolean evict() {
		return profileCache.remove(this.getLeastPriorityUserProfile());
	}

	public MusicUserProfile getLeastPriorityUserProfile() {
		return profileCache.stream().min(MusicUserProfile::compareTo).get();
	}

	public boolean containsUserProfile(final String userId) {
		return (null == this.getUserProfile(userId)) ? false : true;
	}

	public int getSize() {
		return profileCache.size();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public static void main(String args[]) {
		MusicUserProfileCache cache = new MusicUserProfileCache(3);

		MusicUserProfile profile = new MusicUserProfile("123");
		profile.addSong("song1", new Integer(3));
		MusicUserProfile profile2 = new MusicUserProfile("456");
		profile2.addSong("song1", new Integer(2));
		profile2.addSong("song2", new Integer(6));
		MusicUserProfile profile3 = new MusicUserProfile("789");
		profile3.addSong("song2", new Integer(5));
		MusicUserProfile profile4 = new MusicUserProfile("1010");
		profile4.addSong("song1", new Integer(7));

		cache.add(profile);
		cache.add(profile3);
		cache.add(profile2);
		System.out.println(cache.getSize());
		System.out.println(cache.toString());
		cache.add(profile4);
		System.out.println(cache.getSize());
		System.out.println(cache.toString());
	}

}
