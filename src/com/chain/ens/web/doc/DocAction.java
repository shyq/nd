package com.chain.ens.web.doc;

import org.springframework.beans.factory.annotation.Autowired;

import com.chain.base.model.Result;
import com.chain.base.orm.Page;
import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.web.struts2.StrutsAction;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.chain.ens.entity.base.User;
import com.chain.ens.entity.doc.Doc;
import com.chain.ens.service.base.UserManager;
import com.chain.ens.service.doc.DocManager;

@SuppressWarnings("serial")
public class DocAction extends StrutsAction<Doc> {
	
	@Autowired
	private DocManager docManager;
	@Autowired
	private UserManager userManager;
	@Override
	public EntityManager<Doc, Long> getEntityManager() {
		return docManager;
	}
	
	public String list(){
		Struts2Utils.renderText(docManager.getAll());
		return null;
	}
	
	/**
	 * 在执行getDocs()前进行二次绑定，否则model为空 
	 */
	public void prepareGetAllDocs(){
		model = new Doc();
	}
	/**
	 * 查询所有文件
	 */
	public void getAllDocs(){
		Page<Doc> docs = null;
		try {
			docs = docManager.getAllDocs(model,getQueryPage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Struts2Utils.renderJson(docs);
	}
	
	
	public String save(){
		String name = Struts2Utils.getParameter("name");
		User u = userManager.getById(1L);
		model.setOwn(u);
		model.setName(name);
		docManager.save(model);
//		Result result = null;
//		 result = new Result(Result.WARN, "名称为[" + model.getName()
//                 + "]已存在,请修正!", "name");
		Struts2Utils.renderText(Result.successResult());
		return null;
	}

}
