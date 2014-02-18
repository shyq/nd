package com.chain.ens.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chain.base.model.SessionInfo;

/**
 * 系统使用的静态变量.
 * 
 * @author chain
 * @date 2013-03-17 上午8:25:36
 */
public class AppConstants {
	/**
	 * 修改用户密码 个人(需要输入原始密码)
	 */
	public static final String USER_UPDATE_PASSWORD_YES = "1";
	/**
	 * 修改用户密码 个人(不需要输入原始密码)
	 */
	public static final String USER_UPDATE_PASSWORD_NO = "0";
	
	/**
	 * 超级管理员
	 */
	public static final String ROLE_SUPERADMIN = "超级管理员";
	
	/**
	 * 在线用户列表.
	 */
	public static final Map<String,SessionInfo> sessionUser = new ConcurrentHashMap<String, SessionInfo>();
	

}
