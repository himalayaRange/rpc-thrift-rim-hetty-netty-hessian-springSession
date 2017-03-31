package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.cluster.pool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.Client;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.ClientIOException;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils.CloseUtils;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：抽象类，服务对对象池提供对象
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public abstract class ClientFactory<P,R> extends BasePoolableObjectFactory<Client<P, R>>{

public ClientFactory() { }
	
	/**
	 * 创建并且打开{@link Client} 
	 * 
	 * @throws ClientIOException 无法建立连接时抛出
	 * 
	 * @see {@link Client#open()}
	 * */
	@Override
	public Client<P, R> makeObject() throws ClientIOException {
		Client<P, R> client = createClient();
		client.open();
		return client;
	}

	/**
	 * 用于提供Client，不需要open，只要创建即可
	 * */
	protected abstract Client<P, R> createClient();

	@Override
	public void destroyObject(Client<P, R> client) {
		CloseUtils.close(client);
	}

	@Override
	public boolean validateObject(Client<P, R> obj) {
		if (obj == null) {
			return false;
		}
		return obj.ping();
	}
}
