package com.chain.ens.service.doc;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.orm.hibernate.HibernateDao;
import com.chain.ens.entity.doc.Doc;

@Service
public class DocManager extends EntityManager<Doc, Long>{
	private HibernateDao<Doc, Long> docDao;
	@Override
	protected HibernateDao<Doc, Long> getEntityDao() {
		return docDao;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		docDao = new HibernateDao<Doc, Long>(sessionFactory, Doc.class);
	}
	
	public List<Doc> getAllDocs(){
//		String hql = "From Doc d";
		return docDao.getAll();
	}  
	
	
	

}
