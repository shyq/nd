package com.chain.ens.entity.doc;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	private Doc parent;
	private Long parentId;
	private String parentName;
	private String format;
	private long fileSize;
	private String pdfPath;
	
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
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="PARENT_ID")
	public Doc getParent() {
		return parent;
	}
	public void setParent(Doc parent) {
		this.parent = parent;
	}
	
	
	
	
	@Column(name="PDF_PATH")
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	@Column(name="FILE_SIZE")
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	@Column(name="FORMAT")
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	@Transient
	public Long getParentId() {
		if(parent != null)
			parentId = parent.getId();
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Transient
	public String getParentName() {
		if(parent != null)
			parentName = parent.getName();
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
}
