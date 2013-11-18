package com.thinking.hadoop.core.configuration;

import org.apache.hadoop.conf.Configuration;

/**
 * 
 * @author diaoyouwei
 *
 */
public class JobConfiguration {

	private static JobConfiguration CONFIGURATION;
	private static Object locker = new Object();;
	private Configuration conf;
	{
		conf = new Configuration();
		conf.addResource("site-config.xml");
	}

	public String get(String name, String defaultValue) {
		return conf.get(name, defaultValue);
	}

	public void refresh() {
		conf.clear();
		conf = null;
		CONFIGURATION=null;
	}

	public static JobConfiguration get() {
		if (CONFIGURATION == null) {
			synchronized (locker) {
				CONFIGURATION = new JobConfiguration();
			}
		}
		return CONFIGURATION;
	}
}
