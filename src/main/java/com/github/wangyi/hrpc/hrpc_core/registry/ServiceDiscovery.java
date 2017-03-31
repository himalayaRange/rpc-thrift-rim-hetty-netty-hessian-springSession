package com.github.wangyi.hrpc.hrpc_core.registry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.manager.ConnectManage;
import com.github.wangyi.hrpc.hrpc_core.utils.Config;

/**
 * 
 * ClassName: ServiceDiscovery  
 * Function: 服务发现
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 下午1:39:59 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ServiceDiscovery {

	  private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

      private CountDownLatch latch = new CountDownLatch(1);
      
      private String address;
      
      private ZooKeeper zookeeper;
      
      //客户端订阅接口
      private List<String> interfaces;
      
      public ServiceDiscovery(String address,List<String> interfaces){
    	  this.address = address;
    	  this.interfaces = interfaces;
    	  zookeeper = connectServer();
    	  if (zookeeper != null) {
              watchNode();
          }
      }
      
      /**
       * 根节点监听
       */
      private Watcher rootWatcher = new Watcher() {
		
		@Override
		public void process(WatchedEvent watchedEvent) {
			logger.info("####"+watchedEvent.getPath()+" Watcher"+" process"+watchedEvent);
			if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
				//事件 ：子节点改变
				watchNode();
			}
		 }
      };
      
      /**
       * 子节点监听
       */
      private  Watcher childdrenWatcher = new Watcher() {
		
		@Override
		public void process(WatchedEvent watchedEvent) {
			logger.info("#######" + watchedEvent.getPath() + " Watcher " + " process: " + watchedEvent);
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                watchNode();
            }
		 }
      };
      
      
      private void watchNode() {
          try {
              logger.info("invoke watchNode() ");
              List<String> interfaceList = zookeeper.getChildren(Config.ZK_ROOT_PATH, rootWatcher); //获取根节点所有孩子节点
              Set<String> dataSet = new HashSet<String>();
              Map<String, Set<InetSocketAddress>> interfaceAndServerMap = new HashMap<String, Set<InetSocketAddress>>();
              for (final String face : interfaceList) {
                  if (interfaces.contains(face)) {
                      List<String> addressList = zookeeper.getChildren(Config.ZK_ROOT_PATH + "/" + face, childdrenWatcher);
                      Set<InetSocketAddress> set = new HashSet<InetSocketAddress>();
                      for (String s : addressList) {
                          dataSet.add(s);
                          String[] array = s.split(":");
                          if (array.length == 2) {
                              String host = array[0];
                              int port = Integer.parseInt(array[1]);
                              set.add(new InetSocketAddress(host, port));
                          }
                      }
                      interfaceAndServerMap.put(face, set);
                  }
              }
              logger.info("node data: {}", dataSet);
              logger.info("interfaceAndServerMap data: {}", interfaceAndServerMap);
              logger.info("Service discovery triggered updating connected server node");
              //更新连接服务
              ConnectManage.getInstance().updateConnectedServer(dataSet, interfaceAndServerMap);
          } catch (Exception e) {
              logger.error("Exception", e);
          }
      }
      
      
      /**
       * 连接zookeeper
       *
       * @return
       */
      private ZooKeeper connectServer() {
          ZooKeeper zk = null;
          try {
              zk = new ZooKeeper(address, Config.ZK_SESSION_TIMEOUT, new Watcher() {
                  @Override
                  public void process(WatchedEvent event) {
                      logger.info("#######default Watcher" + Config.ZK_ROOT_PATH + " watched node process: " + event);
                      if (event.getState() == Event.KeeperState.SyncConnected) {
                          latch.countDown();
                      }
                  }
              });
              latch.await();
          } catch (IOException e) {
              logger.error("Exception", e);
          } catch (InterruptedException ex) {
              logger.error("Exception", ex);
          }
          return zk;
      }

      
      public void stop(){
    	  if(zookeeper != null){
    		  try {
				zookeeper.close();
			} catch (InterruptedException e) {
				 logger.error("", e);
			}
    	  }
      }
      
}
