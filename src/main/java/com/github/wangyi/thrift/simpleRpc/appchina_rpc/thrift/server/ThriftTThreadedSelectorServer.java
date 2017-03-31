package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server;

import java.util.concurrent.TimeUnit;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.server.TThreadedSelectorServer.Args;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
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
 * 类说明： 维护两个线程池，一个处理网络I/O,另一个用来处理RPC请求业务
 * 		   网络I/O是瓶颈时候，TThreadedSelectorServer比HshaServer效率高
 * 		 NIO Socket工作方式
 * 		  参考：http://blog.csdn.net/sunmenggmail/article/details/46818147	
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftTThreadedSelectorServer  extends AbstractThriftServer {

	protected int selectorThreads = ThriftGenericConfig.SELECTORTHREADS ; //选择器线程
	
	protected int minWorkerThreads = ThriftGenericConfig.MINWORKERRHREAD;
	
	public ThriftTThreadedSelectorServer() {
		super();
	}

	public ThriftTThreadedSelectorServer(ServerProcessor<Request, Response> processor) {
		super(processor);
	}
	
	@Override
	protected TServer buildThriftServer(TProcessor thriftProcessor)
			throws TTransportException {
		TNonblockingServerSocket transport = new TNonblockingServerSocket(port,clientTimeout);
		Args args = new TThreadedSelectorServer.Args(transport);
		args.processor(thriftProcessor);
		args.transportFactory(new TFramedTransport.Factory());// 分帧传输
		if(protocolFactory==null){
			protocolFactory=new TTupleProtocol.Factory();
		}
		setProtocol(protocol);
		args.protocolFactory(protocolFactory);
		args.workerThreads(workerThreads);
		args.selectorThreads(selectorThreads);
		args.stopTimeoutVal(stopTimeoutVal);
		args.stopTimeoutUnit(TimeUnit.MINUTES);
		TThreadedSelectorServer server = new TThreadedSelectorServer(args);
		if(LOG.isWarnEnabled()){
			LOG.warn(">>>>>Init Server Thread By ThriftTThreadedSelectorServer,Publisher Port:"+this.port+"<<<<<");
		}
		return server;
	}

	public int getSelectorThreads() {
		return selectorThreads;
	}

	public void setSelectorThreads(int selectorThreads) {
		this.selectorThreads = selectorThreads;
	}

	public int getMinWorkerThreads() {
		return minWorkerThreads;
	}

	public void setMinWorkerThreads(int minWorkerThreads) {
		this.minWorkerThreads = minWorkerThreads;
	}
	
}
