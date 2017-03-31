package com.github.wangyi.hetty.test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.BeansException;
import com.caucho.hessian.client.HessianProxyFactory;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api.AddService;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api.MoodService;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api.OrderService;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Order;
/**
 * 
 * ClassName: ThreadTest  
 * Function: execute time 7174ms 6362ms
 * 			 Hessian序列化大的数据包时候效率低
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-14 下午5:05:13 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ThreadTest {
	private static byte[] pic;

	public static void main(String[] args) throws BeansException, Exception {
		String url = "http://localhost:8088/apis/orderServices";
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setUser("hettyAdmin");
		factory.setPassword("123456");
		factory.setDebug(true);
		factory.setOverloadEnabled(true);
		final OrderService basic = (OrderService) factory.create(
				OrderService.class, url);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			Order item = new Order();
			item.setStid("88888" + i);
			item.setName("三星盖乐世S7 edge" + i);
			item.setPrice(6888.00);
			item.setCreateTime(new Date());
			sb.append(ObjectsUtil.stringhelper(item));
		}
		pic = (sb.toString()).getBytes("UTF-8");

		OrderService orderService = basic;
		long start = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		threadPool.submit(new MutilThead(null, null, null, orderService));
		threadPool.shutdown(); // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务
		try {
			threadPool.awaitTermination(60, TimeUnit.SECONDS);// 请求关闭，发生超时或者当前线程中断，无论哪一个发生都将导致阻塞，直到所有任务完成执行
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 百万次RMI调用耗时：60004ms--16666tps
		System.out.println("times:" + (System.currentTimeMillis() - start));
	}

	public static void testOrder(OrderService order) {
		for (int i = 0; i < 1000; i++) {
			try {
				Order req = new Order();
				req.setStid("88888");
				req.setName("三星盖乐世S7 edge");
				req.setPrice(6888.00);
				req.setPicture(pic);
				req.setCreateTime(new Date());

				Order insert = order.insert(req);
				// System.err.println(insert.toString());

				String delete = order.delete("88888");
				// System.err.println(delete);

				Order update = order.update(req);
				// System.err.println(update.toString());

				Order query = order.query("88888");
				// System.err.println(query);

				Order sysInit = order.sysInit(req, "66666666");
				// System.err.println(sysInit);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class MutilThead extends Thread {
		private AddService as;
		private AddService os;
		private MoodService ms;
		private OrderService order;

		public MutilThead() {

		}

		public MutilThead(AddService as, AddService os, MoodService ms,
				OrderService order) {
			this.as = as;
			this.os = os;
			this.ms = ms;
			this.order = order;
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
