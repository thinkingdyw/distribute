package com.think.zookeeper.demo.disqueue;

import java.io.IOException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

public class ProjectManager extends Worker{
	
	private int personCount;
	public ProjectManager(String name,Project project) {
		super(project);
		this.name = name;
	}

	@Override
	public void doWork() throws Exception {
		//start : 确定资源
		checkResource();
		//fork : 启动项目
		startProject();
		//join : 验收项目
		checkProject();
	}

	@SuppressWarnings("static-access")
	private void checkProject() throws Exception {
		initZk();
		int completedCount = zk.getChildren(project.getAddress(), false).size(); 
		while(completedCount != personCount){
			//没有建造完毕，等待......
			LOG.info("楼房建造中...,"+name+"，等待中......");
			Thread.currentThread().sleep(500);
			completedCount = zk.getChildren(project.getAddress(), false).size();
		}
		if(completedCount == personCount){
			LOG.info("楼房建造完毕,"+name+"验收中....");
			Thread.currentThread().sleep(1000);
			LOG.info("项目验收完毕");
			this.submitJob();
		}
	}

	/**
	 * TODO 优化：此处需要维护好各个团队之间的协作顺序
	 */
	private void startProject() {
		LOG.info(name+":正在启动项目...");
		Brickie brickie = new Brickie("Zhang San",project);
		Hammal hammal = new Hammal("Li Si",project);
		Engineer engnieer = new Engineer("Wang Wu",project);
		
		new Thread(brickie).start();
		new Thread(hammal).start();
		new Thread(engnieer).start();
		LOG.info("项目已启动!");
	}

	@SuppressWarnings("static-access")
	private void checkResource() throws IOException, InterruptedException, KeeperException {
		LOG.info(name+":正在准备资源.....");
		Thread.currentThread().sleep(1000);
		//确定人数
		personCount = 3;
		this.buildProject(project.getAddress(),String.valueOf(personCount));
		LOG.info(name+":资源准备完毕，员工："+personCount+" 人!");
	}

	@SuppressWarnings("static-access")
	@Override
	protected void submitJob() throws Exception {
		LOG.info("项目提交中.....");
		Thread.currentThread().sleep(1000);
		LOG.info("项目提交完毕！");
	}
	/**
	 * 构建项目
	 * @param zNode
	 * @param content
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void buildProject(String zNode, String content) throws KeeperException, InterruptedException, IOException{
		initZk();
		zk.create(zNode,content.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.close();
	}
}
