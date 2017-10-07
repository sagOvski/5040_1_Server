package TasteProfile;

public class SongImpl extends Song {

	private static final long serialVersionUID = -3962415725647572176L;

	public SongImpl() {
	}

	public SongImpl(final String songId, final Integer playCount) {
		super.songId = songId;
		super.playCount = playCount.intValue();
	}
	
}
