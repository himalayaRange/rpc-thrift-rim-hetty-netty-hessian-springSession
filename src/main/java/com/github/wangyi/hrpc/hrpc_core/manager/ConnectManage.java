package com.github.wangyi.hrpc.hrpc_core.manager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.resource.spi.ConnectionManager;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.client.RPCClientHandler;
import com.github.wangyi.hrpc.hrpc_core.codec.RPCDecoder;
import com.github.wangyi.hrpc.hrpc_core.codec.RPCEncoder;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCRequest;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCResponse;

/**
 * 
 * ClassName: ConnectManager  
 * Function: 管理所有的服务连接
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 下午2:34:12 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ConnectManage {
	private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	private volatile static ConnectManage  connectManage; 
	private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
	
	/*
	 * CopyOnWriteList即写时赋值的容器，即往容器里添加元素时候，不是直接添加，而是先复制一个新的容器，然后添加元素（减少添加次数），然后将原来容器引用指向新的容器
	 * 好处：当前的容器可以进行并发的读，而不需要加锁，因为当前的容器不会添加任何元素
	 */
	private CopyOnWriteArrayList<RPCClientHandler> connectedHandlerList = new CopyOnWriteArrayList<RPCClientHandler>();
	private Map<InetSocketAddress,RPCClientHandler> connectedHandlerMap = new ConcurrentHashMap<InetSocketAddress,RPCClientHandler>();
	private Map<String,SameInterfaceRPCHandlers> interfaceAndHandlersMap =new ConcurrentHashMap<String,SameInterfaceRPCHandlers>();

	private final int reconnnectionTime = 5000; // 重连时间
	private final int connectionTimeOut = 6000;// 连接超时时间
	
	//线程控制
	private CountDownLatch countDownLatch;
	/*
	 *.可重入锁,父类是Lock，在许多线程争同一个线程的时候，ReentrantLock的总体开销通常比synchronize少的多
	 *
	 *.ReentrantLock和Synchronize的扩展性：ReentrantLock拥有Synchronize相同的内存和并发语意，synchronize能做到的ReentrantLock都能
	 * 完成，还拥有其他新的特性，而且在负荷下还拥有这么好的性能，基本可以重写synchronize
	 * 
	 * Lock lock = new  ReentrantLock();
	 * lock.lock();//锁住资源
	 * try{
	 * 	....
	 * }finally{
	 * 	lock.unlock();//释放资源
	 * }
	 */
	private ReentrantLock lock = new ReentrantLock();
	private Condition connected = lock.newCondition();
	
	private long connectionTimeoutMillis = 6000;
	private AtomicInteger roundRobin = new AtomicInteger(0);
	private volatile boolean isRuning = true;
	
	public ConnectManage(){
		
	}
	
	public static ConnectManage getInstance(){
		if(connectManage ==null){
			synchronized (ConnectManage.class) {
				if(connectManage == null){
					connectManage = new ConnectManage();
				}
			}
		}
		return connectManage;
	}
	
	public void updateConnectedServer(Set<String> newServerAddress , Map<String,Set<InetSocketAddress>> interfaceAndServerMap){

        if (newServerAddress != null) {

            //整理出需要连接的服务地址
            Set<InetSocketAddress> newServerNodeSet = new HashSet<InetSocketAddress>();
            for (String newServerAddres : newServerAddress) {
                String[] array = newServerAddres.split(":");
                if (array.length == 2) {
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    InetSocketAddress socketAddress = new InetSocketAddress(host, port);
                    newServerNodeSet.add(socketAddress);
                }
            }

            //删除无效的服务
            for (int i = 0; i < connectedHandlerList.size(); i++) {
                RPCClientHandler handler = connectedHandlerList.get(i);
                InetSocketAddress socketAddress = handler.getSocketAddress();
                if (!newServerNodeSet.contains(socketAddress)) {
                    logger.info("remove and close invalid server node: " + socketAddress);
                    handler.close();
                    connectedHandlerList.remove(handler);
                    connectedHandlerMap.remove(socketAddress);
                }
            }

            //若发现新的未创建连接的服务，则去创建连接
            int needToConnectNum = 0;
            for (InetSocketAddress serverNodeAddress : newServerNodeSet) {
                RPCClientHandler handler = connectedHandlerMap.get(serverNodeAddress);
                if (handler == null) {
                    needToConnectNum++;
                }
            }
            if (needToConnectNum > 0) {
                countDownLatch = new CountDownLatch(needToConnectNum);
                for (InetSocketAddress serverNodeAddress : newServerNodeSet) {
                    RPCClientHandler handler = connectedHandlerMap.get(serverNodeAddress);
                    if (handler == null) {
                        connectServerNode(serverNodeAddress);
                    }
                }
            }
            try {
                if(countDownLatch!=null){
                    countDownLatch.await(connectionTimeoutMillis, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //更新 interfaceAndHandlersMap
            for (String key : interfaceAndServerMap.keySet()) {
                SameInterfaceRPCHandlers handlers = new SameInterfaceRPCHandlers();
                Set<InetSocketAddress> set = interfaceAndServerMap.get(key);
                for (InetSocketAddress inetSocketAddress : set) {
                    RPCClientHandler handler = connectedHandlerMap.get(inetSocketAddress);
                    if (handler != null) {
                        handlers.addHandler(handler);
                    }
                }
                interfaceAndHandlersMap.put(key, handlers);
            }

            logger.info("current connectedHandlerList: {}", connectedHandlerList);
            logger.info("current connectedHandlerMap: {}", connectedHandlerMap);
            logger.info("current interfaceAndHandlersMap: {}", interfaceAndHandlersMap);


        } else {
            logger.error("no available server node. all server nodes are down !!!");
            for (RPCClientHandler handler : connectedHandlerList) {
                logger.info("remove invalid server node: " + handler.getSocketAddress());
                handler.close();//关闭和服务器的连接
            }
            connectedHandlerList.clear();
            connectedHandlerMap.clear();
            interfaceAndHandlersMap.clear();
            logger.error("connectedHandlerList connectedHandlerMap interfaceAndHandlersMap has bean cleared!!!");
        }
	}
			
	/**
     * 创建各个服务的连接（基于netty）
     *
     * @param remote
     */
    private void connectServerNode(final InetSocketAddress remote) {
        logger.info("start connect to remote server: {}", remote);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeOut)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline cp = socketChannel.pipeline();
                        cp.addLast(new RPCEncoder(RPCRequest.class));
                        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                        cp.addLast(new RPCDecoder(RPCResponse.class));
                        cp.addLast(new RPCClientHandler() {
                            @Override
                            public void handlerCallback(RPCClientHandler handler, boolean isActive) {
                                if (isActive) {
                                    logger.info("Active: " + handler.getSocketAddress());
                                    connectedHandlerList.add(handler);
                                    connectedHandlerMap.put(handler.getSocketAddress(), handler);
                                    countDownLatch.countDown();
                                } else {
                                    logger.error("Inactive: " + handler.getSocketAddress());
                                }
                            }
                        });
                    }
                });
        bootstrap.connect(remote).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    logger.info("success connect to remote server: {}", remote);
                } else {
                    //不停的重连
                    logger.info("failed connect to remote server: {} will reconnect {} millseconds later", remote, reconnnectionTime);
                    channelFuture.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            connectServerNode(remote);
                        }
                    }, reconnnectionTime, TimeUnit.MILLISECONDS);
                }
            }
        });
    }

    public RPCClientHandler chooseHandler(String face) {
    	SameInterfaceRPCHandlers handlers = interfaceAndHandlersMap.get(face);
        if (handlers != null) {
            return handlers.getSLBHandler();
        } else {
            return null;
        }

    }

    public void stop() {
        isRuning = false;
        for (int i = 0; i < connectedHandlerList.size(); ++i) {
            RPCClientHandler handler = connectedHandlerList.get(i);
            handler.close();
        }
        eventLoopGroup.shutdownGracefully();
    }
}
