package com.chain.ens.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chain.base.model.Datagrid;
import com.chain.base.model.SessionInfo;
import com.chain.base.utils.StringUtils;
import com.chain.base.utils.SysConstants;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.chain.ens.entity.base.User;
import com.google.common.collect.Lists;

/**
 * 系统使用的特殊工具类 简化代码编写.
 * 
 * @author chain
 * @date 2012-10-18 上午8:25:36
 */
public class AppUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(AppUtils.class);
	/**
	 * User转SessionInfo.
	 * @param user
	 * @return
	 */
	public static SessionInfo userToSessionInfo(User user){
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setUserId(user.getId());
		sessionInfo.setLoginName(user.getLoginName());
	//	sessionInfo.setRoleIds(user.getRoleIds());
		sessionInfo.setIp(Struts2Utils.getIp());
		return sessionInfo;
	}
	
	/**
	 * 将用户放入session中.
	 * @param user
	 */
	public static void putUserToSession(User user){
		String sessionId = Struts2Utils.getSession().getId();
		logger.debug("putUserToSession:{}",sessionId);
		AppConstants.sessionUser.put(sessionId,userToSessionInfo(user));
	}
	
	public static void removeUserFromSession(String sessionId){
		if(StringUtils.isNotBlank(sessionId)){
			Set<String> keySet = AppConstants.sessionUser.keySet();
			for(String key:keySet){
				if(key.equals(sessionId)){
					logger.debug("removeUserFromSession:{}",sessionId);
					AppConstants.sessionUser.remove(key);
				}
			}
		}
	}
	
	public static Datagrid<SessionInfo> getSessionUser(){
		List<SessionInfo> list = Lists.newArrayList();
		Set<String> keySet = AppConstants.sessionUser.keySet();
		for(String key:keySet){
			SessionInfo sessionInfo = AppConstants.sessionUser.get(key);
			list.add(sessionInfo);
		}
		//排序
	    Collections.sort(list, new Comparator<SessionInfo>() {
			public int compare(SessionInfo o1, SessionInfo o2) {
				return o2.getLoginTime().compareTo(o1.getLoginTime());
			}
		}); 
			 
		Datagrid<SessionInfo> dg = new Datagrid<SessionInfo>(AppConstants.sessionUser.size(), list);
		return dg;
	}
	
	public static User getUser(){
		HttpSession session = Struts2Utils.getSession();
		return (User) session.getAttribute(SysConstants.SESSION_USER);
	}
	
	public static Long getUserId(){
		return getUser().getId();
	}

	public static List<User> sessionInfo2User() {
		List<User> users = new ArrayList<User>();
		if(AppConstants.sessionUser.size() <= 0)
			return users;
		for(SessionInfo s : AppConstants.sessionUser.values()){
			User user = new User();
			user.setId(s.getUserId());
			user.setLoginName(s.getLoginName());
//			user.setOnlineStatus(s.getOnlineStatus());
			users.add(user);
		}
		return users;
	}
}

