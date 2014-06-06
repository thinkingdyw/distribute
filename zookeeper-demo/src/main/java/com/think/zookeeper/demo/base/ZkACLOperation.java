package com.think.zookeeper.demo.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import com.think.zookeeper.conf.ZookeeperConfiguration;

/**
 * Zookeeper ACL基本操作
 * @author diaoyouwei
 *
 */
public class ZkACLOperation {
	
	private static Logger LOG = Logger.getLogger(ZkACLOperation.class);
	private ZooKeeper zk;

	{
		try {
			connect();
		} catch (Exception e) {
			LOG.error("初始化zookeeper失败!", e);
		}
	}
	public static void main(String[] args) throws InterruptedException, KeeperException {
		ZkACLOperation op = new ZkACLOperation();
		String zNode = "/dyw/acls/acl-create/c1-read";
		op.createZNode_ACL_READ(zNode);
		String data = op.readZNode(zNode);
		LOG.info(data);
		op.close();
	}
	
	/**
	 * 创建一个zNode
	 * @param zNode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String createZNode(String zNode,List<ACL> acls) throws KeeperException, InterruptedException{
		String znode = zk.create(zNode, "acl test data!".getBytes(), acls, CreateMode.PERSISTENT);
		
		return znode;
	}
	
	/**
	 * 创建ACL权限为：ALL 即所有权限的zNode,所有人在该节点下可以做任何操作
	 * @param zNode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String createZNode_ACL_ALL(String zNode) throws KeeperException, InterruptedException{
		List<ACL> acls = new ArrayList<ACL>();
		ACL acl1 = new ACL(Perms.ALL, new Id("world", "anyone"));
		acls.add(acl1);
		
		return this.createZNode(zNode,acls);
	}
	/**
	 * 所有人在该节点下只可以创建子节点
	 * @param zNode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String createZNode_ACL_Create(String zNode) throws KeeperException, InterruptedException{
		List<ACL> acls = new ArrayList<ACL>();
		ACL acl1 = new ACL(Perms.CREATE, new Id("world", "anyone"));
		acls.add(acl1);
		
		return this.createZNode(zNode,acls);
	}
	/**
	 * 
	 * @param zNode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String createZNode_ACL_READ(String zNode) throws KeeperException, InterruptedException{
		List<ACL> acls = new ArrayList<ACL>();
		ACL acl1 = new ACL(Perms.READ, new Id("world", "anyone"));
		acls.add(acl1);
		
		return this.createZNode(zNode,acls);
	}
	
	/**
	 * 读取zNode节点数据
	 * @param zNode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String readZNode(String zNode) throws KeeperException, InterruptedException{
		Stat stat = new Stat();
		byte[] data = zk.getData(zNode, false, stat);
		return new String(data);
	}
	/**
	 * 初始化zookeeper
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void connect() throws IOException, InterruptedException {
		
		ZookeeperConfiguration zkConfig = new ZookeeperConfiguration();
		
		final CountDownLatch synLock = new CountDownLatch(1);
		zk = new ZooKeeper(zkConfig.getConnectString(),
				zkConfig.getSessionTimeout(), new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						if (event.getState() == KeeperState.SyncConnected) {
							LOG.info("已建立到zookeeper的连接!");
							synLock.countDown();
						}
					}
				});
		LOG.info("正在建立到zookeeper的连接...");
		synLock.await();
	}
	
	
	public void close() throws InterruptedException{
		if(zk != null){
			zk.close();
		}
	}
}
