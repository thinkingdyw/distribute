package com.think.zookeeper.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ZookeeperConfiguration {

	private static String connectString;
	private static int sessionTimeout;
	
	private static final String DEFAULT_CONFIG_FILE = "zk-config.properties";
	{
		try {
			initZkConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getConnectString() {
		return connectString;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}
	
	public void initZkConfig() throws IOException{
		ClassLoader classLoader = getClassLoader();
		
		InputStream in = classLoader.getResourceAsStream(DEFAULT_CONFIG_FILE);
		if(null != in){
			Properties zkConfig = new Properties();
			zkConfig.load(in);
			connectString = zkConfig.getProperty(ZkConfigurationConstant.CONNECT_STRING_KEY);
			sessionTimeout = Integer.valueOf(zkConfig.getProperty(ZkConfigurationConstant.SESSION_TIME_OUT_KEY));
		}else{
			throw new RuntimeException("没有找到配置文件："+DEFAULT_CONFIG_FILE);
		}
	}
	/**
	 * 获得类加载器
	 **/
	private ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(null == classLoader){
			classLoader = getClass().getClassLoader();
		}
		return classLoader;
	}
	
	
}
