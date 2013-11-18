package com.thinking.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import com.thinking.hadoop.core.configuration.JobRunner;
import com.thinking.hadoop.core.io.MREntityInputFormat;
import com.thinking.hadoop.core.io.MREntityOutputFormat;
import com.thinking.hadoop.core.io.MREntityWritable;

public class TestRunner implements Tool{

	public void setConf(Configuration conf) {}

	public Configuration getConf() {
		Configuration conf = JobRunner.getJobConf();
		return conf;
	}

	public int run(String[] args) throws Exception {
		Configuration config = getConf();
		Job job = new Job(config);
		
		job.setJarByClass(TestRunner.class);
		job.setJobName("test");
		job.setMapperClass(TestMapRed.TestMapper.class);
		job.setReducerClass(TestMapRed.TestReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MREntityWritable.class);
		job.setNumReduceTasks(2);
		job.setOutputFormatClass(MREntityOutputFormat.class);
		job.setInputFormatClass(MREntityInputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(MREntityWritable.class);
		Path inPath = new Path("test/input");
		FileInputFormat.setInputPaths(job, inPath);
		Path outPath = new Path("test/output");
		FileOutputFormat.setOutputPath(job, outPath);
		//如果输出目录存在，删除输出目录
		FileSystem fs = JobRunner.getFileSystem(config, outPath.toUri());
		if (fs.exists(outPath)) {
			fs.delete(outPath, true);
		}
		job.submit();
		final boolean rs = job.waitForCompletion(false);
		return rs?1:0;
	}

	public static void main(String[] args) {
		try {
			new TestRunner().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
