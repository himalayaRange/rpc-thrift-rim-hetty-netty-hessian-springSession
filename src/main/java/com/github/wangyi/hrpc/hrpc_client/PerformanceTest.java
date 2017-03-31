package com.github.wangyi.hrpc.hrpc_client;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.hrpc.hrpc_api.entity.JD;
import com.github.wangyi.hrpc.hrpc_api.service.JDService;
import com.github.wangyi.hrpc.hrpc_api.service.UserService;
import com.github.wangyi.hrpc.hrpc_core.client.AsyncRPCCallback;
import com.github.wangyi.hrpc.hrpc_core.client.RPCClient;
import com.github.wangyi.hrpc.hrpc_core.proxy.AsyncRPCProxy;

/**
 * 
 * ClassName: PerformanceTest  
 * Function: 性能测试
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-27 下午2:59:57 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:hrpc/spring-hrpc-client.xml")
public class PerformanceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(PerformanceTest.class);
    
	private static byte[] pic;
	
	@Autowired
    private RPCClient rpcClient;

	private int requestNum = 100000;
	private AtomicInteger successNum = new AtomicInteger(0);
	private AtomicInteger failedNum = new AtomicInteger(0);
	
	/**
	 * 同步性能测试
	 */
	@Test
	public void testSyncPerformace(){
		UserService proxy = rpcClient.createProxy(UserService.class);
		long startTime = System.currentTimeMillis();
		final CountDownLatch countDownLatch = new CountDownLatch(requestNum);
		for(int i=1;i<=requestNum;i++){
			try {
				proxy.getUser(String.valueOf(i));
				successNum.incrementAndGet();
				countDownLatch.countDown();
			} catch (Exception e) {
				failedNum.incrementAndGet();
				countDownLatch.countDown();
				continue;
			}
		}
		
	   try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long useTime = System.currentTimeMillis() - startTime;
        logger.info("-------- {} request useTime:{} 毫秒", requestNum, useTime);
        logger.info("-------- {} request successUmn:{}", requestNum, successNum.intValue());
        logger.info("-------- {} request failedNum:{}", requestNum, failedNum.intValue());
	}
	
	
	/**
	 * 异步性能测试
	 */
	@Test
	public void testPerformace(){
		AsyncRPCProxy asyncProxy = rpcClient.createAsyncProxy(UserService.class);
		long startTime = System.currentTimeMillis();
		final CountDownLatch countDownLatch = new CountDownLatch(requestNum);
		for(int i=1;i<=requestNum;i++){
			asyncProxy.call("getUser", new AsyncRPCCallback() {
				
				@Override
				public void success(Object result) {
					successNum.incrementAndGet();
					countDownLatch.countDown();
				}
				
				@Override
				public void fail(Exception e) {
					failedNum.incrementAndGet();
					countDownLatch.countDown();
				}
			}, String.valueOf(i));
		}
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long userTime = System.currentTimeMillis()-startTime;
		logger.info("-------- {} request useTime:{} 毫秒", requestNum, userTime);
        logger.info("-------- {} request successUmn:{}", requestNum, successNum.intValue());
        logger.info("-------- {} request failedNum:{}", requestNum, failedNum.intValue());
	}
	
	
	/**
	 * 同步多线程测试
	 * 耗时：3635ms
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws UnsupportedEncodingException
	 *
	 */
	@Test
	public void testThread() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException{
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<100;i++){
			JD item=new JD();
			item.setStid("88888"+i);
			item.setName("三星盖乐世S7 edge"+i);
			item.setPrice(6888.00);
			item.setCreateTime(new Date());
			sb.append(ObjectsUtil.stringhelper(item));
		}
		pic=(sb.toString()).getBytes("UTF-8");
		
		JDService jdService = rpcClient.createProxy(JDService.class);
		long start = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		threadPool.submit(new MutilThead(jdService));
		threadPool.shutdown(); // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务
		try {
			threadPool.awaitTermination(60, TimeUnit.SECONDS);//请求关闭，发生超时或者当前线程中断，无论哪一个发生都将导致阻塞，直到所有任务完成执行
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//百万次RMI调用耗时：60004ms--16666tps
		System.out.println("times:" + (System.currentTimeMillis() - start));
	 }

	public static void testOrder(JDService order){
		for(int i=0;i<1000;i++){
			try {
				JD req=new JD();
				req.setStid("88888");
				req.setName("三星盖乐世S7 edge");
				req.setPrice(6888.00);
				req.setPicture(pic);
				req.setCreateTime(new Date());
				
				JD insert = order.insert(req);
				//System.err.println(insert.toString());
				
				String delete = order.delete("88888");
				//System.err.println(delete);
				
				JD update = order.update(req);
				//System.err.println(update.toString());
				
				JD query = order.query("88888");
				//System.err.println(query);
				
				JD sysInit = order.sysInit(req, "66666666");
				//System.err.println(sysInit);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class MutilThead extends Thread{
		private JDService order;
		
		public MutilThead(){}
		
		public MutilThead(JDService order) {
			this.order=order;
		}
	
		@Override
		public void run() {
			try {
				 testOrder(order);
			} catch (Exception e) {
				 e.printStackTrace();
			} 
		}
	}
}
