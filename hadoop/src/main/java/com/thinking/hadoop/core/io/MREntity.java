package com.thinking.hadoop.core.io;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MREntity implements Serializable<MREntity>{

	private String alias;
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String serialize() {
		List<Field> fields = getFields();
		StringBuilder builder = new StringBuilder();
		for (Field field : fields) {
			initValue(builder, field);
		}
		
		return builder.toString().trim();
	}

	private void initValue(StringBuilder builder, Field field) {
		field.setAccessible(true);
		try {
			builder.append(field.get(this)).append(FIELD_SPLIT_MARK);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Field> getFields() {
		List<Field> fields = new ArrayList<Field>();
		LinkedList<Class<? extends MREntity>> superClazzs = new LinkedList<Class<? extends MREntity>>();
		Class superClazz = this.getClass().getSuperclass();
		while(null !=superClazz){
			superClazzs.add(superClazz);
			superClazz = superClazz.getSuperclass();
		}
		int superCount = superClazzs.size();
		for (int i = 0; i < superCount;) {
			Class<? extends MREntity> clazz = superClazzs.removeLast();
			Field[] parentFields = clazz.getDeclaredFields();
			if(null != parentFields && parentFields.length > 0){
				for (Field pf : parentFields) {
					fields.add(pf);
				}
			}
			superCount = superClazzs.size();
		}
		Field[] selfFields = this.getClass().getDeclaredFields();
		for (Field sf : selfFields) {
			fields.add(sf);
		}
		return fields;
	}

	public void deSerialize(String record) {
		List<Field> fields = getFields();
		String[] fieldsValue = record.split(FIELD_SPLIT_MARK);
		final int length = fieldsValue.length;
		for (int i = 0; i < length; i++) {
			Field field = fields.get(i);
			try {
				setValue(fieldsValue[i],field);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private void setValue(String fieldValue,Field field)
			throws IllegalAccessException {
		field.setAccessible(true);
		Type dataType = field.getGenericType();
		final boolean isNull = NULL_EMPTY.equalsIgnoreCase(fieldValue);
		if(dataType == int.class || dataType == Integer.class){
			if(isNull){
				field.set(this, null);
			}else{
				field.setInt(this,Integer.valueOf(fieldValue));
			}
		}else if(dataType == short.class || dataType == Short.class){
			if(isNull){
				field.set(this, null);
			}else{
				field.setShort(this,Short.valueOf(fieldValue));
			}
		}else if(dataType == long.class || dataType == Long.class){
			if(isNull){
				field.set(this, null);
			}else{
				field.setLong(this,Long.valueOf(fieldValue));
			}
		}else if(dataType == float.class || dataType == Float.class){
			if(isNull){
				field.set(this, null);
			}else{
				field.setFloat(this,Float.valueOf(fieldValue));
			}
		}else if(dataType == double.class || dataType == Double.class){
			if(isNull){
				field.set(this, null);
			}else{
				field.setDouble(this,Double.valueOf(fieldValue));
			}
		}else if(dataType == String.class){
			field.set(this, fieldValue);
		}
	}

	@Override
	public String toString() {
		return this.serialize();
	}
	
	
}
