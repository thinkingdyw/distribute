package com.think.hadoop.core.io;

import org.apache.hadoop.io.Writable;

import com.mongodb.DBCursor;

public interface MongoDBWritable extends Writable{

	/**
	 * 默认字段分隔符
	 */
	public final static String DEFAULT_FIELD_SPLITS = "\t";
	
	public void readDocument(DBCursor cursor,String[] fields) throws Exception;
	
	public void readDocument(DBCursor cursor,String[] fields,String fieldsSplit) throws Exception;
}
