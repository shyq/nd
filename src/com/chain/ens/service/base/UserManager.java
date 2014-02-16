package com.chain.ens.service.base;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.orm.hibernate.HibernateDao;
import com.chain.ens.entity.base.User;

@Service
public class UserManager extends EntityManager<User, Long> {

	private HibernateDao<User, Long> userDao;

	@Override
	protected HibernateDao<User, Long> getEntityDao() {
		return userDao;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userDao = new HibernateDao<User, Long>(sessionFactory, User.class);
	}
	
}
