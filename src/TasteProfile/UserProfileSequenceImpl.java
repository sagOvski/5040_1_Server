package TasteProfile;

public class UserProfileSequenceImpl extends UserProfileSequence {

	private static final long serialVersionUID = -3136996197415662198L;

	public UserProfileSequenceImpl() {

	}

	public UserProfileSequenceImpl(UserProfile[] userProfiles) {
		super.userProfiles = userProfiles;
	}

}
