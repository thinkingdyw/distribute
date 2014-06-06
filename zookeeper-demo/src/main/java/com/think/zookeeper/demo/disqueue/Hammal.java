package com.think.zookeeper.demo.disqueue;

/**
 * 搬运工
 * @author diaoyouwei
 *
 */
public class Hammal extends Worker {

	public Hammal(String name,Project project) {
		super(project);
		this.name = name;
	}

	@SuppressWarnings("static-access")
	@Override
	public void doWork() throws Exception {
		LOG.info("搬运工："+name+"正在搬运建筑材料...");
		Thread.currentThread().sleep(2000);
		submitJob();
		LOG.info("搬运工："+name+"搬运完毕!");
	}

	@Override
	protected void submitJob() throws Exception {
		this.createZNode(project.getAddress()+"/lisi",name+" done!");
	}
}
