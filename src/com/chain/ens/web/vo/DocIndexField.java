package com.chain.ens.web.vo;

/**
 * 文档索引实体类
 * @author Chain
 *
 */
public class DocIndexField {
	private String id;
	private String name;
	private String creatorId;
	private String createTimeStr;
	private String format;
	private String content;
	private String cpath;
	
	public DocIndexField() {
		super();
	}
	public DocIndexField(String id, String name, String creatorId,
			String createTimeStr, String format, String content, String cpath) {
		super();
		this.id = id;
		this.name = name;
		this.creatorId = creatorId;
		this.createTimeStr = createTimeStr;
		this.format = format;
		this.content = content;
		this.cpath = cpath;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
	
	
}
