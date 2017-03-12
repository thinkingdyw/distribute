package com.thinking.rpc.core.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicLong;

import com.thinking.rpc.core.protocol.JSONProtocol;
import com.thinking.rpc.core.protocol.Protocol;
import com.thinking.rpc.core.transport.MemoryBuffer;
import com.thinking.rpc.core.transport.NonblockingTransport;
import com.thinking.rpc.core.utils.Bits;

public abstract class AsyncMethodCall<T> {
	// 自增的全局计数器，标示本次调用的一个编号，如果返回的响应与请求的不一致，则说明出错了
	private static AtomicLong sequenceIdCounter = new AtomicLong(0);

	// 当前方法调用处于什么状态
	public static enum State {
		CONNECTING, // 连接
		WRITING_REQUEST_SIZE, // 写当前请求的大小
		WRITING_REQUEST_BODY, // 写当前请求body
		READING_RESPONSE_SIZE, // 读取响应的大小
		READING_RESPONSE_BODY, // 读取响应的body
		RESPONSE_READ, // 响应的body已经读完
		ERROR;// 发生错误
	}

	// 通信协议
	protected Protocol protocol;
	protected AsyncClient asyncClient;
	private NonblockingTransport transport;
	// 回调函数，用户注册的回调逻辑，请求返回后执行该回调，用户线程无感知
	protected AsyncMethodCallBack<T> callback;
	// 是否单向RPC
	private final boolean isOneWay;
	// 当前方法调用的状态
	private State state = null;
	// 方法调用具有超时控制
	private final long timeout;
	private final long sequenceId;

	//内容大小缓冲区，说明bodyBuffer的大小，由于要通过非阻塞io传输，因此不能用原始类型表示，只能用缓冲区表示
	private ByteBuffer bodySize = null;
	//内容缓冲区
	private ByteBuffer bodyBuffer = null;
	
	
	public AsyncMethodCall(Protocol protocol, AsyncMethodCallBack<T> callback, boolean oneWay) {
		this.protocol = protocol;
		this.callback = callback;
		this.isOneWay = oneWay;
		this.timeout = asyncClient.getTimeout();
		this.sequenceId = sequenceIdCounter.getAndIncrement();
	}

	public void register(Selector selector) throws IOException {
		SelectionKey event = null;
		if (transport.isOpen()) {
			event = transport.register(selector, SelectionKey.OP_WRITE);
		} else {
			event = transport.register(selector, SelectionKey.OP_CONNECT);
		}
		if (transport.finishConnect()) {
			event.interestOps(SelectionKey.OP_WRITE);
		}

		event.attach(this);
	}

	/**
	 * 将处理逻辑封装到基础类中
	 * @param event
	 */
	protected void transition(SelectionKey event) {
		// TODO Auto-generated method stub
	}
	/**
	 * 准备操作，执行异步方法调用的时候，提交到后端方法调用管理器时执行，
	 * 执行职责一个操作的时候是跟客户端同步的，之后就变成了异步操作
	 */
	public void prepare(){
		//1、首先将消息写入内存缓冲区，此时客户端就可以返回了，方法的调用交给方法调用管理器
		MemoryBuffer memoryBuffer = new MemoryBuffer(128);
		Protocol protocol = new JSONProtocol(memoryBuffer);
		write(protocol);
		
		bodyBuffer = ByteBuffer.wrap(memoryBuffer.getContent());
		//将整数转成字节
		byte[] sizeBytes = new byte[4];
		Bits.encode(memoryBuffer.length(), sizeBytes);
		//将转换后的字节转成缓冲区
		bodySize = ByteBuffer.wrap(sizeBytes);
	}
	//写操作
	protected abstract int write(Protocol protocol);
	
	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public static AtomicLong getSequenceIdCounter() {
		return sequenceIdCounter;
	}
}
