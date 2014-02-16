package com.chain.ens.service.doc;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chain.base.orm.Page;
import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.orm.hibernate.HibernateDao;
import com.chain.ens.entity.doc.Doc;
import com.chain.ens.entity.doc.state.DeleteState;

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

	public Page<Doc> getAllDocs(Doc model, Page<Doc> page) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from Doc d where d.deleteState=?");
		params.add(DeleteState.undeleted.getValue());
		///~ model.getParentId()==-1时表示没有父节点
		if(model.getParentId() == null || model.getParentId().longValue() == -1){
			hql.append(" and d.parent.id is null");	
		}else{
			hql.append(" and d.parent.id=?");
			params.add(model.getParentId().longValue());
		}
		
		hql.append(" order by d.").append(page.getOrderBy()).append(" ").append(page.getOrder());
		return docDao.findPage(page, hql.toString(), params.toArray());
	}  
	
	
	

}
