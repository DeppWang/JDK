package org.omg.CosNaming.NamingContextExtPackage;

/**
* org/omg/CosNaming/NamingContextExtPackage/InvalidAddressHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/workspace/9-2-build-windows-amd64-cygwin-phase2/jdk9/6725/corba/src/java.corba/share/classes/org/omg/CosNaming/nameservice.idl
* Wednesday, August 2, 2017 9:29:12 PM PDT
*/

public final class InvalidAddressHolder implements org.omg.CORBA.portable.Streamable
{
  public org.omg.CosNaming.NamingContextExtPackage.InvalidAddress value = null;

  public InvalidAddressHolder ()
  {
  }

  public InvalidAddressHolder (org.omg.CosNaming.NamingContextExtPackage.InvalidAddress initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper.type ();
  }

}