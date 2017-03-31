package com.github.wangyi.thrift.thread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-30
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：ArrayBlockingQueue和LinkedBlockingQueue
 * TODO FIFO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 * ArrayBlockingQueue:读写操作需要锁住整个容器，吞吐量与一般的相似，适合"生产者和消费者"模式
 * LinkedBlockingQueue:生产者和消费者采用独立的锁来进行数据同步，能够高效的处理并发数据，并行操作数据提高队列的并发性能
 */
public class BlockQueueTest {

	// 容量为5的数据阻塞队列
	// private static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5,true);
	
	// 容量为5的链表阻塞队列
    private static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(7);
	
	private static CountDownLatch producerLatch;   // 并发环境下生产者倒计时计数器
	
	private static CountDownLatch consumerLatch; // 并发环境下消费者计数器
	
	public static void main(String[] args) {
		BlockQueueTest bqt = new BlockQueueTest();
		bqt.test();
	}
	
	public void test(){
		producerLatch =	new CountDownLatch(10);
		consumerLatch = new CountDownLatch(10);
		Thread t1 = new Thread(new ProducerTask());
		Thread t2 = new Thread(new ConsumerTask());
		
		t1.setDaemon(true);
		t2.setDaemon(true);
		t1.start();
		t2.start();
		
		try {
			System.out.println("producer waiting ....");
			producerLatch.await();//进入等待状态，直到state值为0，再继续往下执行  
			System.out.println("producer end ....");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("consumer waiting....");  
            consumerLatch.await(); //进入等待状态，直到state值为0，再继续往下执行  
            System.out.println("consumer end ....");  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		t1.interrupt();
		t2.interrupt();
		
		System.out.println("thread end");
	}
	
	//生产者
	class ProducerTask  implements Runnable{
		private Random rdm =new Random();
		@Override
		public void run() {
			try {
				while(true){
					queue.put(rdm.nextInt(100)); //queue满的话线程会阻塞，直到有空间继续
					
					//queue.offer(rdm.nextInt(100)); //添加失败直接返回false
					//queue.offer(rdm.nextInt(100), 1, TimeUnit.SECONDS); //等待1s,添加失败返回false
					
					producerLatch.countDown(); //state减1
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	//消费者
	class ConsumerTask implements Runnable{

		@Override
		public void run() {
			try {
				while(true){
					
					Integer value = queue.take(); //如果为空，则一直阻塞直到有新的数据加入
					
					//Integer value = queue.poll(); //非阻塞，放弃获取
					//Integer value = queue.poll(1, TimeUnit.SECONDS); //等候1s还没获取到数据则返回失败，放弃获取
					
					System.out.println("value="+value);
					consumerLatch.countDown();//state值减1
					TimeUnit.SECONDS.sleep(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
