package TasteProfile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

	// private final String FILE_NAME = "train_triplets.txt";
	private final String FILE_NAME = "test.txt";

	private Map<String, Integer> songsCache = new HashMap<String, Integer>();

	public void setORB(ORB orb_val) {
		orb = orb_val;
	}

	@Override
	public int getTimesPlayed(String song_id) {
		long startTime = System.currentTimeMillis();
		// user - song - timesPlayed
		Path path = Paths.get(FILE_NAME);
		int playCount = 0;
		try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
			Iterator<String> iterator = stream.iterator();
			while (iterator.hasNext()) {
				String[] entries = iterator.next().split("\t");
				if (song_id.equals(entries[1].trim())) {
					playCount += Integer.parseInt(entries[2].trim());
				}
			}
			System.out.println(
					String.format("initializeSongsCache() ran for %d ms", (System.currentTimeMillis() - startTime)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return playCount;
	}

	@Override
	public int getTimesPlayedByUser(String user_id, String song_id) {
		// user - song - timesPlayed
		Path path = Paths.get(FILE_NAME);
		int count = 0;
		try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
			Iterator<String> iterator = stream.iterator();
			while (iterator.hasNext()) {
				String[] entries = iterator.next().split("\t");
				String strUserId = entries[0].trim();
				String strSongId = entries[1].trim();
				if (strUserId.equals(user_id) && strSongId.equals(song_id)) {
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
}
