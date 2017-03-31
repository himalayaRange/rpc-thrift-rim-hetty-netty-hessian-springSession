package com.github.wangyi.rmi.provider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
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
 * 类说明：RMI 服务提供者，发布服务并将服务注册到zookepper中（实际存放在zNode上）
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ServerProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerProvider.class);
	
	//用于等待SynConnectioned事件触发后继续执行当前线程
	private CountDownLatch latch = new CountDownLatch(1);
	
	//发布并注册到zookepper
	public void publish(Remote remote,String host,int port){
		String 	url=publishService(remote, host, port);
		if(url!=null){
			ZooKeeper zk = connectServer();
			if(zk!=null){
				createNode(zk, url);//创建节点并将RMI地址放在ZNode上
			}
		}
	}
	
	
	//发布RMI服务
	public String publishService(Remote remote, String host, int port){
		String url = null;
		try {
			url=String.format("rmi://%s:%d/%s", host,port,remote.getClass().getName()); //使用全路径名称作为唯一的服务名称
			LocateRegistry.createRegistry(port);
			Naming.rebind(url, remote);
			LOGGER.debug("publish rmi service (url:{})",url);
		} catch (RemoteException | MalformedURLException e) {
			LOGGER.error("",e);
		}
		return url;
	}
	
	//连接zookepper服务器
	public ZooKeeper connectServer(){
		ZooKeeper zk=null;
		try {
			zk=new ZooKeeper(RConstant.ZK_CONNECTION_STRING, RConstant.ZK_SESSION_TIMEOUT, new Watcher(){

				@Override
				public void process(WatchedEvent event) {
					if(event.getState()==Event.KeeperState.SyncConnected){
						latch.countDown(); //唤醒正在执行的线程
					}
				}
				
			});
			latch.await();
		} catch (IOException | InterruptedException e) {
			 LOGGER.error("", e);
		}
		return zk;
	}
	
	
	//创建ZNode节点
	public void createNode(ZooKeeper zk,String url){
		try {
			byte[] data = url.getBytes();
			// 创建一个临时性且有序的 ZNode，且对所有的zk连接者开放
			String path = zk.create(RConstant.ZK_PROVIDER_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); //短暂的（ephemeral）和持久的（persistent
			
			LOGGER.debug("create zookeeper node ({}=>{})",path,url);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			LOGGER.error("",e);
		}
	}
	
	
}
