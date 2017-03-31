package com.github.wangyi.thrift.rpc.constant;
/**
 * 
 * ========================================================
 * 日 期：@2016-11-29
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：系统框架配置
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class SystemConfig {

	/**
	 * Thrift默认主线程数
	 */
	public static final int THRIFT_BOSS_THREAD_DEFAULT_COUNT = 2 ;
	
	/**
	 * Thrift默认工作线程数
	 */
	public static final int THRIFT_WORKER_THREAD_DEFAULT_COUNT = 4 ;
	
	/**
	 * 是否使用优雅退出 默认false 如果使用程序请使用kill,不要使用kill -9 
	 */
	public static final Boolean THRIFT_ZOONIFTYSHUTDOWNHOOK = false;
	
	/**
	 * 注册服务默认的端口
	 */
	public static final int THRIFT_PORT = 8299;
	
	/**
	 * 默认权重
	 */
	public static final int THRIFT_WEIGHT = 1;
	
	/**
	 * 默认服务接口版本
	 */
	public static final String THRIFT_VERSION = "1.0.0";
	
	/**
	 * 最大活跃连接数
	 */
	public  static final  Integer THRIFT_CLIENT_MAXACTIVE = 32;
	
	/**
	 *最大空闲数 
	 */
	public  static final  Integer THRIFT_CLIENT_MAXIDLE = 1; 
	
	/**
	 * 最小空闲、数
	 */
	public  static  final  Integer THRIFT_CLIENT_MINIDLE = 0;
	
	/**
	 * 最大等待时间，默认-1l
	 */
	public  static  final  Long	THRIFT_CLIENT_MAXWAITMILLIS = -1l;
	
	/**
	 *  连接空闲时间，默认3分钟，-1：关闭空闲检测
	 */
	public static final Integer THRIFT_CLIENT_IDLETIME = 180000;
	
	/**
	 * 监控日志路径
	 * public static final String STATISTICS_DIRECTORY = "/opt/monitor/statistic";
	 */
	public static final String STATISTICS_DIRECTORY = "D:/monitor/statistic";
	
	/**
	 * 	统计绘图路径
	 * 	public static final String CHARTS_DIRECTORY = "/opt/monitor/chart";
	 */
	public static final String CHARTS_DIRECTORY = "D:/monitor/chart";
	
	/**
	 * 监控统计阻塞的队列的长度
	 */
	public static final int BLOCKINGQUEUE_CAPACITY = 100000 ;
	
	
	/**
	 * zk客户端过期时间 30min
	 */
	public static final int  SESSION_TTMEOUT = 300000;
	
	/**
	 *  zk客户端连接时间
	 */
	public static final int  CONNECTION_TTMEOUT = 30000;
	
	/**
	 * zk客户端最大重连次数
	 */
	public static final int MAX_TETRYPOLIY = 8 ;
	
	/**
	 * zk创建节点默认的方式
	 */
	public static final int DEF_NODEMODEL = 3;
	
	/**
	 * 是否使用Jboss下Netty提供的HashedWheelTimer定时器算法，用以高效处理定时任务，但占用内存高,默认不开启
	 */
	public static final Boolean isHashedWheelTimer = false;
}
