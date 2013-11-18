package com.thinking.hadoop.core.io;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import com.thinking.hadoop.core.factory.AppConfig;

public class MREntityRecordReader extends RecordReader<LongWritable, MREntityWritable>{
	private LineRecordReader lineRecordReader;
	
	public MREntityRecordReader(InputSplit split, TaskAttemptContext context)
		throws IOException, InterruptedException {
		lineRecordReader = new LineRecordReader();
		initialize(split, context);
	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		lineRecordReader.initialize(split, context);
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		return lineRecordReader.nextKeyValue();
	}

	@Override
	public LongWritable getCurrentKey() throws IOException,
			InterruptedException {
		return lineRecordReader.getCurrentKey();
	}

	@Override
	public MREntityWritable getCurrentValue() throws IOException,
			InterruptedException {
		Text value = lineRecordReader.getCurrentValue();
		MREntityWritable entityWritable = null;
		if(null != value){
			MREntity entity = null;
			try {
				entity = AppConfig.get(value);
				entityWritable = new MREntityWritable(entity);
				return entityWritable;
			} catch (Exception e) {
				throw new IOException("实例化MREntity发生异常!");
			} 
		}else{
			throw new InterruptedException("输入记录为空!");
		}
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return lineRecordReader.getProgress();
	}

	@Override
	public void close() throws IOException {
		lineRecordReader.close();
	}

}
