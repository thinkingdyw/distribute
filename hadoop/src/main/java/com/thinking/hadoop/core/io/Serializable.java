package com.thinking.hadoop.core.io;


public interface Serializable<T extends MREntity> {
	/**
	 * 默认字段分隔标记：tab
	 */
	public static final String FIELD_SPLIT_MARK = "\t";
	public static final String NULL_EMPTY = "NULL";
	public static final int POSITION = 0;
	public static final String SETTER_PREFIX = "set";
	public static final String GETTER_PREFIX = "get";
	public String serialize();
	public void deSerialize(String record);
}
