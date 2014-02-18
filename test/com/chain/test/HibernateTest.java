package com.chain.test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chain.base.utils.encode.Encrypt;
import com.chain.ens.entity.base.User;
import com.chain.ens.entity.doc.Doc;

import junit.framework.TestCase;

public class HibernateTest extends TestCase {

	
	public void test1(){
		try {
			ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
			SessionFactory sf = (SessionFactory) ac.getBean("sessionFactory");
			Session s = sf.openSession();
			s.beginTransaction();
			//User user = (User) s.createQuery("From User u where u.id=1").uniqueResult();
			User user = new User();
			user.setLoginName("admin");
			user.setPassword(Encrypt.md5AndSha("admin"));
			user.setName("系统管理员");
			s.saveOrUpdate(user);
			s.getTransaction().commit();
			s.close();
			System.out.println(Encrypt.md5AndSha("adminasdasdadsad..").length());
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userManager' defined in file [C:\Users\Administrator\git\nd\WebRoot\WEB-INF\classes\com\chain\ens\service\base\UserManager.class]: Initialization of bean failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sessionFactory' defined in class path resource [applicationContext.xml]: Invocation of init method failed; nested exception is org.hibernate.HibernateException: Unable to get the default Bean Validation factory
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:527)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:456)
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:294)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:225)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:291)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:193)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:607)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:925)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:472)
	at org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:139)
	at org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:83)
	at com.chain.test.HibernateTest.test1(HibernateTest.java:18)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:606)
	at junit.framework.TestCase.runTest(TestCase.java:164)
	at junit.framework.TestCase.runBare(TestCase.java:130)
	at junit.framework.TestResult$1.protect(TestResult.java:106)
	at junit.framework.TestResult.runProtected(TestResult.java:124)
	at junit.framework.TestResult.run(TestResult.java:109)
	at junit.framework.TestCase.run(TestCase.java:120)
	at junit.framework.TestSuite.runTest(TestSuite.java:230)
	at junit.framework.TestSuite.run(TestSuite.java:225)
	at org.eclipse.jdt.internal.junit.runner.junit3.JUnit3TestReference.run(JUnit3TestReference.java:130)
	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:467)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:683)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:390)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:197)
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sessionFactory' defined in class path resource [applicationContext.xml]: Invocation of init method failed; nested exception is org.hibernate.HibernateException: Unable to get the default Bean Validation factory
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1455)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:519)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:456)
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:294)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:225)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:291)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:193)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireByName(AbstractAutowireCapableBeanFactory.java:1136)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1086)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:517)
	... 29 more
Caused by: org.hibernate.HibernateException: Unable to get the default Bean Validation factory
	at org.hibernate.cfg.beanvalidation.BeanValidationActivator.applyDDL(BeanValidationActivator.java:127)
	at org.hibernate.cfg.Configuration.applyBeanValidationConstraintsOnDDL(Configuration.java:1704)
	at org.hibernate.cfg.Configuration.applyConstraintsToDDL(Configuration.java:1654)
	at org.hibernate.cfg.Configuration.secondPassCompile(Configuration.java:1445)
	at org.hibernate.cfg.Configuration.buildMappings(Configuration.java:1375)
	at org.springframework.orm.hibernate3.LocalSessionFactoryBean.buildSessionFactory(LocalSessionFactoryBean.java:717)
	at org.springframework.orm.hibernate3.AbstractSessionFactoryBean.afterPropertiesSet(AbstractSessionFactoryBean.java:188)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1514)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1452)
	... 38 more
Caused by: java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:606)
	at org.hibernate.cfg.beanvalidation.BeanValidationActivator.applyDDL(BeanValidationActivator.java:118)
	... 46 more
Caused by: org.hibernate.HibernateException: Unable to build the default ValidatorFactory
	at org.hibernate.cfg.beanvalidation.TypeSafeActivator.getValidatorFactory(TypeSafeActivator.java:383)
	at org.hibernate.cfg.beanvalidation.TypeSafeActivator.applyDDL(TypeSafeActivator.java:109)
	... 51 more
Caused by: javax.validation.ValidationException: Unable to instantiate Configuration.
	at javax.validation.Validation$GenericBootstrapImpl.configure(Validation.java:272)
	at javax.validation.Validation.buildDefaultValidatorFactory(Validation.java:111)
	at org.hibernate.cfg.beanvalidation.TypeSafeActivator.getValidatorFactory(TypeSafeActivator.java:380)
	... 52 more
Caused by: java.lang.NullPointerException
	at java.util.ResourceBundle.getBundle(ResourceBundle.java:1026)
	at org.hibernate.validator.engine.ResourceBundleMessageInterpolator.loadBundle(ResourceBundleMessageInterpolator.java:202)
	at org.hibernate.validator.engine.ResourceBundleMessageInterpolator.getFileBasedResourceBundle(ResourceBundleMessageInterpolator.java:182)
	at org.hibernate.validator.engine.ResourceBundleMessageInterpolator.<init>(ResourceBundleMessageInterpolator.java:81)
	at org.hibernate.validator.engine.ResourceBundleMessageInterpolator.<init>(ResourceBundleMessageInterpolator.java:73)
	at org.hibernate.validator.engine.ConfigurationImpl.<init>(ConfigurationImpl.java:57)
	at org.hibernate.validator.HibernateValidator.createGenericConfiguration(HibernateValidator.java:43)
	at javax.validation.Validation$GenericBootstrapImpl.configure(Validation.java:269)
	... 54 more


		 */
	}
}
