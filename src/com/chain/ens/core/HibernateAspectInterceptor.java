package com.chain.ens.core;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.chain.base.utils.SysConstants;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.chain.ens.entity.base.User;

/**
 * Hiberate拦截器 实现修改人自动注入.
 * 
 * @author chain
 * @date 2013-3-21 上午12:30:54
 * 
 */
public class HibernateAspectInterceptor extends EmptyInterceptor {
	private static final Logger logger = Logger
			.getLogger(HibernateAspectInterceptor.class);
	private static final long serialVersionUID = 1L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		logger.debug("onSave");
		User user = null;
		try {
			user = (User) Struts2Utils
					.getSessionAttribute(SysConstants.SESSION_USER);
			if (user == null) {
				logger.warn("session中未获取到用户.");
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		try {
			// 添加数据
			for (int index = 0; index < propertyNames.length; index++) {
				if ("createUser".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"创建人名称"属性赋上值 */
					if (state[index] == null) {
						state[index] = user.getLoginName();
					}
					continue;
				}
				if ("createTime".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"创建时间"属性赋上值 */
					if (state[index] == null) {
						state[index] = new Date();
					}
					continue;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		logger.debug("onFlushDirty");
		User user = null;
		try {
			user = (User) Struts2Utils
					.getSessionAttribute(SysConstants.SESSION_USER);
			if (user == null) {
				logger.warn("session中未获取到用户.");
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		try {
			// 修改或添加数据
			for (int index = 0; index < propertyNames.length; index++) {
				if ("updateUser".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"修改人名称"属性赋上值 */
					currentState[index] = user.getLoginName();
					continue;
				}
				if ("updateTime".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"修改时间"属性赋上值 */
					currentState[index] = new Date();
					continue;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
