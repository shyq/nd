package com.chain.ens.entity.doc.state;

public enum DeleteState {
	/***  未删除(0) */
	undeleted(0,"未删除"),
	/***  已删除(1) */
	deleted(1,"已删除"),
	/***  永久删除(2) */
	Permanently(2,"永久删除");
	
	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;
	
	
	DeleteState(Integer value, String description) {
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
	
	public static DeleteState getDeleteState(Integer value) {
		if (null == value)
			return null;
		for (DeleteState _enum : DeleteState.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static DeleteState getDeleteState(String description) {
		if (null == description)
			return null;
		for (DeleteState _enum : DeleteState.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}
}
