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

	public abstract void write(byte[] buffer,int off,int length) throws IOException;
}
