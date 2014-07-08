package com.think.hadoop.core.mapreduce.lib.db;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.LongWritable;
import com.think.hadoop.core.conf.MongoDBConfiguration;
import com.think.hadoop.core.io.MongoDBWritable;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDBInputFormat extends InputFormat<LongWritable, MongoDBWritable> implements Configurable {
	
	public static class MongoDBInputSplit extends InputSplit implements Writable {
		private long end = 0;
		private long start = 0;
		
		/**
		 * Default Constructor
		 */
		public MongoDBInputSplit() {
		}
		
		/**
		 * Convenience Constructor
		 * @param start the index of the first row to select
		 * @param end the index of the last row to select
		 */
		public MongoDBInputSplit(long start, long end) {
		  this.start = start;
		  this.end = end;
		}
		
		/** {@inheritDoc} */
		public String[] getLocations() throws IOException {
		  return new String[] {};
		}
		
		/**
		 * @return The index of the first row to select
		 */
		public long getStart() {
		  return start;
		}
		
		/**
		 * @return The index of the last row to select
		 */
		public long getEnd() {
		  return end;
		}
		
		/**
		 * @return The total row count in this split
		 */
		public long getLength() throws IOException {
		  return end - start;
		}
		
		public void readFields(DataInput input) throws IOException {
		  start = input.readLong();
		  end = input.readLong();
		}
		
		public void write(DataOutput output) throws IOException {
		  output.writeLong(start);
		  output.writeLong(end);
		}
	}

	private Mongo mongo;
	private MongoDBConfiguration dbConf;
	protected RecordReader<LongWritable, MongoDBWritable> createRecordReader(
			MongoDBInputSplit split, Configuration conf) throws IOException,
			InterruptedException {
		return new MongoDBRecordReader(split,dbConf);
	}
	@Override
	public RecordReader<LongWritable, MongoDBWritable> createRecordReader(
			InputSplit split, TaskAttemptContext ctx) throws IOException,
			InterruptedException {
		return createRecordReader((MongoDBInputSplit) split, ctx.getConfiguration());
	}

	
	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException,
			InterruptedException {
		//1、获取查询需要抽取的记录条数
		try {
			long count = getCountQuery();
			//2、job需要的map任务数
			int chunks = job.getConfiguration().getInt("mapred.map.tasks", 1);
			//3、计算每个map抽取的数量
			long chunkSize = (count / chunks);
			
			List<InputSplit> splits = new ArrayList<InputSplit>();
			
			for (int i = 0; i < chunks; i++) {
				MongoDBInputSplit split = null;

			    if ((i + 1) == chunks)
			      split = new MongoDBInputSplit(i * chunkSize, count);
			    else
			      split = new MongoDBInputSplit(i * chunkSize, (i * chunkSize)
			          + chunkSize);

			    splits.add(split);
			}
			return splits;
		} catch (Exception e) {
			throw new RuntimeException("计算分片异常!", e);
		}finally{
			try {
				if(null != mongo){
					mongo.close();
				}
			} catch (Exception e) {}
		}
	}
	private long getCountQuery() throws Exception {
		if(null == mongo){
			mongo = dbConf.getMongoConnection();
		}
		DB db = mongo.getDB(dbConf.getDbName());
		db.authenticate(dbConf.getUserName(), dbConf.getPwd());
		DBObject query = dbConf.getQuery();
		if(null == query){
			return db.getCollection(dbConf.getCollectionName()).count();
		}
		return db.getCollection(dbConf.getCollectionName()).count(query);
	}
	@Override
	public Configuration getConf() {
		return dbConf;
	}
	@Override
	public void setConf(Configuration conf) {
		this.dbConf = new MongoDBConfiguration(conf);
	}

}
