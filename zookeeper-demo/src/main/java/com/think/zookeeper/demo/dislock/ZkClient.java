package com.think.zookeeper.demo.dislock;


public interface ZkClient{

	public void lock() throws Exception;
	
	public void unlock()throws Exception;

	void connectToZkServer() throws Exception;
}
