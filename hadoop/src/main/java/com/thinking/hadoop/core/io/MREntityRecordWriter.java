package com.thinking.hadoop.core.io;

import java.io.IOException;

import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class MREntityRecordWriter<K,V> extends RecordWriter<K, V> {
	private RecordWriter<K, V> recordWriter;
	
	public MREntityRecordWriter(RecordWriter<K, V> recordWriter){
		this.recordWriter = recordWriter;
	}
	@Override
	public void write(K key, V value) throws IOException, InterruptedException {
		recordWriter.write(key, value);
	}

	@Override
	public void close(TaskAttemptContext context) throws IOException,
			InterruptedException {
		recordWriter.close(context);
	}

}
