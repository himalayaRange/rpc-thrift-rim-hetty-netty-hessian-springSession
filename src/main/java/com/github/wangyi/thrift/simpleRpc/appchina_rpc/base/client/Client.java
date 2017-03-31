package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client;

import java.io.Closeable;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：客户端标准接口
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface Client<P,R> extends Closeable {

	/**
	 * 打开连接
	 * @throws ClientIOException 
	 */
	public  void open() throws ClientIOException;
	
	
	/**
	 * 连接成功后，记录时间戳
	 * @return
	 */
	public long openTimestamp();
	
	/**
	 * 测试可用性
	 * @return
	 */
	public boolean ping();
	
	
	/**
	 * 发送请求数据到服务端，并获取服务端的返回数据
	 * @param param
	 * @return
	 * @throws ClientIOException  网络异常
	 * @throws RemoteException    服务端异常
	 */
	public R send(P param)throws ClientIOException,RemoteException;
	
	
	/**
	 * 调用{@link #send(Object)}发送请求的次数
	 *  @return 从连接被打开，到当前时间调用send方法的次数。
	 */
	public long sendCount();
	
	
	/**
	 * extends Closeable
	 * 关闭连接资源
	 */
	@Override
	public void close();

	
	/**
	 * 连接是否关闭
	 * @return
	 */
	public boolean closed();
}
