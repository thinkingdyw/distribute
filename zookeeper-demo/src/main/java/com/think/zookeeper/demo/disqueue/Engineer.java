package com.think.zookeeper.demo.disqueue;

/**
 * 建筑师
 * @author diaoyouwei
 *
 */
public class Engineer extends Worker {

	
	public Engineer(String name,Project project) {
		super(project);
		this.name = name;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void doWork() throws Exception {
		LOG.info("建筑师："+name+"正在建楼...");
		Thread.currentThread().sleep(2000);
		submitJob();
		LOG.info("建筑师："+name+"建楼完毕!");
	}

	@Override
	protected void submitJob() throws Exception {
		this.createZNode(project.getAddress()+"/wangwu",name+" done!");
	}

}
