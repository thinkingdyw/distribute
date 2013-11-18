package com.thinking.hadoop;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.thinking.hadoop.core.io.MREntityWritable;
import com.thinking.hadoop.core.mapreduce.FilterMapper;

public class TestMapRed {

	public static class TestMapper extends FilterMapper<LongWritable, MREntityWritable, Text, MREntityWritable>{

		@Override
		protected void map(LongWritable key, MREntityWritable value,
				Context context) throws IOException, InterruptedException {
			System.out.println("-------------------------------------------->"+value);
			context.write(new Text("map"), value);
		}

		@Override
		public void setKeyOut(MREntityWritable keyOut) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValueOut(MREntityWritable valueOut) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
	public static class TestReducer extends Reducer<Text, MREntityWritable, NullWritable, MREntityWritable>{

		@Override
		protected void reduce(Text key, Iterable<MREntityWritable> value,
				Context context)
				throws IOException, InterruptedException {
			for (MREntityWritable entity : value) {
				context.write(NullWritable.get(), entity);
			}
		}
		
	}
}
