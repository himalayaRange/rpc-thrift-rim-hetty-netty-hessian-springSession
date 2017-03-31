package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.cluster.pool;

import java.util.List;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： 用以提供很多工厂
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface ClientFactoryProvider<P,R> {

	public List<ClientFactory<P, R>> getFactories();
	
}
