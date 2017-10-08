package TasteProfile;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

	public Set<MusicUserProfile> getSet() {
		return this.profileCache;
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

	public List<MusicUserProfile> getTopTenPriorityProfiles() {
		List<MusicUserProfile> sortedProfiles = profileCache.stream().sorted().collect(Collectors.toList());
		Collections.reverse(sortedProfiles);
		return (sortedProfiles.subList(0, 10));
	}

	public MusicUserProfile getUserProfile(final String userId) {
		Optional<MusicUserProfile> option = profileCache.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
		return option.isPresent() ? option.get() : null;
	}

	public boolean evict() {
		return profileCache.remove(this.getLeastPriorityUserProfile());
	}

	public MusicUserProfile getLeastPriorityUserProfile() {
		MusicUserProfile prof = profileCache.stream().min(MusicUserProfile::compareTo).get();
		if (prof.getPriority() > 2000) {
			System.out.println("removing profile : " + prof.toString());
		}

		return prof;
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

}
