package com.chain.ens.entity.doc.state;

public enum DocType {
	/***  文件(0) */
	file(0,"文件"),
	/***  文件夹(1) */
	folder(1,"文件夹");
	
	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;
	
	
	DocType(Integer value, String description) {
		this.value = value;
		this.description = description;
	}
	
	/**
	 * 获取值
	 * @return value
	 */
	public Integer getValue() {
		return value;
	}
	
	/**
     * 获取描述信息
     * @return description
     */
	public String getDescription() {
		return description;
	}
	
	public static DocType getDocType(Integer value) {
		if (null == value)
			return null;
		for (DocType _enum : DocType.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static DocType getDocType(String description) {
		if (null == description)
			return null;
		for (DocType _enum : DocType.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}
}
