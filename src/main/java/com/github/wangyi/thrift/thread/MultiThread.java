package com.github.wangyi.thrift.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-28
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：多线中线程池的Test
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class MultiThread {

	/**
	 * 固定大小线程池
	 * 对于固定线程数的的线程池，当需要加入池的线程数超过最大尺寸时，进入此线程池需要排队等待
	 */
	@Test
	public void fixed(){
		long start= System.currentTimeMillis();
		//1创建一个可重用固定线程数的线程池
		ExecutorService pool = Executors.newFixedThreadPool(2);
		//2.创建单任务线程池
		//ExecutorService pool = Executors.newSingleThreadExecutor();
		for(int i=0;i<=100000;i++){
			Thread t = new MyThread();
			pool.execute(t);
		}
		//close线程池
		pool.shutdown();
		long end=System.currentTimeMillis();
		System.out.println("execute time : "+(end-start)+"ms");
	}
	
	
	/**
	 * 可变尺寸的线程池
	 */
	@Test
	public void cachedPool(){
		long start= System.currentTimeMillis();
		ExecutorService pool = Executors.newCachedThreadPool();
		for(int i=0;i<100000;i++){
			Thread t = new MyThread();
			pool.execute(t);
		}
		pool.shutdown();
		long end=System.currentTimeMillis();
		System.out.println("execute time : "+(end-start)+"ms");
	}
		
	/**
	 * 延迟连接池
	 */
	@Test
	public void scheduledPool(){
		//PoolSize为2的多线程延迟连接池
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
		//单任务延迟连接池
		//ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
		Thread t1=new MyThread();
		Thread t2=new MyThread();
		Thread t3=new MyThread();
		Thread t4=new MyThread();
		Thread t5=new MyThread();
		//Add pool执行
		pool.execute(t1);
		pool.execute(t2);
		pool.execute(t3);
		//延迟风格执行
		pool.schedule(t4, 5,  TimeUnit.MILLISECONDS);
		pool.schedule(t5, 5,  TimeUnit.MILLISECONDS);
	
		pool.shutdown();
	}
	
	/**
	 * 自定义线程池:Exetors底层就是ThreadPoolExector的实现
	 * 参数： 
	　　corePoolSize - 池中所保存的线程数，包括空闲线程。 
	　　maximumPoolSize - 池中允许的最大线程数。 
	　　keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。 
	　　unit - keepAliveTime 参数的时间单位。 
	　　workQueue - 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。 
	   handler： 线程池对拒绝任务的处理策略（四中策略）
	　　抛出： 
	　　IllegalArgumentException - 如果 corePoolSize 或 keepAliveTime 小于零，或者 maximumPoolSize 小于或等于零，或者 corePoolSize 大于 maximumPoolSize。 
	　　NullPointerException - 如果 workQueue 为 null
	      如果线程数少于corePoolSize，则任务不会被添加到队列中，直接执行Thread方法，
	      如果线程数>=corePoolSize,都会将任务放入队列中，如果无法请求添加队列，则会创建新的线程，除非超过最大maximumPoolSize,任务会被拒绝
	 */
	@Test
	public void defiendPool(){
		final AtomicInteger concurrent = new AtomicInteger();
		// 创建无界的线程安全的阻塞队列
	    BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10000);
	    // 当线程池中的线程数量大于corePoolSize,如果某个线程空闲时间超过keepAlive，任务拒绝，执行拒绝策略
	    //RejectedExecutionHandler  handler = new ThreadPoolExecutor.AbortPolicy(); //策略1：抛出RejectedExecutionException
	     RejectedExecutionHandler handler  = new ThreadPoolExecutor.CallerRunsPolicy(); //策略2：回调execute方法执行被拒绝的任务，相当于执行慢些
	    // RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy(); //策略3：丢弃拒绝的任务（丢弃队列中最旧的任务）
	    // RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy(); //策略4：默认情况下它将丢弃被拒绝的任务。（丢弃将要入队列的任务）
	    ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 5, 10, TimeUnit.SECONDS, queue,handler);
	     for(int i=0;i<=500;i++){
	    	pool.execute(new Runnable() {
				@Override
				public void run() {
					int curr=concurrent.getAndIncrement();
					System.out.println(Thread.currentThread().getName()+"循环数："+curr+"is running");
				}
			});
	    }
	    pool.shutdown();
	}
	
	/**
	 * 测试初入队列的方法
	 */
	@Test
	public void testBlockQueue(){
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);
		
		for(int i=1;i<=11;i++){
			try {
				
				//1.add队列满了直接排除异常
				//queue.add("add :"+i);
				
				//2.put队列满了一直阻塞等待时间
				//queue.put("put :"+i);
				
				//3.offer队列满了的话返回false
				boolean success;
				if(i==5){
					success=queue.offer("");
				}else{
					success=queue.offer("offer :"+i);
				}
				if(success){
					System.out.println(i+"入队列完成....");
				}else{
					System.out.println(i+"入队列失败，队列已满....");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//取队列
		while(!queue.isEmpty()){
			//1.poll队列若为空，返回null
			//String poll = queue.poll();
			
			//2.remove若队列为空，抛出NoSuchElementException异常
			//String poll=queue.remove();
			
			//3.take若队列为空，则阻塞等待不为空的队列
			String poll = null;
			try {
				poll = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("****取队列："+poll);
			
		}
	}
}

