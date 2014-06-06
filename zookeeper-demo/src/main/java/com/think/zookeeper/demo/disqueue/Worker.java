package com.think.zookeeper.demo.disqueue;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

import com.think.zookeeper.conf.ZookeeperConfiguration;

/**
 * 生产者
 * @author diaoyouwei
 *
 */
public abstract class Worker implements Runnable{
	protected Logger LOG = Logger.getLogger(getClass());
	protected ZooKeeper zk;
	private CountDownLatch synConnLock;
	protected String name;
	protected Project project;
	public Worker(Project project){
		this.project = project;
	}
	@Override
	public void run() {
		try {
			doWork();
		} catch (Exception e) {
			LOG.error(name+":工作异常!没有完成任务!", e);
		}
	}

	public abstract void doWork() throws Exception;
	protected abstract void submitJob() throws Exception;
	
	protected void initZk() throws IOException, InterruptedException{
		synConnLock = new CountDownLatch(1);
		ZookeeperConfiguration zkConfig = new ZookeeperConfiguration();
		//连接到zookeeper服务器
		zk = new ZooKeeper(zkConfig.getConnectString(),zkConfig.getSessionTimeout() , new Watcher() {
			public void process(WatchedEvent event) {
				if(event.getState() == KeeperState.SyncConnected){
					LOG.info("已建立 ("+name+") 到zookeeper服务器的连接!");
					//释放同步锁
					synConnLock.countDown();
				}
			}
		});
		LOG.info("正在建立 ("+name+") 到zookeeper服务器的连接...");
		//等待连接到zookeeper服务器，成功连接后后往下执行
		synConnLock.await();
	}
	
	protected void createZNode(String zNode, String content) throws KeeperException, InterruptedException, IOException{
		initZk();
		zk.create(zNode,content.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.close();
	}
}
