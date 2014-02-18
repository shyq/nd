package com.chain.ens.lucene;

public class FileField {

	private String name;                //字段名
	private String content;             //内容
	private Object fieldsData;          //大数据  
	private boolean isStored;           //是否存储
	private boolean isIndexed;          //是否索引
	private boolean isTokenized;        //  
	private boolean isBinary;           // 
	private boolean isHighLighter;      //是否高亮显示  
	private float boost;                //权重
	private int occurInt;               //用于查询时使用  1:MUST(and)2:MUST_NOT(not) 3:SHOULD(or)

	public FileField() {
	}

	/**
	 * 构造文件字段对象
	 * 
	 * @param name
	 *            字段名
	 * @param content
	 *            内容
	 * @param fieldsData
	 *            日期
	 * @param isStored
	 *            是否保存
	 * @param isIndexed
	 *            是否索引
	 * @param isTokenized
	 * @param isBinary
	 */
	public FileField(String name, String content, Object fieldsData,
			boolean isStored, boolean isIndexed, boolean isTokenized,
			boolean isBinary, float boost) {
		super();
		this.name = name;
		this.content = content;
		this.fieldsData = fieldsData;
		this.isStored = isStored;
		this.isIndexed = isIndexed;
		this.isTokenized = isTokenized;
		this.isBinary = isBinary;
		this.boost = boost;
	}

	/**
	 * 构造文件字段对象
	 * 
	 * @param occurInt
	 *            1:MUST 2:MUST_NOT 3:SHOULD
	 */
	public FileField(String name,
			boolean isStored, boolean isIndexed, float boost,
			boolean isHighLighter, int occurInt) {
		super();
		this.name = name;
		this.isStored = isStored;
		this.isIndexed = isIndexed;
		this.boost = boost;
		this.isHighLighter = isHighLighter;
		this.occurInt = occurInt;
	}
	
	/**
	 * 构造文件字段对象
	 * 
	 * @param occurInt
	 *            1:MUST 2:MUST_NOT 3:SHOULD
	 */
	public FileField(String name, String content, Object fieldsData,
			boolean isStored, boolean isIndexed, float boost,
			boolean isHighLighter, int occurInt) {
		super();
		this.name = name;
		this.content = content;
		this.fieldsData = fieldsData;
		this.isStored = isStored;
		this.isIndexed = isIndexed;
		this.boost = boost;
		this.isHighLighter = isHighLighter;
		this.occurInt = occurInt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getFieldsData() {
		return fieldsData;
	}

	public void setFieldsData(Object fieldsData) {
		this.fieldsData = fieldsData;
	}

	public boolean isStored() {
		return isStored;
	}

	public void setStored(boolean isStored) {
		this.isStored = isStored;
	}

	public boolean isIndexed() {
		return isIndexed;
	}

	public void setIndexed(boolean isIndexed) {
		this.isIndexed = isIndexed;
	}

	public boolean isTokenized() {
		return isTokenized;
	}

	public void setTokenized(boolean isTokenized) {
		this.isTokenized = isTokenized;
	}

	public boolean isBinary() {
		return isBinary;
	}

	public void setBinary(boolean isBinary) {
		this.isBinary = isBinary;
	}

	public int getOccurInt() {
		return occurInt;
	}

	public void setOccurInt(int occurInt) {
		this.occurInt = occurInt;
	}

	boolean hasPosition;

	public boolean isHasPosition() {
		return hasPosition;
	}

	public void setHasPosition(boolean hasPosition) {
		this.hasPosition = hasPosition;
	}

	public boolean isHighLighter() {
		return isHighLighter;
	}

	public void setHighLighter(boolean isHighLighter) {
		this.isHighLighter = isHighLighter;
	}

	public float getBoost() {
		return boost;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}
}
