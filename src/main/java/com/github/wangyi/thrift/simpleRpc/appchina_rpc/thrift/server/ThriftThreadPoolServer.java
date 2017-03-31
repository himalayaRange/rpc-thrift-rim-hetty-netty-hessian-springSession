package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server;

import java.util.concurrent.TimeUnit;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
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
 * 类说明：每个客户端消耗一个服务器线程
 * 		 延迟率低，短连接可以考虑
 *      阻塞式Socket工作方式
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftThreadPoolServer extends AbstractThriftServer{

	protected int requestTimeout=(int)TimeUnit.SECONDS.toMillis(60);
	
	protected int minWorkerThreads=ThriftGenericConfig.MINWORKERRHREAD;

	public ThriftThreadPoolServer() {
		super();
	}

	public ThriftThreadPoolServer(ServerProcessor<Request, Response> processor) {
		super(processor);
	}

	@Override
	protected TServer buildThriftServer(TProcessor thriftProcessor)
			throws TTransportException {
		TServerTransport transport = new TServerSocket(port, clientTimeout);
		TThreadPoolServer.Args args = new TThreadPoolServer.Args(transport);
		args.processor(thriftProcessor);
		if(protocolFactory==null){
			protocolFactory=new TTupleProtocol.Factory();
		}
		setProtocol(protocol);
		args.protocolFactory(protocolFactory);
		args.maxWorkerThreads(workerThreads);
		args.minWorkerThreads(minWorkerThreads);
		args.requestTimeout(requestTimeout);
		args.requestTimeoutUnit(TimeUnit.MILLISECONDS);
		args.stopTimeoutVal(stopTimeoutVal);
		args.stopTimeoutUnit = TimeUnit.MILLISECONDS;
		if(LOG.isWarnEnabled()){
			LOG.warn(">>>>>Init Server Thread By ThriftThreadPoolServer,Publisher Port:"+this.port+"<<<<<");
		}
		return new TThreadPoolServer(args);
	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public int getMinWorkerThreads() {
		return minWorkerThreads;
	}

	public void setMinWorkerThreads(int minWorkerThreads) {
		this.minWorkerThreads = minWorkerThreads;
	}

}
