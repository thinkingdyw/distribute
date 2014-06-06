package com.think.zookeeper.core;

/**
 * Zookeeper lock
 * 
 * @author diaoyouwei
 *
 */
public interface ZookeeperLock {

	/**
	 * @return	name
	 * 
	 * example:
	 * 		lockName:lock-0000000001
	 * return	:	lock-
	 */
	public String getName();
	/**
	 * @return	name
	 * 
	 * example:
	 * 		lockName:lock-0000000001
	 * return	:	lock-0000000001
	 */
	public String getFullName();
	/**
	 * @return	path
	 * 
	 * example:
	 * 		lockPath:/app/locks/lock-0000000001
	 * return	:	/app/locks
	 */
	public String getPath();
	/**
	 * @return	prefix
	 * 
	 * example:
	 * 		lockName:lock-0000000001
	 * 		lockPrefix:lock-000000000
	 * return	:	lock-000000000
	 */
	public String getPrefix();
	/**
	 * @return	suffix
	 * 
	 * example:
	 * 		lockName:lock-0000000001
	 * 		lockSuffix:1
	 * return	:	lock-000000000
	 */
	public long getSuffix();
	/**
	 * @return	splitTag
	 * 
	 * example:
	 * 		lockName:lock-0000000001
	 * 		splitTag:-
	 * return	:	-
	 */
	public String getSplitTag();
	
}
