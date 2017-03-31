package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TTupleProtocol;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.server.GenericServer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.server.ServerProcessor;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.ThriftGenericConfig;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-15
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：封装Server通用配置
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public  abstract class ConfigurableThriftServer extends GenericServer<Request, Response> {
	
	protected int port=ThriftGenericConfig.DEFAULT_PORT;
	
	protected int workerThreads=ThriftGenericConfig.WORKERTHREADS;
	
	protected int clientTimeout=ThriftGenericConfig.CLIENTTIMEOUT;
	
	protected String protocol;
	
	protected TProtocolFactory protocolFactory;
	
	protected boolean security = ThriftGenericConfig.SECURITY;
	
	protected Map<String,String> allowedFromTokens=new HashMap<String,String>();
	
	protected int stopTimeoutVal=ThriftGenericConfig.STOPTIMEOUTVAL;
	
	public ConfigurableThriftServer() {
		super();
	}
	
	public ConfigurableThriftServer(ServerProcessor<Request, Response> processor){
		super(processor);
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getWorkerThreads() {
		return workerThreads;
	}
	public void setWorkerThreads(int workerThreads) {
		this.workerThreads = workerThreads;
	}
	public TProtocolFactory getProtocolFactory() {
		return protocolFactory;
	}
	public void setProtocolFactory(TProtocolFactory protocolFactory) {
		this.protocolFactory = protocolFactory;
	}
	public boolean isSecurity() {
		return security;
	}
	public void setSecurity(boolean security) {
		this.security = security;
	}
	public Map<String, String> getAllowedFromTokens() {
		return allowedFromTokens;
	}
	public void setAllowedFromTokens(Map<String, String> allowedFromTokens) {
		this.allowedFromTokens = allowedFromTokens;
	}
	
	public int getClientTimeout() {
		return clientTimeout;
	}

	public void setClientTimeout(int clientTimeout) {
		this.clientTimeout = clientTimeout;
	}
	
	public int getStopTimeoutVal() {
		return stopTimeoutVal;
	}

	public void setStopTimeoutVal(int stopTimeoutVal) {
		this.stopTimeoutVal = stopTimeoutVal;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
		if("1".equals(protocol)||"".equals(protocol)||Objects.equals(null, protocol)){
			this.protocolFactory = new TTupleProtocol.Factory();
			if(LOG.isWarnEnabled()){
				LOG.warn("transport protocol is TTupleProtocol");
			}
		}else if("2".equals(protocol)){
			this.protocolFactory = new TBinaryProtocol.Factory();
			if(LOG.isWarnEnabled()){
				LOG.warn("transport protocol is TBinaryProtocol");
			}
		}else if("3".equals(protocol)){
			this.protocolFactory = new TCompactProtocol.Factory();
			if(LOG.isWarnEnabled()){
				LOG.warn("transport protocol is TCompactProtocol");
			}
		}else if("4".equals(protocol)){
			this.protocolFactory = new TJSONProtocol.Factory(); 
			if(LOG.isWarnEnabled()){
				LOG.warn("transport protocol is TJSONProtocol");
			}
		}else{
			this.protocolFactory = new TTupleProtocol.Factory();
			if(LOG.isWarnEnabled()){
				LOG.warn("transport protocol is TTupleProtocol");
			}
		}
	}

	@Override
	public String toString() {
		return "[port=" + port + ", workerThreads=" + workerThreads + ", protocolFactory="
				+ protocolFactory.getClass() + ", clientTimeout=" + clientTimeout + ", stopTimeoutVal=" + stopTimeoutVal
				+ ", security=" + security + ", allowedFromTokens=" + allowedFromTokens + "]";
	}
 

}
