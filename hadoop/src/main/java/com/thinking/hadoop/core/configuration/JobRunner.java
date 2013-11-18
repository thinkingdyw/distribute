package com.thinking.hadoop.core.configuration;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapred.JobConf;


/**
 * 运行一个job
 * @author wangjuntao
 */
public class JobRunner {

	public static final String NAMENODE = "fs.default.name";
	public static final String JOBTRACKER = "mapred.job.tracker";
	public static final String QUEUENAME = "mapred.job.queue.name";
	public static final String COMPRESS = "mapred.output.compress";
	public static final String COMPRESS_CODEC = "mapred.output.compression.codec";
	/**
	 * 得到配置对象
	 * @return
	 */
	public static Configuration getJobConf(){
    	Configuration conf = new Configuration();
    	conf.set(NAMENODE, JobConfiguration.get().get("nameNode", ""));
		conf.set(JOBTRACKER, JobConfiguration.get().get("jobTracker", ""));
		conf.set(QUEUENAME, JobConfiguration.get().get("queueName", ""));
		conf.set(COMPRESS, JobConfiguration.get().get("mapredOutputCompress", ""));
		conf.set(COMPRESS_CODEC, JobConfiguration.get().get("mapredOutputCompressionCodec", ""));
		return conf;
	}
	
	public static int getNumReduceTasks(){
		int task;
		try{
			task = Integer.valueOf(JobConfiguration.get().get("numReduceTasks", "5"));
		}catch(Exception e){
			task = 5;
		}
		return task;
	}
	/**
	 * 得到FileSystem对象
	 */
	public static FileSystem getFileSystem(Configuration conf) throws IOException {
		return FileSystem.get(new JobConf(conf));
	}

	/**
	 * 得到FileSystem对象
	 */
	public static FileSystem getFileSystem(Configuration conf, URI uri)
			throws IOException {
		return FileSystem.get(uri, new JobConf(conf));
	}
}
