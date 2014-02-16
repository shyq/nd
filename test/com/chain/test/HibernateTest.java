package com.chain.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chain.ens.entity.base.User;
import com.chain.ens.entity.doc.Doc;

import junit.framework.TestCase;

public class HibernateTest extends TestCase {

	
	public void test1(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		SessionFactory sf = (SessionFactory) ac.getBean("sessionFactory");
		Session s = sf.openSession();
		s.beginTransaction();
		User user = (User) s.createQuery("From User u where u.id=1").uniqueResult();
		for(int i = 0 ; i< 20; i++){
			Doc d = new Doc();
			d.setName("新建文件夹" + i);
			d.setCreateUser(user.getUsername());
			d.setOwn(user);
			d.setPath("/");
			d.setDir("/" + user.getId() + "/");
			s.save(d);
		}
		s.getTransaction().commit();
		s.close();
		System.out.println(s);
	}
}
