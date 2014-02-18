package com.chain.ens.web;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.chain.base.model.Result;
import com.chain.base.orm.entity.StatusState;
import com.chain.base.utils.StringUtils;
import com.chain.base.utils.SysConstants;
import com.chain.base.utils.encode.Encrypt;
import com.chain.base.web.struts2.SimpleActionSupport;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.chain.ens.entity.base.User;
import com.chain.ens.service.base.UserManager;
import com.chain.ens.utils.AppUtils;

@SuppressWarnings("serial")
public class LoginAction extends SimpleActionSupport {

	private String loginName;

	private String password;

	private String theme;
	/**
	 * 提示信息
	 */
	private String msg;

	@Autowired
	private UserManager userManager;

	public String welcome() throws Exception {
		return list();
	}

	public String list() throws Exception {
		return SUCCESS;
	}

	public String login() {
		Validate.notBlank(loginName, "用户名不能为空!");
		Validate.notBlank(password, "密码不能为空!");
		Result result = null;
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
			msg = "用户名或密码不能为空!";
			result = new Result(Result.ERROR, msg, null);
			Struts2Utils.renderText(result);
			return null;
		}
		User user = userManager.getUserByLP(loginName, Encrypt
				.md5AndSha(password));
		if (user == null) {
			msg = "用户名或密码不正确!";
		} else if (user.getStatus().intValue() == StatusState.lock.getValue()) {
			msg = "该用户已被锁定，暂不允许登陆!";
		}
		if (msg != null) {
			result = new Result(Result.ERROR, msg, null);
			Struts2Utils.renderText(result);
			return null;
		}
		if (user.getId() == userManager.getSuperUser().getId()) {
			user.setSuper(true);
		} else {// 设置菜单....
		}
		Struts2Utils.getSession().setAttribute(SysConstants.SESSION_USER, user);
		AppUtils.putUserToSession(user);
		logger.info("用户{}登录系统,IP:{}.", user.getLoginName(),Struts2Utils.getIp());
		//设置调整URL 如果session中包含未被授权的URL 则跳转到该页面
        String resultUrl = Struts2Utils.getRequest().getContextPath()+"/login!index.action";
        Object unAuthorityUrl = Struts2Utils.getSession().getAttribute(SysConstants.SESSION_UNAUTHORITY_URL);
        if(unAuthorityUrl != null){
            resultUrl = unAuthorityUrl.toString();
            //清空未被授权的URL
            Struts2Utils.getSession().setAttribute(SysConstants.SESSION_UNAUTHORITY_URL,null);
        }
        result = new Result(Result.SUCCESS, user.getName(),resultUrl);
        Struts2Utils.renderText(result);
        return null;
	}

	  /**
     * 后台管理主界面
     * @return
     * @throws Exception
     */
    public String index() throws Exception {
        //根据客户端指定的参数跳转至 不同的主题 如果未指定 默认:index
        if(StringUtils.isNotBlank(theme) && (theme.equals("app") || theme.equals("index"))){
            return theme;
        }else{
            return "index";
        }
    }
    
    /**
     * 注销登录
     */
    public String logout() {
        try {
            // 退出时清空session中的内容
            User user = (User) Struts2Utils.getSessionAttribute(SysConstants.SESSION_USER);
            Struts2Utils.getSession().setAttribute(SysConstants.SESSION_USER, null);
            String sessionId = Struts2Utils.getSession().getId();
            //由监听器更新在线用户列表
            AppUtils.removeUserFromSession(sessionId);
            logger.info("用户{}退出系统.", user.getLoginName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
