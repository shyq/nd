package com.chain.ens.web.doc;

import org.springframework.beans.factory.annotation.Autowired;

import com.chain.base.model.Result;
import com.chain.base.orm.Page;
import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.web.struts2.StrutsAction;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.chain.ens.entity.base.User;
import com.chain.ens.entity.doc.Doc;
import com.chain.ens.entity.doc.state.DocType;
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
		Result result =  null;
		User u = userManager.getById(1L);
		if(model.getParentId()!=null && !model.getParentId().equals(-1L)){
			Doc parent = docManager.getById(model.getParentId());
			model.setParent(parent);
			model.setOwn(parent.getOwn());
			model.setDir(parent.getDir() + "/" + parent.getName() + "#" + parent.getId());
		}else{
			model.setParent(null);
			model.setOwn(u);
			model.setDir("/" + u.getId() );
		}
		model.setDocType(DocType.folder.getValue());
//		model.setCreateTime(new Date());
		docManager.save(model);
		result = new Result(Result.SUCCESS, "添加成功", "{name:'"+model.getName()+"',id:'"+model.getId()+"'}");
		Struts2Utils.renderText(result);
		return null;
	}

}
