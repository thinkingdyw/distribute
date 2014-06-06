package com.think.zookeeper.demo.dislock;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.think.zookeeper.conf.ZookeeperConfiguration;

public class TicketZKClientProxy implements ZkClient{

	private String name;
	/**
	 * 默认重试：3次
	 */
	private int retryCount = 3;
	/**
	 * 默认延迟重试时间：3秒
	 */
	private int retryDelay = 3000;
	/**
	 * 成功添加锁后往下执行
	 */
	private CountDownLatch synLock = new CountDownLatch(1);;
	/**
	 * 只有成功连接到zookeeper服务器以后才往下执行，所以需要同步
	 */
	private CountDownLatch synConnLock = new CountDownLatch(1);
	private ZooKeeper zk;
	private DistributeLock lock;
	private ZookeeperConfiguration zkConfig;
	
	private Logger LOG = Logger.getLogger(getClass());
	
	{
		zkConfig = new ZookeeperConfiguration();
	}
	
	public TicketZKClientProxy(String name, int retryCount, int retryDelay) {
		this.name = name;
		this.retryCount = retryCount;
		this.retryDelay = retryDelay;
	}
	
	@Override
	public void lock() throws Exception{
		try {
			//1、连接到服务器
			connectToZkServer();
			//2、创建分布式锁
			createDistributeLock();
			//3、验证创建的锁是否是最早创建的
			if(isMinLock()){
				//如果自己创建的锁是最早创建的，则往下执行
				mainProcessContinue();
			}else{
				//获取比自己较早创建的锁
				DistributeLock minnerLockThanOneSelf = getMinnerLockThanOneself();
				if(minnerLockThanOneSelf == null){
					//没有比自己创建的锁要造的锁，则往下执行
					mainProcessContinue();
					return;
				}else{
					//监听比自己创建的的锁要早的锁，注意此过程是：异步操作，即：子进程在监听，需要等到子进程完毕，主进程才往下继续执行
					Stat stat = zk.exists(minnerLockThanOneSelf.getPath()+"/"+minnerLockThanOneSelf.getFullName(), new Watcher() {
						@Override
						public void process(WatchedEvent event) {
							if(event.getType() == EventType.NodeDeleted){
								//如果监听的锁，被删除，则重新验证自己创建的锁在当前是否是最早创建的
								try {
									if(isMinLock()){
										//如果是当前是最早创建的，则主进程往下执行
										mainProcessContinue();
									}
								} catch (KeeperException e) {
									LOG.error("监听较早创建的锁异常!",e);
									throw new RuntimeException(name+":监听较早创建的锁异常!",e);
								} catch (InterruptedException e) {
									LOG.error("监听较早创建的异常中断!",e);
									throw new RuntimeException(name+":监听较早创建的异常中断!",e);
								}
							}
						}
					});
					if(stat == null){
						//不存在监听的锁，往下执行
						mainProcessContinue();
					}
				}
			}
			//主进程等待加锁
			mainProcessWait();
			LOG.debug(name+":成功创建分布式锁："+lock.getFullName());
		} catch (Exception e) {
			LOG.error("加锁失败,重试中...",e);
			try {
				retry();
			} catch (Exception e1) {
				LOG.error("重试失败!",e1);
				throw e1;
			}
		}
	}
	/**
	 * 主进程等待
	 * @throws InterruptedException
	 */
	private void mainProcessWait() throws InterruptedException {
		synLock.await();
	}
	/**
	 * 主进程继续往下执行
	 */
	private void mainProcessContinue(){
		synLock.countDown();
	}
	@SuppressWarnings("static-access")
	private void retry() {
		int retriedCount = 1;
		for (;retriedCount <= retryCount; retriedCount++) {
			try {
				Thread.currentThread().sleep(retryDelay);
				LOG.info("第："+retriedCount+"次重试!");
				lock();
			} catch (Exception e) {
				LOG.error("重试加锁第："+retriedCount+"次失败!",e);
			}
		}
		if(retriedCount >= retryCount){
			throw new RuntimeException("重试失败!");
		}
	}

	/**
	 * 获取比自己创建的锁要早创建的锁
	 * @return
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	private DistributeLock getMinnerLockThanOneself() throws KeeperException, InterruptedException {
		DistributeLock disLock = new DistributeLock();
		List<String> locks = zk.getChildren(lock.getPath(), false);
		if(locks.isEmpty()){
			return null;
		}
		TreeSet<Long> LockSeqs = new TreeSet<Long>();
		for (String lok : locks) {
			String[] splits = lok.split(lock.getSplitTag());
			//lock suffix
			LockSeqs.add(Long.valueOf(splits[1]));
		}
		disLock.setName(lock.getName());
		disLock.setPath(lock.getPath());
		disLock.setSplitTag(lock.getSplitTag());
		
		//获取自己小的锁的序号
		final Long minnerSeqThanOneSelf = LockSeqs.lower(lock.getSuffix());
		if(minnerSeqThanOneSelf == null){
			return null;
		}
		for (String lok : locks) {
			String[] splits = lok.split(lock.getSplitTag());
			//lock suffix
			if(minnerSeqThanOneSelf == Long.valueOf(splits[1])){
				disLock.setSuffix(minnerSeqThanOneSelf);
				disLock.setPrefix(lok.substring(0,lok.indexOf(String.valueOf(minnerSeqThanOneSelf))));
				disLock.setFullName(disLock.getPrefix()+disLock.getSuffix());
				break;
			}
		}
		
		return disLock;
	}

	private boolean isMinLock() throws KeeperException, InterruptedException {
		List<String> locks = zk.getChildren(lock.getPath(), false);
		TreeSet<Long> LockSeqs = new TreeSet<Long>();
		for (String lok : locks) {
			String[] splits = lok.split(lock.getSplitTag());
			//lock suffix
			LockSeqs.add(Long.valueOf(splits[1]));
		}
		if(LockSeqs.isEmpty()){
			return true;
		}
		final long seq = LockSeqs.first();
		
		return seq == lock.getSuffix();
	}

	private void createDistributeLock() throws KeeperException, InterruptedException {
		lock = new DistributeLock();
		lock.setName("lock-");
		lock.setPath("/train_station/dis_locks");
		lock.setSplitTag("-");
		ZkOperation createZNode = new ZkOperation() {
			@Override
			public void execute() {
				try {
					String lockPath = zk.create(lock.getPath()+"/"+lock.getName(), "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
					lock.setFullName(lockPath.substring(lockPath.lastIndexOf("/")+1));
				} catch (Exception e) {
					LOG.error(name+":创建锁失败!");
					throw new RuntimeException(name+":创建锁失败!");
				} 
			}
		};
		createZNode.execute();
		String[] splits = lock.getFullName().split(lock.getSplitTag());
		lock.setSuffix(splits[1]);
		lock.setPrefix(lock.getFullName().substring(0,lock.getFullName().indexOf(String.valueOf(lock.getSuffix()))));
	}
	
	@Override
	public void unlock() throws Exception{
		zk.delete(lock.getPath()+"/"+lock.getFullName(), -1);
		zk.close();
	}
	/**
	 * 建立客户端到zookeeper服务器的连接
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@Override
	public void connectToZkServer() throws Exception{
		
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
}
