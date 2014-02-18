package com.chain.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chain.base.utils.encode.Encrypt;
import com.chain.ens.entity.base.User;
import com.chain.ens.entity.doc.Doc;

import junit.framework.TestCase;

public class HibernateTest extends TestCase {

	
	public void test1(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		SessionFactory sf = (SessionFactory) ac.getBean("sessionFactory");
		Session s = sf.openSession();
		s.beginTransaction();
		//User user = (User) s.createQuery("From User u where u.id=1").uniqueResult();
		User user = new User();
		user.setLoginName("admin");
		user.setPassword(Encrypt.md5AndSha("admin"));
		s.saveOrUpdate(user);
		s.getTransaction().commit();
		s.close();
		System.out.println(Encrypt.md5AndSha("adminasdasdadsad..").length());
	}
}
