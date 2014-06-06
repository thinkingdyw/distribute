package com.think.zookeeper.demo.watch;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooKeeper;

import com.think.zookeeper.conf.ZookeeperConfiguration;

public class ZkMainClient implements Watcher{

	private static Logger LOG = Logger.getLogger(ZkMainClient.class);
	private CountDownLatch synLock;
	public ZkMainClient(CountDownLatch synLock) {
		this.synLock = synLock;
	}

	@Override
	public void process(WatchedEvent event) {
		if(event.getState() == KeeperState.SyncConnected){
			LOG.info("成功建立zookeeper连接!");
			synLock.countDown();
		}
		LOG.info(event);
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		
		CountDownLatch synLock = new CountDownLatch(1);
		CountDownLatch lisLock = new CountDownLatch(1);
		
		ZookeeperConfiguration zkConfig = new ZookeeperConfiguration();
		
		ZooKeeper zk = new ZooKeeper(zkConfig.getConnectString(), 
				zkConfig.getSessionTimeout(), new ZkMainClient(synLock));
		LOG.info("正在建立zookeeper连接...");
		synLock.await();
		
		byte[] data = zk.getData("/dyw/znodes/persisitent-node", new ZkNodeListener(lisLock), new Stat());
		
		LOG.info(new String(data));
		
		lisLock.await();
		
		zk.close();
	}
}
