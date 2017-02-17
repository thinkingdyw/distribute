package com.thinking.rpc.core.transport;

import java.io.Closeable;
import java.io.IOException;

/**
 * 传输层抽象
 * @author didi
 *
 */
public abstract class Transport implements Closeable{

	public abstract boolean isOpen();

	public abstract int read(byte[] buffer,int off,int length);
	
	public void read(byte[] buffer) throws IOException{
		throw new IOException("read operation is not implement");
	}

	public void write(byte[] buffer) throws IOException {
		throw new IOException("write operation is not implement");
	}
}
