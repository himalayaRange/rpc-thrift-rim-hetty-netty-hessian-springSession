package com.github.wangyi.thrift.rpc.thrift;

import java.io.Closeable;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.NettyServerConfigBuilder;
import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.NiftyBootstrap;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import com.facebook.nifty.guice.NiftyModule;
import com.github.wangyi.thrift.rpc.constant.SystemConfig;
import com.github.wangyi.thrift.rpc.constant.ThriftProxy;
import com.github.wangyi.thrift.rpc.exception.ThriftException;
import com.github.wangyi.thrift.rpc.monitor.ServiceProxy;
import com.github.wangyi.thrift.rpc.monitor.statistic.MonitorService;
import com.github.wangyi.thrift.rpc.zookeeper.AddressRegister;
import com.github.wangyi.thrift.rpc.zookeeper.IPResolve;
import com.github.wangyi.thrift.rpc.zookeeper.support.LocalNetworkIPResolve;
import com.google.common.base.Objects;
import com.google.inject.Guice;
import com.google.inject.Stage;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-24
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：服务端注册服务工厂
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述: JDK1.7以后实现Closeable 自动关闭IO相关的资源
 * 		ApplicationContextAware：从容器获取系统配置
 */
public class ThriftServiceServerFactory implements InitializingBean,Closeable,ApplicationContextAware{
	
	private static final Logger LOG = LoggerFactory.getLogger(ThriftServiceServerFactory.class);

	private int BOSS_THREAD_DEFAULT_COUNT = SystemConfig.THRIFT_BOSS_THREAD_DEFAULT_COUNT;
	
	private int WORKER_THREAD_DEFAULT_COUNT =SystemConfig.THRIFT_WORKER_THREAD_DEFAULT_COUNT;
	
	private Boolean zooniftyShutdownHook =SystemConfig.THRIFT_ZOONIFTYSHUTDOWNHOOK;
	
	//需要注入的服务名称
	private String name;
	
	//服务IP
	private String hostname = null;
	
	// 服务注册本机端口
	private Integer port = SystemConfig.THRIFT_PORT;
	
	// 优先级权重
	private Integer weight = SystemConfig.THRIFT_WEIGHT;	// default
	
	// 服务实现类
	private Object service;	// serice实现类IMPL
	
	// 服务版本号
	private String version=SystemConfig.THRIFT_VERSION;	//default
	
	// 解析发布服务的IP
	private IPResolve ipResolve;
	
	// zookeeper服务注册
	private AddressRegister addressRegister;
	
	//nifty是Facebook开源的，基于Netty的thrift的实现，使用此包可以快速的开发出基于Netty的高效服务端和客户端的代码
	private ThriftServerDefBuilder thriftServerDefBuilder;
	
	//NettyServer
	private NettyServerTransport server=null;
	
	//Monitor
	private MonitorService monitorService;
	
	//是否开启监控，false时monitorService无效
	private Boolean isMonitor ;
	
	//是否使用netty的定时器算法
	private Boolean isHashedWheelTimer=SystemConfig.isHashedWheelTimer;
	
	//框架配置
	private ThriftProxy proxy;
	
