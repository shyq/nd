package com.chain.ens.service.base;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chain.base.exception.DaoException;
import com.chain.base.exception.ServiceException;
import com.chain.base.exception.SystemException;
import com.chain.base.orm.entity.StatusState;
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

	@SuppressWarnings("unchecked")
	public User getUserByLP(String loginName, String password) {
		Assert.notNull(loginName, "参数[loginName]为空!");
		Assert.notNull(password, "参数[password]为空!");
		List<User> list = userDao
				.createQuery(
						"from User u where u.loginName = ? and u.password = ? and u.status <> ?",
						new Object[] { loginName, password,
								StatusState.delete.getValue() }).list();
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * 得到超级用户.
	 * 
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public User getSuperUser() throws DaoException, SystemException,
			ServiceException {
		User superUser = userDao.load(1l);// 超级用户ID为1
		if (superUser == null) {
			throw new SystemException("系统未设置超级用户.");
		}
		return superUser;
	}

}
