package com.chain.ens.service.base.state;
/**
 * 
 * 状态标识 枚举类型.
 * 
 * @author chain
 * @date 2012-8-11 下午10:48:23
 * 
 */
public enum StatusState {
	/** 正常(0) */ 
	normal(0, "正常"),
	/** 已删除(1) */
	delete(1, "已删除"),
	/** 待审核(2) */
	audit(2, "待审核"), 
	/** 锁定(3) */
	lock(3, "已锁定");

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	StatusState(Integer value, String description) {
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

	public static StatusState getStatusState(Integer value) {
		if (null == value)
			return null;
		for (StatusState _enum : StatusState.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static StatusState getStatusState(String description) {
		if (null == description)
			return null;
		for (StatusState _enum : StatusState.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}

}