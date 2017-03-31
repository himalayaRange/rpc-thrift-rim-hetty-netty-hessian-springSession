package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.Client;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.ClientIOException;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.RemoteException;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils.CloseUtils;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils.ExceptionUtils;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.ThriftServce;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-14
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： Thrift客户端
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftClient implements Client<Request,Response>{
	
	private static final Logger LOG = LoggerFactory.getLogger(ThriftClient.class);
	
	private Socket socket;
	
	private TTransport transport;
	
	private ThriftServce.Iface iface;
	
	private boolean closed=true;
	
	private int sendCount=0;
	
	private long openTimestamp =-1;
	
	private ThriftClientConfig config=new ThriftClientConfig();
	
	public ThriftClient(){
		
	}
	
	public ThriftClient(ThriftClientConfig config){
		this.config=config;
	}

	@Override
	public void open() throws ClientIOException {
		try {
			socket=new Socket();
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);
			socket.setSoLinger(false, 0); //socket不执行close方法，底层的socket保持连接,保证请求的快速连接
			socket.setSoTimeout(config.timeout);
			socket.connect(new InetSocketAddress(config.host, config.port), config.connectionTimeout);
			transport=new TSocket(socket); // 设置管道，使用自己的socket是为了ping和send时候设置不同的超时时间
			if(config.framed){ // 使用分帧传输（NIO）
				transport=new TFramedTransport(transport);
			}
			//Iface是Thrift生成的接口，在Thrift的包装下，是个远程接口
			iface=new ThriftServce.Client(config.protocolFactory.getProtocol(transport));
			closed=false;
			sendCount=0;
			openTimestamp=System.currentTimeMillis();
			if(LOG.isInfoEnabled()){
				LOG.info("Open client:"+config.toString());
			}
		} catch (TTransportException | IOException e) {
			throw new ClientIOException("Can not open client " + toString(), e);
		}
		
	}

	@Override
	public long openTimestamp() {
		return openTimestamp;
	}

	/**
	 * 向服务器发送一个请求，判断服务是否可用
	 * @see {@link AbstractThriftServer#setStopTimeoutVal(int)}
	 * @see {@link AbstractThriftServer#stop()}
	 * @see {@link ThriftServceImpl#ping()}
	 **/
	@Override
	public boolean ping() {
		try {
			// 这里需要使用connectionTimeout
			socket.setSoTimeout(config.connectionTimeout);
			// ping值由服务器返回
			return !closed()&&iface.ping();
		} catch (TTransportException | IOException e) {
			//iface.ping服务器端没有抛出异常，表示网络不可用，因而返回false
			return false;
		}catch (Exception e) {
			//这种异常根本不会出现，如果出现应该抛出来，这是框架的BUG
			throw ExceptionUtils.getRuntimeException(e);
		}
	}

	/**
	 * 向服务器发送请求，除了{@link ClientIOException}其他的都是RemoteException
	 * 
	 **/
	@Override
	public Response send(Request request) throws ClientIOException,RemoteException {
		try {
			sendCount++;
			ClientHelper.putCredential(request, config.from, config.token);
			socket.setSoTimeout(config.timeout);
			Response response = iface.execute(request);
			ClientHelper.validateError(response);
			return response;
		} catch (RemoteException e) {
			//服务端发生了异常
			e.addSuppressed(new SuppressedException(config.host + ":" + config.port));
			throw e;
		} catch (TTransportException | IOException e) {
			//这里出现这两种异常表示网络不可用
			throw new ClientIOException("can not send or receive data at " + config.host + ":" + config.port, e);
		} catch (Exception e) {
			//这种异常根本不会出现，如果出现应该抛出来，这是框架的BUG
			throw ExceptionUtils.getRuntimeException(e);
		}
		
	}

	@Override
	public long sendCount() {
		return sendCount;
	}

	@Override
	public void close() {
		closed=true;
		CloseUtils.close(transport);
		if(LOG.isInfoEnabled()){
			LOG.info("Close client: " + this);
		}
	}

	@Override
	public boolean closed() {
		return closed;
	}

	
}
