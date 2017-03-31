package com.github.wangyi.hetty.core;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.github.wangyi.hetty.conf.HettyConfig;
import com.github.wangyi.hetty.core.ssl.SslHettyChannelPipelineFactory;
import com.github.wangyi.hetty.object.Application;
import com.github.wangyi.hetty.object.HettyException;
import com.github.wangyi.hetty.plugin.IPlugin;
import com.github.wangyi.hetty.util.FileUtil;


/**
 * 
 * ClassName: Hetty  
 * Function: Hetty终端
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-14 下午1:37:22 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class Hetty {

	private static Logger logger = LoggerFactory.getLogger(Hetty.class);
	
	private ServerBootstrap httpBootstrap =null;
	private ServerBootstrap httpsBootstrap = null;
	private HettyConfig hettyConfig=HettyConfig.getInstance();
	private int httpListenPort;
	private int httpsListenPort;

	public Hetty(){
		HettyConfig.getInstance().loadPropertyFile("server.properties");// load default file 
	}
	
	public Hetty(String file){
		HettyConfig.getInstance().loadPropertyFile(file); // load defined file 
	}
	
	private void init(){
		initServerInfo();
		initHettySecurity();
		initPlugins();
		initServiceMetaData();
		if (httpListenPort == -1 && httpsListenPort == -1) {
			httpListenPort = 8081;//default port is 8081
		}
		if (httpListenPort != -1) {
			initHttpBootstrap();
		}
		if (httpsListenPort != -1) {
			initHttpsBootstrap();
		}
	}
	
	
	/**
	 * init http bootstrap
	 */
    private void initHttpBootstrap(){
    	logger.info("init HTTP Bootstrap...........");
		ThreadFactory serverBossTF = new NamedThreadFactory("HETTY-BOSS-");
		ThreadFactory serverWorkerTF = new NamedThreadFactory("HETTY-WORKER-");
		httpBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(serverBossTF), //主线程，负责连接处理
				Executors.newCachedThreadPool(serverWorkerTF))); //工作线程，服务每个连接的业务处理
		httpBootstrap.setOption("tcpNoDelay", Boolean.parseBoolean(hettyConfig
				.getProperty("hetty.tcp.nodelay", "true")));
		httpBootstrap.setOption("reuseAddress", Boolean.parseBoolean(hettyConfig
				.getProperty("hetty.tcp.reuseaddress", "true")));
		
		int coreSize = hettyConfig.getServerCorePoolSize();
		int maxSize = hettyConfig.getServerMaximumPoolSize();
		int keepAlive = hettyConfig.getServerKeepAliveTime();
		ThreadFactory threadFactory = new NamedThreadFactory("hetty-");
		ExecutorService threadPool = new ThreadPoolExecutor(coreSize, maxSize, keepAlive,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
		httpBootstrap.setPipelineFactory(new HettyChannelPipelineFactory(threadPool));
		
		if (!checkPortConfig(httpListenPort)) {
			throw new IllegalStateException("port: " + httpListenPort + " already in use!");
		}
		httpBootstrap.bind(new InetSocketAddress(httpListenPort));
    }
    
    
    /**
	 * init https bootstrap
	 */
    private void initHttpsBootstrap(){
    	if(!checkHttpsConfig()){
    		return;
    	}
    	logger.info("init HTTPS Bootstrap...........");
		ThreadFactory serverBossTF = new NamedThreadFactory("HETTY-BOSS-");
		ThreadFactory serverWorkerTF = new NamedThreadFactory("HETTY-WORKER-");
		httpsBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(serverBossTF),
				Executors.newCachedThreadPool(serverWorkerTF)));
		httpsBootstrap.setOption("tcpNoDelay", Boolean.parseBoolean(hettyConfig
				.getProperty("hetty.tcp.nodelay", "true")));
		httpsBootstrap.setOption("reuseAddress", Boolean.parseBoolean(hettyConfig
				.getProperty("hetty.tcp.reuseaddress", "true")));
		
		int coreSize = hettyConfig.getServerCorePoolSize();
		int maxSize = hettyConfig.getServerMaximumPoolSize();
		int keepAlive = hettyConfig.getServerKeepAliveTime();
		ThreadFactory threadFactory = new NamedThreadFactory("hetty-");
		ExecutorService threadPool = new ThreadPoolExecutor(coreSize, maxSize, keepAlive,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
		httpsBootstrap.setPipelineFactory(new SslHettyChannelPipelineFactory(threadPool));
		
		if (!checkPortConfig(httpsListenPort)) {
			throw new IllegalStateException("port: " + httpsListenPort + " already in use!");
		}
		httpsBootstrap.bind(new InetSocketAddress(httpsListenPort));
    }

	
	private void initServerInfo(){
		httpListenPort=hettyConfig.getHttpPort();
		httpsListenPort=hettyConfig.getHttpsPort();
	}
	
	public void initPlugins(){
		logger.info("init plugins.......");
		List<Class<?>> pluginList = hettyConfig.getPluginClassList();
		try {
			for(Class<?> cls:pluginList){
				IPlugin p;
				p = (IPlugin) cls.newInstance();
				p.start();
			}
		} catch (InstantiationException e) {
			logger.error("init plugin failed.");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("init plugin failed.");
			e.printStackTrace();
		}
	}
	
	
	public void initServiceMetaData(){
		logger.info("init service MetaData...........");
    	MetadataProcessor.initMetaDataMap();
	}

	public void initHettySecurity(){
		logger.info("init hetty security ..........");
		Application app=new Application(hettyConfig.getServerKey(), hettyConfig.getServerSecret());
		HettySecurity.addToApplicationMap(app);
	}
	
	public boolean checkPortConfig(int listenPort){
		if(listenPort<0 || listenPort>65536){
			throw new IllegalArgumentException("Invalid start port: "
					+ listenPort);
		}
		ServerSocket ss=null;
		DatagramSocket ds=null;
		try {
			ss = new ServerSocket(listenPort);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(listenPort);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {

		}finally {
			if (ds != null) {
				ds.close();
			}
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					// should not be thrown, just detect port available.
				}
			}
		}
		return false;
	}
	
	
	private boolean  checkHttpsConfig(){
		if(!StringUtils.isEmpty(hettyConfig.getKeyStorePath())){
			if(!FileUtil.getFile(hettyConfig.getKeyStorePath()).exists()){
				throw new HettyException("we can't find the file which you configure:[ssl.keystore.file]");
			}
		}else if(!StringUtils.isEmpty(hettyConfig.getCertificateKeyFile()) && 
				!StringUtils.isEmpty(hettyConfig.getCertificateFile())){
			if(!FileUtil.getFile(hettyConfig.getCertificateKeyFile()).exists()){
				throw new HettyException("we can't find the file which you configure:[ssl.certificate.key.file]");
			}
			if(!FileUtil.getFile(hettyConfig.getCertificateFile()).exists()){
				throw new HettyException("we can't find the file which you configure:[ssl.certificate.file]");
			}
		}else{
			throw new HettyException("please check your ssl's config.");
		}
		return true;
	}
	
	public void serverLog(){
		logger.info("devMod:"+hettyConfig.getDevMod());
		logger.info("server key:"+hettyConfig.getServerKey());
		logger.info("server secret:"+hettyConfig.getServerSecret());
		if(httpListenPort != -1){
			logger.info("Server started,listening for HTTP on port "+httpListenPort);
		}
		if(httpsListenPort != -1){
			logger.info("Server started,listening for HTTPS on port "+httpsListenPort);
		}
	}
	
	/**
	 * start hetty
	 */
	public void start(){
		init();
		serverLog();
	}
	
	public void stop(){
		logger.info("Server stop!");
		if(httpsBootstrap!=null){
			httpBootstrap.releaseExternalResources();
		}
		if(httpsBootstrap != null){
			httpsBootstrap.releaseExternalResources();
		}
	}
	
	public static void main(String[] args) {
		new Hetty().start();
	}
}
