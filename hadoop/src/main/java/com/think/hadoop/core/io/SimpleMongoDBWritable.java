package com.think.hadoop.core.io;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.io.Text;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SimpleMongoDBWritable extends Text implements MongoDBWritable{

	@Override
	public void readDocument(DBCursor cursor,String[] fields,String fieldsSplit) throws Exception {
		DBObject document = cursor.next();
		List<String> fieldList = Arrays.asList(fields);
		StringBuilder builder = new StringBuilder();
		builder.append(cursor.getCollection().getName());
		if(null != null && !fieldList.isEmpty()){
			if(StringUtils.isBlank(fieldsSplit)){
				builder.append(DEFAULT_FIELD_SPLITS);
			}else{
				builder.append(fieldsSplit);
			}
		}
		Iterator<String> itor = fieldList.iterator();
		while(itor.hasNext()){
			String field = itor.next();
			Object value = document.get(field);
			if(null != value){
				if(value instanceof Date){
					builder.append(DateFormatUtils.format((Date)value, "yyyy-MM-dd HH:mm:ss"));
				}else{
					builder.append(String.valueOf(value));
				}
			}else{
				builder.append("");
			}
			if(itor.hasNext()){
				if(StringUtils.isBlank(fieldsSplit)){
					builder.append(DEFAULT_FIELD_SPLITS);
				}else{
					builder.append(fieldsSplit);
				}
			}
		}
		set(builder.toString());
	}

	@Override
	public void readDocument(DBCursor cursor, String[] fields) throws Exception {
		readDocument(cursor, fields, null);
	}
}
