package com.chain.ens.entity.doc;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.chain.base.orm.entity.BaseEntity;
import com.chain.ens.entity.base.User;
import com.chain.ens.entity.doc.state.DeleteState;
import com.chain.ens.entity.doc.state.DocType;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_DOC")
public class Doc extends BaseEntity{
	
	private String name;
	private String path;
	private String dir;
	private User own;
	private Integer docType = DocType.folder.getValue();
	private Integer deleteState = DeleteState.undeleted.getValue();
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="PATH")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Column(name="DIR")
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="OWN_ID")
	public User getOwn() {
		return own;
	}
	public void setOwn(User own) {
		this.own = own;
	}
	@Column(name="DOC_TYPE")
	public Integer getDocType() {
		return docType;
	}
	public void setDocType(Integer docType) {
		this.docType = docType;
	}
	
	@Column(name="DELETE_STATE")
	public Integer getDeleteState() {
		return deleteState;
	}
	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}
}
