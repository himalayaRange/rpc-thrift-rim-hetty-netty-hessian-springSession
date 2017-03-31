package com.github.wangyi.thrift.rpc.monitor.statistic.support;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wangyi.thrift.rpc.constant.SystemConfig;
import com.github.wangyi.thrift.rpc.monitor.statistic.MonitorService;

/**
 *
 * ========================================================
 * 日 期：@2016-11-24
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：数据收集器
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class SimpleMonitorService extends MonitorService {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleMonitorService.class);

	//多线程安全阻塞队列，ArrayBlockQueue<基于数组实现>和LinkedBlockQueue<基于链表实现>在处理多线程生产者消费者问题足以
	//若没有设置大小，默认为Integer.MAX_VAL 2147483647
	private final BlockingQueue<Statistic> queue = new LinkedBlockingQueue<Statistic>(SystemConfig.BLOCKINGQUEUE_CAPACITY);
	
	//定时任务执行器
	private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
	
	private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("HHmm");
	
	
	@Override
	public void start() {
		Thread writeThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
                        write(); // 记录统计日志
                    } catch (Throwable t) { // 防御性容错
                        LOG.error("Unexpected error occur at write stat log, cause: " + t.getMessage(), t);
                        try {
                            Thread.sleep(5000); // 失败延迟
                        } catch (Throwable t2) {
                        }
                    }
				}
			}
		});
		//需要在线程启动之前调用，将java的线程设置为后台守护进程,在JVM空闲时处理
		writeThread.setDaemon(true);
		writeThread.setName("SimpleMonitorAsynWriteLogThread");
		writeThread.start();
		
		//定时任务延迟处理线程
		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				try {
					new SimpleVisualService().draw();//绘制图标
				} catch (Throwable t) {
					LOG.error("Unexpected error occur at draw stat chart, cause: " + t.getMessage(), t);
				}
			}
		}, 1, 300, TimeUnit.SECONDS);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void collect(String serviceName, String serviceVersion, Class clazz,
			Method method, Object[] args, int concurrent, long takeTime,
			boolean isError) {
		Statistic entity = new Statistic(serviceName, serviceVersion, method, concurrent, takeTime, isError);
		collect(entity);
	}
	
	/**
	 * 统计信息采集
	 * @param entity
	 */
	private void collect(Statistic entity) {
		//将采集的信息放入等候队列BlockQueue中，不接受null值，等待线程处理 add,offer,put
		if(queue!=null){
			queue.offer(entity);
		}
	}

	/**
	 * 将统计数据写入文件
	 * @throws Exception
	 */
	private synchronized void write()throws Exception{
		// 从等候队列BlockQueue中取出监控数据,如果为空则阻塞等待
		Statistic entity = queue.take();
		String day=sdf1.format(new Date());
		for(String type :TYPES){
			String filename = STATISTICS_DIRECTORY
						+ "/" + day 
	        			+ "/" + entity.getServiceName()
	        			+ "/" + entity.getServiceVersion()
	        			+ "/" + entity.getMethod().getName()
	        			+ "/" + PROVIDER + "." + type;
			File file=new File(filename);
			File dir=file.getParentFile();
			if(dir!=null && !dir.exists()){
				dir.mkdirs();
			}
			//对文件内容进行追加
			FileWriter write = new FileWriter(file, true);
			try {
				if(SUCCESS.equals(type)){
					write.write(sdf2.format(new Date())+""+entity.getTakeTime()+"\n");
				}else if(ERROR.equals(type) && entity.getIsError()){
					write.write(sdf2.format(new Date())+""+1+"\n");
				}else if(CONCURRENT.equals(type)){
					write.write(sdf2.format(new Date()) + " " + entity.getConcurrent() + "\n");
				}
				
			} catch (Exception e) {
			}finally{
				write.close();
			}
		}
	}

}
