package com.chain.ens.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chain.base.orm.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_USER")
public class User extends BaseEntity {
	private String username;
	private String password;
	
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
