package com.github.wangyi.hetty.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * ClassName: NamedThreadFactory  
 * Function: Help for threadpool to set thread name
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 下午4:25:25 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class NamedThreadFactory implements ThreadFactory{

	public static final AtomicInteger poolNumber=new AtomicInteger(1);
	
	final AtomicInteger threadNumber = new AtomicInteger(1);
	
	final ThreadGroup group;
	
	final String namePrefix;
	
	final boolean isDaemon;
	
	public NamedThreadFactory(){
		this("pool");
	}
	
	public NamedThreadFactory(String name) {
		this(name, false);
	}
	
	/**
	 * Example: pool-3-thread-
	 * Creates a new instance of NamedThreadFactory. 
	 * 
	 * @param preffix
	 * @param daemon
	 */
	public NamedThreadFactory(String preffix,boolean daemon){
		SecurityManager s = System.getSecurityManager(); //安全管理器，在Java虚拟机中
		group=(s!=null)?s.getThreadGroup():Thread.currentThread().getThreadGroup();
		namePrefix= preffix + "-" + poolNumber.getAndIncrement() + "-thread-";
		isDaemon=daemon;
	}
	
	
	@Override
	public Thread newThread(Runnable r) {
		Thread t= new Thread(group, r, namePrefix+threadNumber.getAndIncrement(), 0);
		t.setDaemon(isDaemon); //ture表示线程变成后台守护进程
		if(t.getPriority()!=Thread.NORM_PRIORITY){
			//优先级设置：用MIN_PRIORITY来表示优先级最小1；用MAX_PRIORITY来表示优先级最大10；线程的默认优先级为5即NORM_PRIORITY。
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}

}
