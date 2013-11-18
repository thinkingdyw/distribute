package com.thinking.hadoop.core.io;

import java.io.IOException;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MREntityOutputFormat<K, V> extends FileOutputFormat<K, V>{
	private TextOutputFormat<K,V> textOutputFormat;

	public MREntityOutputFormat(){
		textOutputFormat=new TextOutputFormat<K, V>();
	}

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		return new MREntityRecordWriter<K, V>(textOutputFormat.getRecordWriter(job));
	}
	
}
