package com.github.wangyi.thrift.rpc.constant;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-6
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：框架容器等选择
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftProxy {

	// 1.Spring IOC 大项目 效率低
	// 2.Google Guice 小项目 嵌入式 效率很高
	private int  iocProxy =1 ;

	//1.二进制格式，系统默认 TBinaryProtocol   
	//2.压缩格式 TCompactProtocol
	//3.JSON格式 TJSONProtocol
	//4.提供简单的JSON只写协议，生成的文件很容易通过脚本语言解析
	private int protocol =1 ;
	
	
	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public int getIocProxy() {
		return iocProxy;
	}

	public void setIocProxy(int iocProxy) {
		this.iocProxy = iocProxy;
	}
	
	
	
}
