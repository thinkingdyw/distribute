package com.thinking.rpc.core.client;

public abstract class AsyncMethodCallBack<T> {

	public abstract void onComplete(T result);
	public abstract void onError(Throwable exception);
}
