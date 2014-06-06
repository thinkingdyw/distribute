package com.think.zookeeper.demo.dislock;

import com.think.zookeeper.core.ZookeeperLock;

public class DistributeLock implements ZookeeperLock{

	private String name;
	private String fullName;
	private String path;
	private String prefix;
	private String suffix;
	private String splitTag;
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getFullName() {
		if(fullName == null || fullName.trim().equals("")){
			return prefix + suffix;
		}
		return fullName;
	}
	@Override
	public String getPath() {
		return path;
	}
	@Override
	public String getPrefix() {
		return prefix;
	}
	@Override
	public long getSuffix() {
		return Long.valueOf(suffix);
	}
	@Override
	public String getSplitTag() {
		return splitTag;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public void setSuffix(long suffix) {
		this.suffix = String.valueOf(suffix);
	}
	public void setSplitTag(String splitTag) {
		this.splitTag = splitTag;
	}
	public void setFullName(String fullName){
		this.fullName = fullName;
	}
}
