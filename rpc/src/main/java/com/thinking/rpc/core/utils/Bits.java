package com.thinking.rpc.core.utils;

public class Bits {
	public static final void encode(final int frameSize, final byte[] buf) {
	    buf[0] = (byte)(0xff & (frameSize >> 24));
	    buf[1] = (byte)(0xff & (frameSize >> 16));
	    buf[2] = (byte)(0xff & (frameSize >> 8));
	    buf[3] = (byte)(0xff & (frameSize));
	  }

	  public static final int decode(final byte[] buf) {
	    return 
	      ((buf[0] & 0xff) << 24) |
	      ((buf[1] & 0xff) << 16) |
	      ((buf[2] & 0xff) <<  8) |
	      ((buf[3] & 0xff));
	  }
}
