package com.chain.ens.web;

import com.chain.base.web.struts2.SimpleActionSupport;

@SuppressWarnings("serial")
public class LoginAction extends SimpleActionSupport{

	private String username;
	
	private String password;

	
	public String login(){
//    	if(username.equals("admin") && password.equals(password)){
//    		return "index";
//    	}
		return "index";
	}
	
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
