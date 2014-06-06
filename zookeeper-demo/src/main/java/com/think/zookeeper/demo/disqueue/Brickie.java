package com.think.zookeeper.demo.disqueue;

/**
 * 泥瓦匠
 * @author diaoyouwei
 *
 */
public class Brickie extends Worker{

	public Brickie(String name,Project project) {
		super(project);
		this.name = name;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void doWork() throws Exception {
		LOG.info("泥瓦匠："+name+"正在打地基.....");
		Thread.currentThread().sleep(2000);
		submitJob();
		LOG.info("泥瓦匠："+name+"打完地基!");
	}

	@Override
	protected void submitJob() throws Exception {
		this.createZNode(project.getAddress()+"/zhangsan",name+" done!");
	}
}
