package com.thinking.rpc.core.protocal;

import java.io.IOException;

import com.thinking.rpc.core.transport.Transport;

public abstract class Protocol {
	private Transport transport;
	
	public Protocol(Transport transport){
		this.setTransport(transport);
	}
	public abstract int read(byte[] buffer) throws IOException;
	public abstract void write(byte[] buffer) throws IOException;
	public Transport getTransport() {
		return transport;
	}
	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	
	
}