package TasteProfile;

/**
* TasteProfile/SongSequenceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Saturday, 7 October, 2017 5:53:34 PM CEST
*/

public final class SongSequenceHolder implements org.omg.CORBA.portable.Streamable
{
  public TasteProfile.SongSequence value = null;

  public SongSequenceHolder ()
  {
  }

  public SongSequenceHolder (TasteProfile.SongSequence initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = TasteProfile.SongSequenceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    TasteProfile.SongSequenceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TasteProfile.SongSequenceHelper.type ();
  }

}
