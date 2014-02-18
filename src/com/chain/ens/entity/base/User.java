package com.chain.ens.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.chain.base.orm.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_USER")
public class User extends BaseEntity {
	 /**
     * 登录用户
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 
     */
    private String sex ;

    private Integer onlineStatus;
    
    /**
     * 邮件 以 ","分割
     */
    private String email;
    /**
     * 住址
     */
    private String address;
    /**
     * 住宅电话 以 ","分割
     */
    private String tel;
    /**
     * 手机号 以 ","分割
     */
    private String phone;
    
    private boolean isSuper = false;
    
    
    @Column(name="LOGIN_NAME")
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="PASSWORD",length=45)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="SEX")
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Column(name="ONLINE_STATUS")
	public Integer getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	
	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="ADDRESS")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name="TEL")
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@Column(name="PHONE")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Transient
	public boolean isSuper() {
		return isSuper;
	}
	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}
    
    
    
}
