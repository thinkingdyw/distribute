package com.thinking.hadoop.core.exception;

public class MREntityNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "未找到匹配的MREntity对象!";
	}
	
}
