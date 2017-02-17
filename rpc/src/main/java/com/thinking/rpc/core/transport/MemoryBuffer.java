package com.thinking.rpc.core.transport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MemoryBuffer extends Transport {

	private ByteArrayOutputStream buf;
	private int position;

	public MemoryBuffer(int capacity) {
		buf = new ByteArrayOutputStream(capacity);
	}

	public int length() {
		return buf.size();
	}

	public byte[] getContent() {
		return buf.toByteArray();
	}

	public void close() throws IOException {
	}

	public boolean isOpen() {
		return true;
	}

	@Override
	public void write(byte[] buffer) throws IOException {
		buf.write(buffer);
	}

	@Override
	public int read(byte[] buffer, int off, int length) {
		byte[] src = getContent();
		int factCanRead = (length > length() - position ? length() - position : length);
		if (factCanRead > 0) {
			System.arraycopy(src, position, buf, off, factCanRead);
			position += factCanRead;
		}
		return factCanRead;
	}
}