	//发布服务支持的传输格式
	private int protocol;
	
	
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public void setService(Object service) {
		this.service = service;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setIsHashedWheelTimer(Boolean isHashedWheelTimer) {
		this.isHashedWheelTimer = isHashedWheelTimer;
	}

	
	public void setProxy(ThriftProxy proxy) {
		this.proxy = proxy;
	}

	public void setIpResolve(IPResolve ipResolve) {
		this.ipResolve = ipResolve;
	}

	public void setAddressRegister(AddressRegister addressRegister) {
		this.addressRegister = addressRegister;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsMonitor() {
		return isMonitor;
	}

	public void setIsMonitor(Boolean isMonitor) {
		this.isMonitor = isMonitor;
	}

	public MonitorService getMonitorService() {
		return monitorService;
	}

	public void setMonitorService(MonitorService monitorService) {
		this.monitorService = monitorService;
	}

	public Boolean getZooniftyShutdownHook() {
		return zooniftyShutdownHook;
	}

	public void setZooniftyShutdownHook(Boolean zooniftyShutdownHook) {
		this.zooniftyShutdownHook = zooniftyShutdownHook;
	}
	
	@Override
	public void close() throws IOException {
		if(server!=null){
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过Set方法注入Bean后，进行一些资源的初始化，发布服务到zookeeper
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(isMonitor){
			this.monitorService.start();
		}
		if(ipResolve==null){
			ipResolve = new LocalNetworkIPResolve();
		}
		String serverIP =ipResolve.getServerIp();
		if(StringUtils.isEmpty(serverIP)){
			throw new ThriftException("can not find server ip...");
		}
		
		hostname=serverIP+ ":" + port + ":" + weight;
		LOG.info("server start initing....发布服务HostName:"+hostname);
		System.out.println("server start initing....发布服务HostName:"+hostname);
		
		//代理容器选择
		if(StringUtils.isEmpty(proxy) || Objects.equal(1 , proxy.getIocProxy())){
			LOG.info("通过springIOC发布服务...");
			System.out.println("通过springIOC发布服务...");
			springIOC();
		}else if(Objects.equal(2, proxy.getIocProxy())){
			LOG.info("通过Google Guice IOC发布服务...");
			System.out.println("通过Google Guice IOC发布服务...");
			GuiceIOC();
		}
		
		//关机或者重启时候移除zk上服务
		if(zooniftyShutdownHook){
			Runtime.getRuntime().addShutdownHook(new Thread(){
				public void run(){
					try {
						if(addressRegister!=null){
							addressRegister.unregister(name, version, hostname);
							addressRegister.close();
						}
						Thread.sleep(1000 * 5);  
	                	close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 使用SpringIOC进行依赖注入处理，适合大项目
	 * @throws IllegalClassFormatException
	 */
	public void springIOC() throws IllegalClassFormatException{
		Class<? extends Object> serviceClass = service.getClass(); // 获取接口实现类的定义信息，通过反射访问其他信息
		Class<?>[] interfaces = serviceClass.getInterfaces();	// 获取实现类的接口
		if(interfaces.length==0){
			throw new IllegalClassFormatException("service-class should implements Iface");
		}
		
		//通过反射获取处理器
		TProcessor processor=null;
		String serviceName=null;
		for(Class<?> clazz:interfaces){
			String cname = clazz.getSimpleName();//底层类简单名称
			if(!"Iface".equals(cname)){
				continue;
			}
			serviceName =clazz.getEnclosingClass().getName();
			String pname=serviceName+"$Processor"; //获取服务的处理器，由Thrift生成
			try {
				System.out.println("pulish server....>>pname:"+pname);
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				Class<?> pclass = classLoader.loadClass(pname); //加载处理器类
				//判断pclass是否是TProcessor的子类或者接口
				if(!TProcessor.class.isAssignableFrom(pclass)){
					continue;
				}
				Constructor<?> constructor = pclass.getConstructor(clazz);
				//通过构造方法反射创建代理对象
				processor= (TProcessor)constructor.newInstance(new ServiceProxy().wrapper(service, name, version, monitorService, isMonitor));
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(processor==null){
			throw new IllegalClassFormatException("service-class should implements Iface");
		}
		TProtocolFactory factory=null;
		//局部变量配置优先
		if(!StringUtils.isEmpty(this.protocol)){
			factory=mathFactory(this.protocol,serviceName);
		}else{
			if((!StringUtils.isEmpty(this.proxy)) && (!StringUtils.isEmpty(this.proxy.getProtocol()))){
				factory=mathFactory(this.proxy.getProtocol(),serviceName);
			}else{
				factory=mathFactory(1,serviceName);
			}
		}
		
		//设置服务器配置
		thriftServerDefBuilder = new ThriftServerDefBuilder().listen(port).withProcessor(processor).protocol(factory);
		
		//设置服务器管道
		server = new NettyServerTransport(thriftServerDefBuilder.build(), defaultThriftServerConfigBuilder().build(), new DefaultChannelGroup());
		
		//server start
		server.start();
		
		//注册服务
		if(addressRegister!=null){
			addressRegister.register(serviceName, version, hostname);
		}
	}
	
	/**
	 * 匹配传输格式
	 * @param f
	 * @return
	 */
	private TProtocolFactory mathFactory(int f, String serviceName){
		switch (f) {
			case 1:
				LOG.info("通过二进制传输格式发布："+serviceName+"服务！！！");
				System.out.println("通过二进制传输格式发布："+serviceName+"服务！！！");
				return new TBinaryProtocol.Factory();
			case 2:
				LOG.info("通过压缩传输格式发布："+serviceName+"服务！！！");
				System.out.println("通过压缩传输格式发布："+serviceName+"服务！！！");
				return new TCompactProtocol.Factory();
			case 3:
				LOG.info("通过JSON传输格式发布："+serviceName+"服务！！！");
				System.out.println("通过JSON传输格式发布："+serviceName+"服务！！！");
				return new TJSONProtocol.Factory();
			case 4:
				LOG.info("通过SimpleJSON传输格式发布："+serviceName+"服务！！！");
				System.out.println("通过SimpleJSON传输格式发布："+serviceName+"服务！！！");
				return new TSimpleJSONProtocol.Factory();
			default:
				LOG.info("通过default二进制传输格式发布："+serviceName+"服务！！！");
				System.out.println("通过default二进制传输格式发布："+serviceName+"服务！！！");
				return new TBinaryProtocol.Factory();
		}
	}
	
	
	/**
	 * 使用Google Guice进行依赖管理，效率比spring高100倍，适合小项目或者嵌入
	 * @throws IllegalClassFormatException 
	 * TODO 实现有异常
	 */
	public void GuiceIOC() throws IllegalClassFormatException{
		
		  final NiftyBootstrap bootstrap = Guice.createInjector(
			        Stage.PRODUCTION,
			        new NiftyModule() {
			            @Override
			            protected void configureNifty() {
			               
			            	Class<? extends Object> serviceClass = service.getClass(); // 获取接口实现类的定义信息，通过反射访问其他信息
			        		Class<?>[] interfaces = serviceClass.getInterfaces();	// 获取实现类的接口
			        		if(interfaces.length==0){
			        			try {
									throw new IllegalClassFormatException("service-class should implements Iface");
								} catch (IllegalClassFormatException e) {
									e.printStackTrace();
								}
			        		}
			        		
			        		//通过反射获取处理器
			        		TProcessor processor=null;
			        		String serviceName=null;
			            	
			        		
			        		for(Class<?> clazz:interfaces){
			        			String cname = clazz.getSimpleName();//底层类简单名称
			        			if(!"Iface".equals(cname)){
			        				continue;
			        			}
			        			serviceName =clazz.getEnclosingClass().getName();
			        			String pname=serviceName+"$Processor"; //获取服务的处理器，由Thrift生成
			        			try {
			        				System.out.println("pulish server....>>pname:"+pname);
			        				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			        				Class<?> pclass = classLoader.loadClass(pname); //加载处理器类
			        				//判断pclass是否是TProcessor的子类或者接口
			        				if(!TProcessor.class.isAssignableFrom(pclass)){
			        					continue;
			        				}
			        				Constructor<?> constructor = pclass.getConstructor(clazz);
			        				//通过构造方法反射创建代理对象
			        				processor= (TProcessor)constructor.newInstance(new ServiceProxy().wrapper(service, name, version, monitorService, isMonitor));
			        				break;
			        			} catch (Exception e) {
			        				e.printStackTrace();
			        			}
			        		}
			 
			                if(processor==null){
			        			try {
									throw new IllegalClassFormatException("service-class should implements Iface");
								} catch (IllegalClassFormatException e) {
									e.printStackTrace();
								}
			        		}
			                ThriftServerDef serverDef = new ThriftServerDefBuilder().withProcessor(processor)
			                                                                        .build();
			                bind().toInstance(serverDef);
			                
			        		//注册服务
			        		if(addressRegister!=null){
			        			addressRegister.register(serviceName, version, hostname);
			        		}
			                
			            }
			        }).getInstance(NiftyBootstrap.class);
			 
			    bootstrap.start();
	}
	
	/**
	 * nifty服务配置
	 * @return
	 */
	private NettyServerConfigBuilder  defaultThriftServerConfigBuilder() {
		try {
			NettyServerConfigBuilder configBuilder = NettyServerConfig.newBuilder();
			configBuilder.setBossThreadCount(BOSS_THREAD_DEFAULT_COUNT);
			configBuilder.setWorkerThreadCount(WORKER_THREAD_DEFAULT_COUNT);
			//创建固定线程数的线程池
			ExecutorService boss = Executors.newFixedThreadPool(BOSS_THREAD_DEFAULT_COUNT);
			ExecutorService workers = Executors.newFixedThreadPool(WORKER_THREAD_DEFAULT_COUNT);
			configBuilder.setBossThreadExecutor(boss);
			configBuilder.setWorkerThreadExecutor(workers);
			if(isHashedWheelTimer){
				configBuilder.setTimer(new HashedWheelTimer()); //使用netty的定时器算法，效率高，但占用内存高
			}
			return configBuilder;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 从spring容器中获取整体框架配置对象
	 * @param arg0
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		try {
			this.proxy = applicationContext.getBean(ThriftProxy.class); //代理容器
		} catch (NoSuchBeanDefinitionException e) {
			//用户未进行配置，默认使用null
			this.proxy=null;
		}catch(Exception e){
			throw new ThriftException(e);
		}
	}

	
	
}
