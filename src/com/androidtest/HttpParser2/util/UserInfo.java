package com.androidtest.HttpParser2.util;

public class UserInfo {
	private String id;
	private String pass;
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		if (name==null) return "";
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
