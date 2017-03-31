package com.github.wangyi.thrift.rpc.test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.wangyi.thrift.rpc.service.Date;
import com.github.wangyi.thrift.rpc.service.HelloWorldService;
import com.github.wangyi.thrift.rpc.service.Order;


/**
 * 
 * ========================================================
 * 日 期：@2016-11-28
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：并发测试
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */

public class Client {

	public static void main(String[] args) {
		spring();
		//System.out.println(Integer.MAX_VALUE);
	}
	
	/**
	 * 并发测试
	 */
	@SuppressWarnings("resource")
	public static void spring() {
		CountDownLatch countDownLatch = new CountDownLatch(1000);//并发倒计时
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("spring-test-applicationContext.xml");
			Thread.sleep(100);
			long start=System.currentTimeMillis();
			HelloWorldService.Iface helloSerivce = (HelloWorldService.Iface) context.getBean("helloSerivce");
			ExecutorService pool = Executors.newFixedThreadPool(4);
			for (int i = 1; i <=100; i++) {
				//无返回值pool.execute(new TThread(helloSerivce,i) );
				//pool.submit(...)可以返回是否成功标志
				Future<?> future = pool.submit(new TThread(helloSerivce,i,countDownLatch));
				  try {
			            if(future.get()==null){//如果Future's get返回null，任务完成
			                System.out.println("taskID:"+i+"任务完成。。。");
			            }
			        } catch (InterruptedException e) {
			        } catch (ExecutionException e) {
			            //否则我们可以看看任务失败的原因是什么
			            System.out.println("taskID:"+i+"case:"+e.getCause().getMessage());
			        }
			}
			countDownLatch.await(); //等候状态，state=0后续执行
			pool.shutdown();
			long end=System.currentTimeMillis();
			System.err.println("耗时："+(end-start)+"ms"); //4236ms 3000ms 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class TThread extends Thread {
		HelloWorldService.Iface helloSerivce;
		int taskId;
		
		CountDownLatch countDownLatch;
		
		TThread(HelloWorldService.Iface service,int taskId,CountDownLatch countDownLatch) {
			this.helloSerivce = service;
			this.taskId=taskId;
			this.countDownLatch=countDownLatch;
		}
		

		public void run() {
			
			try {
				for (int i = 1; i <= 100; i++) {
					Order order=new Order();
					order.setOrderNo(UUID.randomUUID().toString());
					order.setOrderName("iphone8 plus");
					order.setOrderPrice(8888L);
					order.setCreateTime(new Date(2016,12,1));
					System.err.println("taskID:"+taskId+","+Thread.currentThread().getName()+" "+(i+1)+" "+helloSerivce.sayHello("汪谊"));
					System.err.println("taskID:"+taskId+","+Thread.currentThread().getName()+" "+(i+1)+" "+helloSerivce.sendOrder(order));
					countDownLatch.countDown();//减1
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}