package com.thinking.hadoop.core.factory;

import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.Text;

import com.thinking.hadoop.core.User;
import com.thinking.hadoop.core.exception.MREntityNotFoundException;
import com.thinking.hadoop.core.exception.MREntityRecordIsNullException;
import com.thinking.hadoop.core.io.MREntity;

public final class AppConfig {
	
	private static Map<String,Class<? extends MREntity>> pool = new HashMap<String, Class<? extends MREntity>>();
	static{
		pool.put("user",User.class);
	}
	public static MREntity get(Text value)throws NullPointerException,MREntityRecordIsNullException,MREntityNotFoundException, InstantiationException, IllegalAccessException{
		if(null != value){
			final String v = value.toString();
			String[] fields = v.split(MREntity.FIELD_SPLIT_MARK);
			if(fields.length >= MREntity.POSITION){
				final String key = getKey(fields);
				MREntity mrEntity = pool.get(key).newInstance();
				if(null != mrEntity){
					mrEntity.deSerialize(v);
					return mrEntity;
				}else{
					throw new MREntityNotFoundException();
				}
			}else{
				throw new MREntityRecordIsNullException();
			}
		}else{
			throw new NullPointerException();
		}
	}
	private static String getKey(String[] fields) {
		return fields[MREntity.POSITION];
	}
}
