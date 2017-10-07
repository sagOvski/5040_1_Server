package TasteProfile;

public class UserProfileImpl extends UserProfile {

	private static final long serialVersionUID = -6906567519926872093L;

	public UserProfileImpl() {
	}

	public UserProfileImpl(final String userId) {
		super.userId = userId;
	}

	public UserProfileImpl(final String userId, Song[] songs) {
		super.userId = userId;
		super.songs = new SongSequenceImpl(songs);
	}

}
