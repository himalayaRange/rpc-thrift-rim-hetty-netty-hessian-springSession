package com.github.wangyi.rmi.customer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.rmi.constant.RConstant;

/**
 * 
 * ========================================================
 * 日 期：@2017-2-6
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：服务消费者
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ServiceConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumer.class);
	
	//用于等待SynConnectioned事件触发后继续执行当前线程
	private CountDownLatch latch = new CountDownLatch(1);
	
	//定义volatile变量，保存最新的RMI地址（变量肯能被其他线程修改，一旦修改要影响到所有线程）
	private volatile List<String> urlList = new ArrayList<String>();

	public ServiceConsumer(){
		 ZooKeeper zk = connectServer(); // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
         if (zk != null) {
            watchNode(zk); // 观察 /registry 节点的所有子节点并更新 urlList 成员变量
         }
	}
	
	//查找RMI服务
	public <T extends Remote> T lookup(){
		T service =null;
		int size=urlList.size();
		if(size>0){
			String url;
			if(size==1){
				url=urlList.get(0);
				LOGGER.debug("using only url:{}",url);
			}else{
				url=urlList.get(ThreadLocalRandom.current().nextInt(size));//从线程中随机的取出一个元素
				LOGGER.debug("using radom url:{}",url);
			}
			service=lookupService(url);
		}
		return service;
	}
	
	//从JNDI中查找服务
	@SuppressWarnings("unchecked")
	public <T extends Remote> T lookupService(String url) {
		T remote =null;
		try {
			remote=(T) Naming.lookup(url);
			
		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			if(e instanceof ConnectException){
				 // 若连接中断，则使用 urlList 中第一个 RMI 地址来查找（这是一种简单的重试方式，确保不会抛出异常）
                LOGGER.error("ConnectException -> url: {}", url);
                if (urlList.size() != 0) {
                    url = urlList.get(0);
                    return lookupService(url);
                }
			}
			LOGGER.error("", e);
		}
		return remote;
	}

	
	// 连接 ZooKeeper 服务器
    public ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(RConstant.ZK_CONNECTION_STRING, RConstant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 唤醒当前正在执行的线程
                    }
                }
            });
            latch.await(); // 使当前线程处于等待状态
        } catch (IOException | InterruptedException e) {
            LOGGER.error("", e);
        }
        return zk;
    }
    
    // 观察/registry/rmi下的所有节点是否有变化
    public  void watchNode(final ZooKeeper zk){
    	try {
			List<String> nodeList=zk.getChildren(RConstant.ZK_REGISTRY_PATH, new Watcher(){
				
				@Override
				public void process(WatchedEvent event) {
					if(event.getType()==Event.EventType.NodeChildrenChanged){ 
						watchNode(zk);//若节点变化，回调获取最新的一份RMI服务树形菜单
					}
				}
			});
			
			List<String> dataList = new ArrayList<String>();
			for(String node:nodeList){
				byte[] data=zk.getData(RConstant.ZK_REGISTRY_PATH+"/"+node, false, null);
				dataList.add(new String(data));
			}
			LOGGER.debug("node data: {}", dataList);
			urlList=dataList;  //更新最新的RMI地址
		} catch (KeeperException | InterruptedException e) {
			LOGGER.error("",e);
		}
    }
    
}
