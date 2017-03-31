package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils;

import org.apache.commons.pool.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：客户端连接池<基于Apache Commons pool2实现的连接池>
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class PoolUtils {

	private static final Logger LOG =LoggerFactory.getLogger(PoolUtils.class);
	
	/**
	 * 将对象放入连接池
	 * @param pool
	 * @param obj
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void returnObject(ObjectPool pool,Object obj){
		if(pool!=null && obj!=null){
			try {
				pool.returnObject(obj);
			} catch (Exception e) {
				if(LOG.isWarnEnabled()){
					LOG.warn(e.getMessage(),e);
				}
			}
		}
	}
	
	/**
	 * 设置连接池某对象无效，将不再进入连接池
	 * @param pool
	 * @param obj
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void invalidateObject(ObjectPool pool,Object obj){
		if(pool!=null && obj!=null){
			try {
				pool.invalidateObject(obj);
			} catch (Exception e) {
				if(LOG.isWarnEnabled()){
					LOG.warn(e.getMessage(),e);
				}
			}
		}
	}
	
	
	/**
	 * 关闭连接池
	 * @param pool
	 */
	@SuppressWarnings("rawtypes")
	public static void close(ObjectPool pool){
		if(pool!=null){
			try {
				pool.close();
			} catch (Exception e) {
				if(LOG.isWarnEnabled()){
					LOG.warn(e.getMessage(),e);
				}
			}
		}
	}
	
}
