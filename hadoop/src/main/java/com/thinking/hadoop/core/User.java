package com.thinking.hadoop.core;

import com.thinking.hadoop.core.io.MREntity;

public class User extends MREntity{

	private String name;
	private String sex;
	private int age;
	public String getName() {
		return name;
	}
	public String getSex() {
		return sex;
	}
	public int getAge() {
		return age;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
