package com.think.zookeeper.demo.disqueue;

import java.util.Date;

public class Project {

	private String address;
	private Date startTime;
	private Date endTime;
	private Worker pm;
	
	public Worker getPm() {
		return pm;
	}

	public void setPm(Worker pm) {
		this.pm = pm;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
