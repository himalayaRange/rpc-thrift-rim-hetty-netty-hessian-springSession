package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.client;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TTupleProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.ThriftGenericConfig;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-14
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： Thrift基础配置
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftClientConfig{
	
	private static final Logger LOG = LoggerFactory.getLogger(ThriftClient.class);

	public String host= ThriftGenericConfig.DEFAULT_HOST;
	
	public int port = ThriftGenericConfig.DEFAULT_PORT;
	
	public int timeout=(int)TimeUnit.MINUTES.toMillis(1);
	
	public int connectionTimeout= (int) TimeUnit.SECONDS.toMillis(1);
	
	public boolean framed = false; //是否采用分帧传输，如果线程模型采用非阻塞NIO通讯，必须设置成true
	
	public TProtocolFactory protocolFactory = new TTupleProtocol.Factory(); //协议工厂，默认即可
	
	public String protocol; //协议名称
	
	public String from = "";
	
	public String token = "";

	public ThriftClientConfig(){}

	public ThriftClientConfig(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public boolean isFramed() {
		return framed;
	}

	public void setFramed(boolean framed) {
		this.framed = framed;
	}

	public TProtocolFactory getProtocolFactory() {
		return protocolFactory;
	}

	public void setProtocolFactory(TProtocolFactory protocolFactory) {
		this.protocolFactory = protocolFactory;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
		if(this.protocol!=null||this.protocol!=""){
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
	}

	@Override
	public String toString() {
		return "[host=" + host + ", port=" + port + ", timeout=" + timeout
				+ ", connectionTimeout=" + connectionTimeout + ", framed=" + framed + ", protocolFactory="
				+ protocolFactory.getClass() + ", from=" + from + ", token=" + token + "]";
	}

	
}
