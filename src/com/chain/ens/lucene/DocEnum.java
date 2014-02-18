package com.chain.ens.lucene;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;



public enum DocEnum {
	//文件编号
	id(new FileField("id", true, false, 1f, false, 1)),
	//标题
	name(new FileField("name", true, true, 0.9f, true, 3)),
	//所属者ID
	creatorId(new FileField("creatorId", true, false, 0.9f, false, 1)),
	//上传日期
	createTimeStr(new FileField("createTimeStr", true, false, 0.5f, false, 3)),
	//格式
	format(new FileField("format", true, false, 0.9f, false, 1)),
	//内容  该字段要索引，但不存储，因为他的内容都在cpath中存储
	content(new FileField("content", false, true, 0.5f, true, 3)),
	//内容存储路径
	cpath(new FileField("cpath", true, false, 0f, false, 3));
	//0公共文档，1个人文档
	
	public final FileField f;
	
	/**
	 * 构造函数
	 * 
	 * @param f
	 */
	DocEnum(FileField f) {
//		Logger.getLogger(DocEnum.class).info(
//				f.getName() + " " + f.hashCode());
		this.f = f;
	}

	/**
	 * 设置内容
	 * 
	 * @param v
	 */
	public void setValue(String v) {
		this.f.setContent(v);
	}

	public FileField getF() throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException {
		FileField fileField = new FileField();
		BeanUtils.copyProperties(fileField, this.f);
		return fileField;
	}

	/**
	 * 根据字符串取得枚举值
	 * 
	 * @param name
	 * @return
	 */
	public static DocEnum getValue(String name) {

		try {
			return valueOf(name);
		} catch (Exception ex) {
			return null;
		}
	}
}
