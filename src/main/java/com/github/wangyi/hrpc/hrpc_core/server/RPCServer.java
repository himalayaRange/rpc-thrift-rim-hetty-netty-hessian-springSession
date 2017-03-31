package com.github.wangyi.hrpc.hrpc_core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.github.wangyi.hrpc.hrpc_core.annotation.HRPCService;
import com.github.wangyi.hrpc.hrpc_core.codec.RPCDecoder;
import com.github.wangyi.hrpc.hrpc_core.codec.RPCEncoder;
import com.github.wangyi.hrpc.hrpc_core.constant.HConstant;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCRequest;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCResponse;
import com.github.wangyi.hrpc.hrpc_core.registry.ServiceRegistry;

/**
 * 
 * ClassName: RPCServer  
 * Function: RPC Server
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 下午4:28:24 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class RPCServer implements BeanNameAware,BeanFactoryAware,ApplicationContextAware,InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(RPCServer.class);
	
	private ServiceRegistry serviceRegistry;
	
	//服务地址，具体路径
	private String serverAddress;
	
	//多线程下存放服务名和服务对象之间的关系
	private Map<String,Object> serviceBeanMap = new ConcurrentHashMap<String,Object>();
	
	//线程池处理，提供性能
	private static ExecutorService threadPoolExecutor;
	
	public RPCServer(String serverAddress,String zookeeper){
		this.serverAddress=serverAddress;
		this.serviceRegistry=new ServiceRegistry(zookeeper);
	}
	
	
	/**
	 * 让Bean获取自己在BeanFactory配置中的名称（根据情况是ID或者Name）
	 */
	@Override
	public void setBeanName(String name) {
		logger.info("setBeanName() {}" , name);
	}
	
	/**
	 * 让Bean获取配置他们的BeanFactory的作用
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		 logger.info("setBeanFactory()");		
	}
	
	/**
	 * 该类能获取ApplicationContext所有引用到的Bean
	 * 此处是扫描发布服务的注解
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		logger.info("setApplicationContext...");
		// 扫描所有@RPCService的注解类
		Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(HRPCService.class);
		if(MapUtils.isNotEmpty(serviceBeanMap)){
			for(Object serviceBean : serviceBeanMap.values()){
				HRPCService annotation = serviceBean.getClass().getAnnotation(HRPCService.class);
				String interfaceName = annotation.value().getName();
				logger.info("@HRPCSevice:"+interfaceName);
				// 在Zookepper上注册接口服务
				serviceRegistry.createInterfaceAddressNode(interfaceName, serverAddress);
				// 本地保存接口服务
				this.serviceBeanMap.put(interfaceName, serviceBean);
			}
		}
	}
	
	/**
	 * 实例被创建时候执行，后续即是init-method
	 * 作用：创建Netty服务端
	 * 服务端添加childHandler目的是添加Handler，监听连接的客户端的Channel的动作和状态
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("afterPropertiesSet()");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0))
                                    .addLast(new RPCDecoder(RPCRequest.class))
                                    .addLast(new RPCEncoder(RPCResponse.class))
                                    .addLast(new RPCServerHandler(serviceBeanMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}

	/**
	 * 线程池执行任务
	 * @param task
	 *
	 */
	public static  void submit(Runnable task){
		if(threadPoolExecutor == null){
			synchronized (RPCServer.class) {
				if(threadPoolExecutor == null){
					threadPoolExecutor = Executors.newFixedThreadPool(HConstant.EXECUTORS_THREAD);
				}
			}
		}
		threadPoolExecutor.submit(task);
	}

}
