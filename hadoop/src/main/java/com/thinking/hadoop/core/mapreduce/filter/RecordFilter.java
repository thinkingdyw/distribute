package com.thinking.hadoop.core.mapreduce.filter;

public interface RecordFilter <T>{

	public boolean doFilter(T t);
}
