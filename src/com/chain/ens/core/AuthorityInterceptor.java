package com.chain.ens.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chain.base.utils.SysConstants;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 登录验证拦截器.
 * @author chain
 */
@SuppressWarnings("serial")
public class AuthorityInterceptor extends MethodFilterInterceptor{

    protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String doIntercept(ActionInvocation actioninvocation) throws Exception {
		Object user = Struts2Utils.getSessionAttribute(SysConstants.SESSION_USER);
		if(user != null){
			return actioninvocation.invoke(); //递归调用拦截器
		}else{
			return Action.LOGIN; //返回到登录页面
		}		
	}

}
