package com.think.zookeeper.demo.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.think.zookeeper.conf.ZookeeperConfiguration;

/**
 * 对zookeeper的基本操作：CURD
 * @author diaoyouwei
 *
 */
public class ZkBaseOperation {
	private static Logger LOG = Logger.getLogger(ZkBaseOperation.class);
	
	private ZooKeeper zk;
	
	{
		try {
			connect();
		} catch (Exception e) {
			LOG.error("初始化zookeeper失败!", e);
		} 
	}
	public void close() throws InterruptedException{
		if(zk != null){
			zk.close();
		}
	}
	public static void main(String[] args) throws UnsupportedEncodingException, KeeperException, InterruptedException {
		ZkBaseOperation op = new ZkBaseOperation();
		op.createPersistentZNode("/dyw/znodes/persisitent-node");
		op.close();
	}
	/**
	 * 创建瞬时有序节点
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * @throws UnsupportedEncodingException 
	 */
	public String createEphemeralSequentialZNode(String zNode) throws KeeperException, InterruptedException, UnsupportedEncodingException{

		String node = zk.create(zNode, "测试：创建瞬时有序节点!".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		
		LOG.info("创建瞬时有序节点："+node);
		
		return node;
	}
	/**
	 * 创建瞬时节点
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * @throws UnsupportedEncodingException 
	 */
	public String createEphemeralZNode(String zNode) throws KeeperException, InterruptedException, UnsupportedEncodingException{

		String node = zk.create(zNode, "测试：创建瞬时有序节点!".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		LOG.info("创建瞬时节点："+node);
		
		return node;
	}
	/**
	 * 创建持久节点
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * @throws UnsupportedEncodingException 
	 */
	public String createPersistentZNode(String zNode) throws KeeperException, InterruptedException, UnsupportedEncodingException{

		String node = zk.create(zNode, "测试：创建持久节点!".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		LOG.info("创建持久节点："+node);
		
		return node;
	}
	/**
	 * 创建持久有序节点
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * @throws UnsupportedEncodingException 
	 */
	public String createPersistentSeqZNode(String zNode) throws KeeperException, InterruptedException, UnsupportedEncodingException{

		String node = zk.create(zNode, "测试：创建持久节点!".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
		
		LOG.info("创建持久有序节点："+node);
		
		return node;
	}
	/**
	 * 获取指定zNode节点的数据
	 * @param zNode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String getDataOfZNode(String zNode) throws KeeperException, InterruptedException{
		Stat stat = new Stat();
		byte[] data = zk.getData(zNode, false, stat);
		return new String(data);
	}
	/**
	 * 删除zNode
	 * @param zNode
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void deleteZNode(String zNode) throws InterruptedException, KeeperException{
		zk.delete(zNode, -1);
	}
	
	/**
	 * 更新zNode的数据
	 * @param zNode
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void updateZNodeData(String zNode,String data) throws KeeperException, InterruptedException{
		Stat stat = new Stat();
		/**
		 * 注意：
		 * 1、更新前必须先获取该节点，得到更新前zNode节点数据的版本号，如果不获取，则只在第一次时有效，之后如果不知道更新前的版本号，会报错；
		 * 2、此外没有必要添加watcher
		 */
		zk.getData(zNode, false, stat);
		stat = zk.setData(zNode, data.getBytes(), stat.getVersion());
	}
	private void connect() throws IOException, InterruptedException{
		ZookeeperConfiguration zkConfig = new ZookeeperConfiguration();
		final CountDownLatch synLock = new CountDownLatch(1);
		zk = new ZooKeeper(zkConfig.getConnectString(), zkConfig.getSessionTimeout(), new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				if(event.getState() == KeeperState.SyncConnected){
					LOG.info("已建立到zookeeper的连接!");
					synLock.countDown();
				}
			}
		});
		LOG.info("正在建立到zookeeper的连接...");
		synLock.await();
	}
}
