package com.think.hadoop.core.mapreduce.lib.db;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.think.hadoop.core.conf.MongoDBConfiguration;
import com.think.hadoop.core.io.MongoDBWritable;
import com.think.hadoop.core.io.SimpleMongoDBWritable;
import com.think.hadoop.core.mapreduce.lib.db.MongoDBInputFormat.MongoDBInputSplit;

public class MongoDBRecordReader extends RecordReader<LongWritable, MongoDBWritable>{

	private MongoDBInputFormat.MongoDBInputSplit split;
	private MongoDBConfiguration dbConf;
	private Mongo mongo;
	private DB db;
	private DBCursor results;
	private long pos;
	private LongWritable key;
	private MongoDBWritable value;
	
	
	public MongoDBRecordReader(MongoDBInputSplit split,
			MongoDBConfiguration dbConf) {
		super();
		this.split = split;
		this.dbConf = dbConf;
	}

	@Override
	public void close() throws IOException {
		if(null != mongo){
			mongo.close();
		}
	}

	@Override
	public LongWritable getCurrentKey() throws IOException,
			InterruptedException {
		return key;
	}

	@Override
	public MongoDBWritable getCurrentValue() throws IOException,
			InterruptedException {
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return 0;
	}

	@Override
	public void initialize(InputSplit arg0, TaskAttemptContext arg1)
			throws IOException, InterruptedException {
		
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		try {
	      if (key == null) {
	        key = new LongWritable();
	      }
	      if (value == null) {
	        value = createValue();
	      }
	      if (null == this.results) {
	        this.results = executeQuery(dbConf.getQuery());
	      }
	      if (!results.hasNext())
	        return false;

	      key.set(pos + split.getStart());

	      value.readDocument(results,dbConf.getQueryFields(),dbConf.getFieldsSplit());

	      pos ++;
	    } catch (Exception e) {
	    	throw new IOException(e.getMessage());
	    }
	    return true;
	}
	private DBCursor executeQuery(DBObject query)throws Exception {
		if(mongo == null){
			mongo = dbConf.getMongoConnection();
		}
		if(db == null){
			db = mongo.getDB(dbConf.getDbName());
			db.authenticate(dbConf.getUserName(), dbConf.getPwd());
		}
		if(null == query){
			return db.getCollection(dbConf.getCollectionName()).find().skip((int)split.getStart()).limit((int)(split.getEnd()-split.getStart()));
		}
		return db.getCollection(dbConf.getCollectionName()).find(query).skip((int)split.getStart()).limit((int)(split.getEnd()-split.getStart()));
	}

	private MongoDBWritable createValue() {
		return new SimpleMongoDBWritable();
	}
}
