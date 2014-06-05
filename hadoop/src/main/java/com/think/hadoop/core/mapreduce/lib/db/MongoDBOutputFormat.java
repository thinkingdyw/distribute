package com.think.hadoop.core.mapreduce.lib.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import com.think.hadoop.core.conf.MongoDBConfiguration;
import com.think.hadoop.core.io.MongoDBWritable;

public class MongoDBOutputFormat extends OutputFormat<NullWritable, MongoDBWritable>{
	@Override
	public void checkOutputSpecs(JobContext context) throws IOException,
			InterruptedException {}

	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return new FileOutputCommitter(FileOutputFormat.getOutputPath(context),
                context);
	}

	@Override
	public RecordWriter<NullWritable, MongoDBWritable> getRecordWriter(
			TaskAttemptContext context) throws IOException, InterruptedException {
		return new MongoDBRecordWriter(context.getConfiguration());
	}

	public class MongoDBRecordWriter extends RecordWriter<NullWritable, MongoDBWritable>{

		private Logger logger = Logger.getLogger(getClass());
		private Mongo mongo;
		private DB db;
		private DBCollection collection;
		private MongoDBConfiguration dbConf;
		private List<DBObject> dbObjects = new ArrayList<DBObject>();

		public MongoDBRecordWriter(Configuration conf){
			dbConf = new MongoDBConfiguration(conf);
		}
		@Override
		public void close(TaskAttemptContext ctx) throws IOException,
				InterruptedException {
			if(null != mongo){
				flush();
				mongo.close();
			}
		}
		private void flush() {
			logger.debug("flush.....");
			collection.insert(dbObjects);
			dbObjects.clear();
		}
		@Override
		public void write(NullWritable key, MongoDBWritable value)
				throws IOException, InterruptedException {
			try {
				if(null == mongo){
					logger.debug("get new mongo connection...");
					mongo = dbConf.getMongoConnection();
					if(null == db){
						db = mongo.getDB(dbConf.getDbName());
						db.authenticate(dbConf.getUserName(), dbConf.getPwd());
						collection = db.getCollection(dbConf.getCollectionName());
					}
				}
				DBObject dbObject = (DBObject) JSON.parse(value.toString());
				dbObjects.add(dbObject);
				if(dbObjects.size() == 1000){
					flush();
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	} 
}
