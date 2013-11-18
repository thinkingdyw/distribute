package com.thinking.hadoop.core.mapreduce;

import java.io.IOException;
import org.apache.hadoop.mapreduce.Mapper;
import com.thinking.hadoop.core.mapreduce.filter.RecordFilter;

public abstract class FilterMapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> implements RecordFilter<VALUEIN>{

	private KEYOUT keyOut = null;
	private VALUEOUT valueOut = null;
	@Override
	protected void map(KEYIN key, VALUEIN value,Context context)
			throws IOException, InterruptedException {
		final boolean isValid = doFilter(value);
		setKeyOut(value);
		setValueOut(value);
		if(isValid){
			context.write(this.keyOut, this.valueOut);
		}
	}
	public abstract void setKeyOut(VALUEIN keyOut);
	public abstract void setValueOut(VALUEIN valueOut);
	public boolean doFilter(VALUEIN t) {
		return true;
	}
}
