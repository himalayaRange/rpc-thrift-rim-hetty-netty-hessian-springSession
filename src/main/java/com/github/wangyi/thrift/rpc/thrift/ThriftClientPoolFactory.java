package com.github.wangyi.thrift.rpc.thrift;

import java.net.InetSocketAddress;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.github.wangyi.thrift.rpc.exception.ThriftException;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-28
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift客户端连接池工厂
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftClientPoolFactory extends BasePooledObjectFactory<TServiceClient>{

	private static final Logger LOG = LoggerFactory.getLogger(ThriftClientPoolFactory.class);
	
	private InetSocketAddress address; //IP
	
	private TServiceClientFactory<TServiceClient> clientFactory; //thrift客户端工厂
	
	private PoolOperationCallBack callback; //连接池回调
	
	private int protocol;
	
	protected ThriftClientPoolFactory(InetSocketAddress address2, TServiceClientFactory<TServiceClient> clientFactory,
			   int protocol,PoolOperationCallBack callback) throws Exception {
		this.address = address2;
		this.clientFactory = clientFactory;
		this.protocol=protocol;
		this.callback = callback;
	}
	
	@Override
	public TServiceClient create() throws Exception {
		if(address==null){
			new ThriftException("No Provider avaliable for remote service");
		}
		
		TSocket tSocket = new TSocket(address.getHostName(), address.getPort()); //阻塞式socket
		
		TFramedTransport transport = new TFramedTransport(tSocket);
		
		TProtocol protocol;
		
		if(StringUtils.isEmpty(this.protocol)){
			protocol = mathProtocol(1, transport);	
		}else{
			protocol = mathProtocol(this.protocol, transport);
		}
		
		TServiceClient client = this.clientFactory.getClient(protocol);
		
		transport.open();
		if(callback!=null){
			try {
				callback.make(client);
			} catch (Exception e) {
				LOG.warn("makeObject:{}", e);
			}
		}
		
		return client;
	}

	private TProtocol mathProtocol(int f,TFramedTransport transport){
		switch (f) {
			case 1:
				return new TBinaryProtocol(transport);
			case 2:
				return new TCompactProtocol(transport);
			case 3:
				return new TJSONProtocol(transport);
			case 4:
				return new TSimpleJSONProtocol(transport);
			default:
				return new TBinaryProtocol(transport);
		}
	}
	
	@Override
	public PooledObject<TServiceClient> wrap(TServiceClient obj) {
		
		return new DefaultPooledObject<TServiceClient>(obj);
	}
	
	@Override
	public void destroyObject(PooledObject<TServiceClient> client) throws Exception {
		if(callback != null) {
			try{
				callback.destroy(client);
			}catch(Exception e) {
				LOG.warn("destroyObject:{}",e);
			}
		}
		LOG.info("destroyObject:{}", client);
		TTransport pin = client.getObject().getInputProtocol().getTransport();
		pin.close();
		TTransport pout = client.getObject().getOutputProtocol().getTransport();
		pout.close();
	}
	
	@Override
	public boolean validateObject(PooledObject<TServiceClient> client) {
		TTransport pin = client.getObject().getInputProtocol().getTransport();
		TTransport pout = client.getObject().getOutputProtocol().getTransport();
		return pin.isOpen() && pout.isOpen();
	}
	
	static interface PoolOperationCallBack {
		// 销毁client之前执行
		void destroy(PooledObject<TServiceClient> client);

		// 创建成功后执行
		void make(TServiceClient client);
		
	}
}
