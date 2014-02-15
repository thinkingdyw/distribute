public class MultiOutput {

	public static class MultiOutputMapper extends Mapper<LongWritable, Text, LongWritable, Text>{
  @Override
  protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
  //业务代码
			context.write(key, value);
		}
	}
	public static class MultiOutputReducer extends Reducer<LongWritable, Text, NullWritable, Text>{
		
		@Override
		protected void reduce(LongWritable key, Iterable<Text> value,Context context)
				throws IOException, InterruptedException {
   //业务代码
		}
	}
}
