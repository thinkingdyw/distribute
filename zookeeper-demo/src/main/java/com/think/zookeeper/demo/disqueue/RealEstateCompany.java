package com.think.zookeeper.demo.disqueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * 房地产公司
 * @author diaoyouwei
 *
 */
public class RealEstateCompany {

	public static void build() throws Exception{
		//初始化项目
		Project project = initProject();
		//项目经理启动项目
		Worker pm = project.getPm();
		pm.doWork();
	}
	public static void main(String[] args) throws Exception {
		//开始建造楼房
		build();
	}
	
	public static Project initProject() throws ParseException{
		Project project = new Project();
		//项目地址
		project.setAddress("/dyw/disqueue");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		project.setStartTime(dateFormat.parse("2013-12-12"));
		project.setEndTime(dateFormat.parse("2016-12-12"));
		//选择项目经理
		project.setPm(new ProjectManager("项目经理-thinkingdyw",project));
		return project;
	}
}
