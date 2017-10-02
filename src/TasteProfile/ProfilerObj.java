package TasteProfile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.omg.CORBA.ORB;

public class ProfilerObj extends ProfilerPOA {

	private static Logger logger = Logger.getLogger("ProfilerPOA");
	private ORB orb;

	// private final String FILE_NAME = "train_triplets.txt";
	private final String FILE_NAME = "test.txt";

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
		try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
			Iterator<String> iterator = stream.iterator();
			while (iterator.hasNext()) {
				String[] entries = iterator.next().split("\t");
				String strUserId = entries[0].trim();
				String strSongId = entries[1].trim();
				Integer timesPlayed = new Integer(entries[2].trim());
				profileCache.addToProfile(strUserId, strSongId, timesPlayed);

			}
			System.out.println(
					String.format("Total number of users cached : %d", profileCache.getSize()));
			System.out.println(
					String.format("initializeUsersCache() ran for %d ms", (System.currentTimeMillis() - startTime)));
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
		if (profileCache.containsUserProfile(user_id)) {
			timesPlayed = profileCache.getUserProfile(user_id).getTimesPlayedOfSong(song_id);
		} else {
			timesPlayed = getTimes(user_id, song_id);
		}
		System.out.println(String.format("getTimesPlayedByUser(%s, %s) = %d timesPlayed ran for %d ms", user_id,
				song_id, timesPlayed, (System.currentTimeMillis() - startTime)));
		return timesPlayed;
	}

	public static void main(String args[]) {
		ProfilerObj obj = new ProfilerObj();
		obj.initializeUsersCache();
		obj.initializeSongsCache();

		System.out.println(obj.getTimesPlayedByUser("f0cd8df775b33e171e2f1f5454338e2f82feaa89", "SOYJYFW12A8C130E52"));
		System.out.println(obj.getTimesPlayedByUser("cbe161a3d8767529b2ddba2ba1a205d4a2591b30", "SOPXKYD12A6D4FA876"));
		System.out.println(obj.getTimesPlayed("SOYJYFW12A8C130E52"));
	}
}
