package com.thinking.hadoop.core.io;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class MREntityInputFormat extends
		FileInputFormat<LongWritable, MREntityWritable> {

	@Override
	public RecordReader<LongWritable, MREntityWritable> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new MREntityRecordReader(split, context);
	}

	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		CompressionCodec codec = new CompressionCodecFactory(
				context.getConfiguration()).getCodec(file);
		return codec == null;
	}
}
