package TasteProfile;


/**
* TasteProfile/UserProfileSequence.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Saturday, 7 October, 2017 5:53:34 PM CEST
*/

public abstract class UserProfileSequence implements org.omg.CORBA.portable.StreamableValue
{
  public TasteProfile.UserProfile userProfiles[] = null;

  private static String[] _truncatable_ids = {
    TasteProfile.UserProfileSequenceHelper.id ()
  };

  public String[] _truncatable_ids() {
    return _truncatable_ids;
  }

  public void _read (org.omg.CORBA.portable.InputStream istream)
  {
    int _len0 = istream.read_long ();
    this.userProfiles = new TasteProfile.UserProfile[_len0];
    for (int _o1 = 0;_o1 < this.userProfiles.length; ++_o1)
      this.userProfiles[_o1] = TasteProfile.UserProfileHelper.read (istream);
  }

  public void _write (org.omg.CORBA.portable.OutputStream ostream)
  {
    ostream.write_long (this.userProfiles.length);
    for (int _i0 = 0;_i0 < this.userProfiles.length; ++_i0)
      TasteProfile.UserProfileHelper.write (ostream, this.userProfiles[_i0]);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TasteProfile.UserProfileSequenceHelper.type ();
  }
} // class UserProfileSequence
