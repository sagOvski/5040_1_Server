package TasteProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.omg.CORBA.ORB;

public class ProfilerObj extends ProfilerPOA {

	private static Logger logger = Logger.getLogger("ProfilerPOA");
	private ORB orb;

	private final String FILE_NAME = "train_triplets.txt";
	// private final String FILE_NAME = "test.txt";

	private Map<String, Integer> songsCache = new HashMap<String, Integer>();
	private MusicUserProfileCache profileCache = new MusicUserProfileCache(1024);

	public void setORB(ORB orb_val) {
		orb = orb_val;
	}

	// implement shutdown() method
	public void shutdown() {
		orb.shutdown(false);
	}

	public void initializeSongsCache() {
		long startTime = System.currentTimeMillis();
		// user - song - timesPlayed
		Path path = Paths.get(FILE_NAME);
		try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
			Iterator<String> iterator = stream.iterator();
			while (iterator.hasNext()) {
				String[] entries = iterator.next().split("\t");
				String song = entries[1].trim();
				songsCache.putIfAbsent(song, new Integer("0"));
				songsCache.put(song, songsCache.get(song) + 1);
			}
			System.out.println(
					String.format("initializeSongsCache() ran for %d ms", (System.currentTimeMillis() - startTime)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initializeUsersCache() {
		long startTime = System.currentTimeMillis();
		// user - song - timesPlayed
		Path path = Paths.get(FILE_NAME);
		String line = null;
		try (BufferedReader bf = new BufferedReader(new FileReader(new File(FILE_NAME)))) {
			line = bf.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] entries = line.split("\t");
		String strUserId = entries[0].trim();
		String strSongId = entries[1].trim();
		Integer timesPlayed = new Integer(entries[2].trim());
		MusicUserProfile profile = new MusicUserProfile(strUserId);
		profile.addSong(strSongId, timesPlayed);

		try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
			Iterator<String> iterator = stream.iterator();
			while (iterator.hasNext()) {
				entries = iterator.next().split("\t");
				String innerUserId = entries[0].trim();
				strSongId = entries[1].trim();
				timesPlayed = new Integer(entries[2].trim());

				if (innerUserId.equals(strUserId)) {
					profile.addSong(strSongId, timesPlayed);
				} else {
					profileCache.add(profile);
					strUserId = innerUserId;
					profile = new MusicUserProfile(innerUserId);
					profile.addSong(strSongId, timesPlayed);
				}

			}
			System.out.println(String.format("Total number of users cached : %d", profileCache.getSize()));
			System.out.println(
					String.format("initializeUsersCache() ran for %d ms", (System.currentTimeMillis() - startTime)));
			System.out.println(String.format("UserProfileCache : %s", profileCache.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getTimesPlayed(String song_id) {
		long startTime = System.currentTimeMillis();
		if (songsCache.size() == 0) {
			initializeSongsCache();
		}
		int timesPlayed = songsCache.get(song_id);
		try {
			Thread.sleep(60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String msg = String.format("Returning %d for song_id %s", timesPlayed, song_id);
		logger.log(Level.ALL, msg);
		System.out.println(String.format("getTimesPlayed(%s) = %d timesPlayed ran for %d ms", song_id, timesPlayed,
				(System.currentTimeMillis() - startTime)));
		return timesPlayed;
	}

	private int getTimes(String userId, String songId) {
		// user - song - timesPlayed
		Path path = Paths.get(FILE_NAME);
		int count = 0;
		try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
			Iterator<String> iterator = stream.iterator();
			while (iterator.hasNext()) {
				String[] entries = iterator.next().split("\t");
				String strUserId = entries[0].trim();
				String strSongId = entries[1].trim();
				if (strUserId.equals(userId) && strSongId.equals(songId)) {
					String strCount = entries[2];
					count = count + new Integer(strCount);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int getTimesPlayedByUser(String user_id, String song_id) {
		long startTime = System.currentTimeMillis();
		int timesPlayed = 0;
//		if (profileCache.containsUserProfile(user_id)) {
//			timesPlayed = profileCache.getUserProfile(user_id).getTimesPlayedOfSong(song_id);
//			try {
//				Thread.sleep(60);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		} else {
			timesPlayed = getTimes(user_id, song_id);
//		}
		System.out.println(String.format("getTimesPlayedByUser(%s, %s) = %d timesPlayed ran for %d ms", user_id,
				song_id, timesPlayed, (System.currentTimeMillis() - startTime)));
		return timesPlayed;
	}

	public static void main(String args[]) {
		ProfilerObj obj = new ProfilerObj();
		obj.initializeUsersCache();
		obj.initializeSongsCache();

		System.out.println(obj.profileCache.getTopTenPriorityProfiles());
	}

	@Override
	public UserProfile getUserProfile(String userId) {
		MusicUserProfile profile = null;
		if (profileCache.containsUserProfile(userId)) {
			profile = profileCache.getUserProfile(userId);
		} else {
			profile = new MusicUserProfile(userId);
			// user - song - timesPlayed
			Path path = Paths.get(FILE_NAME);
			try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
				Iterator<String> iterator = stream.iterator();
				while (iterator.hasNext()) {
					String[] entries = iterator.next().split("\t");
					String strUserId = entries[0].trim();
					String strSongId = entries[1].trim();
					int timesPlayed = Integer.parseInt(entries[2]);
					if (strUserId.equals(userId)) {
						profile.addSong(strSongId, timesPlayed);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		UserProfileImpl corbaProfile = new UserProfileImpl(profile.getUserId(),
				UserProfileHelper.getCorbaSongObjects(profile.getSongMap()));

		return corbaProfile;
	}

	@Override
	public UserProfileSequence getTopTenUsers() {
		UserProfile[] corbaProfiles = new UserProfile[10];
		List<MusicUserProfile> topProfiles = profileCache.getTopTenPriorityProfiles();
		int index = 0;
		for (final MusicUserProfile aProfile : topProfiles) {
			corbaProfiles[index++] = new UserProfileImpl(aProfile.getUserId(),
					UserProfileHelper.getCorbaSongObjects(aProfile.getSongMap()));
		}
		return new UserProfileSequenceImpl(corbaProfiles);
	}
}
