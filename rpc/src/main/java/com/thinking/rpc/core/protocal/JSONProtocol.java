package com.thinking.rpc.core.protocal;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.thinking.rpc.core.transport.Transport;

public class JSONProtocol extends Protocol{

	public JSONProtocol(Transport transport) {
		super(transport);
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(buffer.length);
		getTransport().read(buffer);
		buf.flip();
		buf.get(buffer);
		return buf.limit();
	}

	@Override
	public void write(byte[] buffer) throws IOException {
		getTransport().write(buffer);
	}
}
