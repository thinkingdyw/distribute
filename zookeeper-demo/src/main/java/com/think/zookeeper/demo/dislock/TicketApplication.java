package com.think.zookeeper.demo.dislock;

/**
 * 模拟火车站售票
 * 该案例只是测试分布式锁用例，存在的不足：由于是同步操作，则整改购票过程类似于在窗口排队的过程，如果由于某种原因，某个售票窗口阻塞，那么整个售票业务将会暂停
 * 
 * @author diaoyouwei
 *
 */
public class TicketApplication {

	public static void main(String[] args) {
		
		/**
		 * 假设：以下售票窗口都售同一种火车票,会发现，每个窗口都是有序售票
		 */
		
		TicketTerminal terminal_1 = new TicketTerminal("1号售票口", 3, 3000);
		new Thread(terminal_1).start();
		
		TicketTerminal terminal_2 = new TicketTerminal("2号售票口", 3, 3000);
		new Thread(terminal_2).start();
		
		TicketTerminal terminal_3 = new TicketTerminal("3号售票口", 3, 3000);
		new Thread(terminal_3).start();
		
		TicketTerminal terminal_4 = new TicketTerminal("4号售票口", 3, 3000);
		new Thread(terminal_4).start();
		
		TicketTerminal terminal_5 = new TicketTerminal("5号售票口", 3, 3000);
		new Thread(terminal_5).start();
		
		TicketTerminal terminal_6 = new TicketTerminal("6号售票口", 3, 3000);
		new Thread(terminal_6).start();
		
		TicketTerminal terminal_7 = new TicketTerminal("7号售票口", 3, 3000);
		new Thread(terminal_7).start();
	}
}
