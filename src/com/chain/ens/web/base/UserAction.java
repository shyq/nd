package com.chain.ens.web.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.chain.base.exception.ActionException;
import com.chain.base.model.Result;
import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.web.struts2.StrutsAction;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.chain.ens.entity.base.User;
import com.chain.ens.service.base.UserManager;

@SuppressWarnings("serial")
public class UserAction extends StrutsAction<User> {
	@Autowired
	private UserManager userManager; 
	
	@Override
	public EntityManager<User, Long> getEntityManager() {
		return userManager;
	}

	public String save() throws ActionException {
		try {
			userManager.saveOrUpdate(model);
			Struts2Utils.renderText(Result.successResult());
		} catch (ActionException e) {
			throw e;
		}
		return null;
	}
	
}
