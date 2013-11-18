package com.thinking.hadoop.core.exception;

public class MREntityRecordIsNullException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "输入记录为空字符串!";
	}

	
}
