public class WordCount{
	public static void main(String[] args)throws Exception{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "word count");
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit((job.waitForCompletion(true)) ? 0 : 1);
	}

	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		private IntWritable result;
		
		public IntSumReducer(){
			this.result = new IntWritable();
		}
		
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
			throws IOException, InterruptedException{
			int sum = 0;
			for (Iterator i$ = values.iterator(); i$.hasNext(); ) { 
				IntWritable val = (IntWritable)i$.next();
				sum += val.get();
			}
			this.result.set(sum);
			context.write(key, this.result);
		}
	}

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{
		private static final IntWritable one = new IntWritable(1);
		private Text word;
		
		public TokenizerMapper(){
			this.word = new Text();
		}
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				this.word.set(itr.nextToken());
				context.write(this.word, one);
			}
		}
	}
	public List<InputSplit> getSplits(JobContext job
                                    ) throws IOException {
               	minSize = Math.max(getFormatMinSplitSize(), getMinSplitSize(job));
    		long maxSize = getMaxSplitSize(job);
    		// generate splits
    		List<InputSplit> splits = new ArrayList<InputSplit>();
    		for (FileStatus file: listStatus(job)) {
		        Path path = file.getPath();
		        FileSystem fs = path.getFileSystem(job.getConfiguration());
		        long length = file.getLen();
			BlockLocation[] blkLocations = fs.getFileBlockLocations(file, 0, length);
			if ((length != 0) && isSplitable(job, path)) { 
			        long blockSize = file.getBlockSize();
			        long splitSize = computeSplitSize(blockSize, minSize, maxSize);

        			long bytesRemaining = length;
        			while (((double) bytesRemaining)/splitSize > SPLIT_SLOP) {
          				int blkIndex = getBlockIndex(blkLocations, length-bytesRemaining);
          				splits.add(new FileSplit(path, length-bytesRemaining, splitSize, 
                                   	blkLocations[blkIndex].getHosts()));
          				bytesRemaining -= splitSize;
        			}
        
        			if (bytesRemaining != 0) {
          				splits.add(new FileSplit(path, length-bytesRemaining, bytesRemaining, 
                     			blkLocations[blkLocations.length-1].getHosts()));
        		}		
	      		} else if (length != 0) {
	        		splits.add(new FileSplit(path, 0, length, blkLocations[0].getHosts()));
	      		} else { 
        			//Create empty hosts array for zero length files
        			splits.add(new FileSplit(path, 0, length, new String[0]));
      			}
    		}
    		LOG.debug("Total # of splits: " + splits.size());
    		return splits;
  	}

}
