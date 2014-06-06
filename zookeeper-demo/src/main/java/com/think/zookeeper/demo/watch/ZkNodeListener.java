package com.think.zookeeper.demo.watch;

import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ZkNodeListener implements Watcher{
	
	private static Logger LOG = Logger.getLogger(ZkNodeListener.class);
	
	private CountDownLatch lisLock;
	
	public ZkNodeListener(CountDownLatch lisLock) {
		this.lisLock = lisLock;
	}
	@Override
	public void process(WatchedEvent event) {
		if(event.getType() == EventType.NodeDeleted){
			LOG.info("节点："+event.getPath()+"已被删除!");
			lisLock.countDown();
		}
		if(event.getType() == EventType.NodeDataChanged){
			LOG.info("节点："+event.getPath()+"数据被改变!");
			lisLock.countDown();
		}
		if(event.getType() == EventType.NodeChildrenChanged){
			LOG.info("节点："+event.getPath()+"子节点数据被改变!");
			lisLock.countDown();
		}
	}

}
