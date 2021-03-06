package TasteProfile;


/**
* TasteProfile/SongSequence.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Saturday, 7 October, 2017 5:53:34 PM CEST
*/

public abstract class SongSequence implements org.omg.CORBA.portable.StreamableValue
{
  public TasteProfile.Song songs[] = null;

  private static String[] _truncatable_ids = {
    TasteProfile.SongSequenceHelper.id ()
  };

  public String[] _truncatable_ids() {
    return _truncatable_ids;
  }

  public void _read (org.omg.CORBA.portable.InputStream istream)
  {
    int _len0 = istream.read_long ();
    this.songs = new TasteProfile.Song[_len0];
    for (int _o1 = 0;_o1 < this.songs.length; ++_o1)
      this.songs[_o1] = TasteProfile.SongHelper.read (istream);
  }

  public void _write (org.omg.CORBA.portable.OutputStream ostream)
  {
    ostream.write_long (this.songs.length);
    for (int _i0 = 0;_i0 < this.songs.length; ++_i0)
      TasteProfile.SongHelper.write (ostream, this.songs[_i0]);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TasteProfile.SongSequenceHelper.type ();
  }
} // class SongSequence
