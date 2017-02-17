package com.thinking.rpc.core.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("rawtypes")
public class AsyncMethodCallManager {
	//waiting queue，store AsyncMethodCall，that will be executed in future
	private static ConcurrentLinkedQueue<AsyncMethodCall> pendingQueue = new ConcurrentLinkedQueue<AsyncMethodCall>();
	//worker is daemon thread,responsibility for rpc call
	private Worker worker;
	
	public AsyncMethodCallManager(){
		worker = new Worker();
		//don't hold server when stop server
		worker.setDaemon(true);
		worker.running = true;
		worker.start();
	}
	public void finish(){
		worker.running = false;
	}
	public void submit(AsyncMethodCall<?> asyncMethodCall) {
		pendingQueue.add(asyncMethodCall);
		worker.selector.wakeup();
	}
	public static class Worker extends Thread{
		private Selector selector;
		private volatile boolean running;
		public Worker() {
			try {
				selector = Selector.open();
			} catch (IOException e) {
				e.printStackTrace();
			}
			running = true;
		}
		@Override
		public void run() {
			while(running){
				try {
					selector.select();
					dealWithInstrestEvent();
					registerPendingMethodCall();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void dealWithInstrestEvent() {
			Set<SelectionKey> events = selector.selectedKeys();
			Iterator<SelectionKey> iter = events.iterator();
			iter.remove();
			
			SelectionKey event = iter.next();
			AsyncMethodCall method = (AsyncMethodCall) event.attachment();
			//将处理逻辑转移到对应的“方法”上
			method.transition(event);
		}
		private void registerPendingMethodCall() {
			AsyncMethodCall method = null;
			while((method = pendingQueue.poll())!=null){
				try {
					method.register(selector);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public Selector getSelector(){
			return selector;
		}
	}
	
}
