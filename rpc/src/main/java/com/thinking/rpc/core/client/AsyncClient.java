package com.thinking.rpc.core.client;

public abstract class AsyncClient {

	//客户端超时时间，主要用于方法调用，每次方法调用的超时控制
	public abstract long getTimeout();
}
