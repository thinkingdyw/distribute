package com.think.zookeeper.demo.dislock;

import org.apache.log4j.Logger;

/**
 * 实际售票场景中的：售票终端，即售票窗口，代表一个售票员
 * @author diaoyouwei
 *
 */
public class TicketTerminal implements Runnable{

	private TicketZKClientProxy zkClient;
	private String terminalName;
	private int retryCount;
	private int retryDelay;
	
	private Logger LOG = Logger.getLogger(getClass());
	
	public TicketTerminal(String terminalName, int retryCount, int retryDelay) {
		this.terminalName = terminalName;
		this.retryCount = retryCount;
		this.retryDelay = retryDelay;
	}

	@Override
	public void run() {
		try {
			ticketSell();
		} catch (Exception e) {
			LOG.error(terminalName+":售票失败!",e);
		}
	}
	
	public void init() throws Exception{
		zkClient = new TicketZKClientProxy(terminalName,retryCount,retryDelay);
		zkClient.lock();
	}
	public void ticketSell() throws Exception{
		init();
		doTicketSell();
		destroy();
	}
	@SuppressWarnings("static-access")
	private void doTicketSell() throws InterruptedException {
		LOG.info(terminalName+": 正在售票........");
		Thread.currentThread().sleep(1000);
		LOG.info(terminalName+": 售票完毕!");
		
	}
	public void destroy() throws Exception{
		zkClient.unlock();
	}
}
