package com.think.hadoop.core.conf;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;
import com.think.hadoop.core.io.MongoDBWritable;

public final class MongoDBConfiguration extends Configuration{

	public static final String MONGO_DB_NAME = "mongo.db.name";
	public static final String MONGO_DB_COLLECTION = "mongo.db.colletion.name";
	public static final String MONGO_DB_USERNAME = "mongo.db.user.name";
	public static final String MONGO_DB_PASSWARD = "mongo.db.password";
	public static final String MONGO_DB_QUERY_FIELDS = "mongo.db.query.fields";
	public static final String MONGO_DB_QUERY_FIELDS_SPLIT = "mongo.db.query.fields.split";
	public static final String MONGO_DB_QUERY_JSON = "mongo.db.query.json";
	public static final String MONGO_DB_REPLICA_SET = "mongo.db.replica.set";
	
	
	public MongoDBConfiguration(){}
	public MongoDBConfiguration(Configuration configuration){
		super(configuration);
	}
	/**
	 * @return 数据库连接
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 */
	public Mongo getMongoConnection() throws Exception {
		if(StringUtils.isBlank(get(MongoDBConfiguration.MONGO_DB_REPLICA_SET))){
			throw new MongoException("请指定MongoDB host配置");
		}
		String[] replicaSet = get(MongoDBConfiguration.MONGO_DB_REPLICA_SET).split(",");
		List<ServerAddress> replicaSetSeeds = new ArrayList<ServerAddress>();
		for (String host : replicaSet) {
			String[] hostPort = host.split(":");
			replicaSetSeeds.add(new ServerAddress(hostPort[0], Integer.valueOf(hostPort[1])));
		}
		return new Mongo(replicaSetSeeds);
	}
	/**
	 * @return 数据库名
	 */
	public String getDbName() {
		if(StringUtils.isBlank(get(MongoDBConfiguration.MONGO_DB_NAME))){
			throw new MongoException("请指定MongoDB数据库名称");
		}
		return get(MongoDBConfiguration.MONGO_DB_NAME);
	}

	/**
	 * @return 集合名称
	 */
	public String getCollectionName() {
		if(StringUtils.isBlank(get(MongoDBConfiguration.MONGO_DB_COLLECTION))){
			throw new MongoException("请指定数据库集合名称");
		}
		return get(MongoDBConfiguration.MONGO_DB_COLLECTION);
	}

	/**
	 * 
	 * @return 查询条件
	 * @throws IOException
	 */
	public DBObject getQuery() throws IOException{
		String queryJson = get(MongoDBConfiguration.MONGO_DB_QUERY_JSON);
		if(StringUtils.isBlank(queryJson)){
			return null;
		}
		return (DBObject) JSON.parse(queryJson);
	}

	/**
	 * 
	 * @return 数据库用户名
	 * @throws IOException
	 */
	public String getUserName()throws IOException {
		if(StringUtils.isNotBlank(get(MONGO_DB_USERNAME))){
			return get(MONGO_DB_USERNAME);
		}else{
			return "";
		}
	}

	/**
	 * @return 数据库密码
	 */
	public char[] getPwd() {
		if(StringUtils.isNotBlank(get(MONGO_DB_PASSWARD))){
			return get(MONGO_DB_PASSWARD).toCharArray();
		}else{
			return new char[0];
		}
	}
	/**
	 * @return 查询字段
	 */
	public String[] getQueryFields(){
		String[] fields = null;
		String fieldz = get(MONGO_DB_QUERY_FIELDS);
		if(StringUtils.isNotBlank(fieldz)){
			fields = fieldz.split(",");
		}
		return fields;
	}
	/**
	 * @return 字段分隔符
	 */
	public String getFieldsSplit() {
		String split = get(MONGO_DB_QUERY_FIELDS_SPLIT,MongoDBWritable.DEFAULT_FIELD_SPLITS);
		if(StringUtils.isBlank(split)){
			split = MongoDBWritable.DEFAULT_FIELD_SPLITS;
		}
		return split;
	}
}
