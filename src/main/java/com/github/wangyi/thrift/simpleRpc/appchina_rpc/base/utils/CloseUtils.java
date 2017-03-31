package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils;

import java.io.Closeable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：资源关闭Utils
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class CloseUtils {

	private static final Logger LOG = LoggerFactory.getLogger(CloseUtils.class);

	public static void close(Closeable obj){
		if(obj!=null){
			try {
				obj.close();
			} catch (IOException e) {
				if(LOG.isWarnEnabled()){
					LOG.warn(e.getMessage(),e);
				}
			}
		}
	}
}
