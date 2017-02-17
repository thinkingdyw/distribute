package com.thinking.rpc.core.transport;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NonblockingTransport extends Transport {

	private SocketChannel channel;
	
	public void close() throws IOException {
		channel.close();
	}

	public boolean isOpen() {
		return channel.isOpen() && channel.isConnected();
	}

	public SelectionKey register(Selector selector, int ops) throws ClosedChannelException {
		return channel.register(selector, ops);
	}

	public boolean finishConnect() throws IOException {
		return channel.finishConnect();
	}


	@Override
	public int read(byte[] buffer, int off, int length) {
		// TODO Auto-generated method stub
		return 0;
	}

}
