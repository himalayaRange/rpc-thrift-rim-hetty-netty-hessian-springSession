package com.github.wangyi.hrpc.hrpc_core.registry;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.utils.Config;


/**
 * 
 * ClassName: ServiceRegistry  
 * Function: RPC服务注册中心
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 上午11:44:31 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ServiceRegistry {

	private static final Logger logger =  LoggerFactory.getLogger(ServiceRegistry.class);

	private CountDownLatch latch = new CountDownLatch(1);
	
	private String address;
	
	private ZooKeeper zookeeper;
	
	public ServiceRegistry(String address){
		this.address = address;
		
		//连接Zookepper服务
		zookeeper = connectServer();
		//创建服务根节点
		if(zookeeper!=null){
			setRootNode();
		}
		
	}
	
	/**
	 * 连接服务
	 * @return
	 *
	 */
	private ZooKeeper connectServer(){
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(address, Config.ZK_SESSION_TIMEOUT, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					if(event.getState() == Event.KeeperState.SyncConnected){
						latch.countDown();
					}
				}
			});
			latch.await();
		} catch (IOException e) {
            logger.error("", e);
        } catch (InterruptedException ex) {
            logger.error("", ex);
        }
        return zk;
	}
	
	/**
	 * 
	 * 设置服务根节点
	 */
	private void setRootNode(){
		try {
			Stat exists = zookeeper.exists(Config.ZK_ROOT_PATH, false);
			if(exists == null){
				// 此处创建持久化节点，这个目录节点存储的数据不会丢失
				String path = zookeeper.create(Config.ZK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				logger.info("create zookeeper root node (path:{})", path);
			}
		} catch (KeeperException e) {
			 logger.error(e.toString());
		} catch (InterruptedException  e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * 创建服务接口节点
	 * <br>
	 * 路径：根节点/接口名称
	 * @param interfaceName
	 *
	 */
	private void createInterfaceNode(String interfaceName){
		 try {
	            String path = zookeeper.create(Config.ZK_ROOT_PATH + "/" + interfaceName, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	            logger.info("create zookeeper interface node (path:{})", path);
	        } catch (KeeperException e) {
	            logger.error("", e);
	        } catch (InterruptedException ex) {
	            logger.error("", ex);
	        }
	}
	
	
	 /**
     * 创建服务接口地址节点
     * PERSISTENT：创建后只要不删就永久存在
     * EPHEMERAL：会话结束年结点自动被删除，EPHEMERAL结点不允许有子节点
     * SEQUENTIAL：节点名末尾会自动追加一个10位数的单调递增的序号，同一个节点的所有子节点序号是单调递增的
     * PERSISTENT_SEQUENTIAL：结合PERSISTENT和SEQUENTIAL
     * EPHEMERAL_SEQUENTIAL：结合EPHEMERAL和SEQUENTIAL
     *
     * @param interfaceName
     * @param serverAddress
     */
    public void createInterfaceAddressNode(String interfaceName, String serverAddress) {
        try {
            Stat s = zookeeper.exists(Config.ZK_ROOT_PATH + "/" + interfaceName, false);
            if (s == null) {
                createInterfaceNode(interfaceName);
            }
            String path = zookeeper.create(Config.ZK_ROOT_PATH + "/" + interfaceName + "/" + serverAddress,
                    serverAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("create zookeeper interface address node (path:{})", path);
        } catch (KeeperException e) {
            logger.error("", e);
        } catch (InterruptedException ex) {
            logger.error("", ex);
        }
    }

}
