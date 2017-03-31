package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server;

import java.util.concurrent.TimeUnit;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
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
 * 类说明： 单独的线程处理网络I/O,独立的worker线程处理消息（NIO Socket工作方式）
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftHsHaServer extends AbstractThriftServer{

	
	protected int minWorkerThreads=ThriftGenericConfig.MINWORKERRHREAD;
	
	public ThriftHsHaServer(){ }

	public ThriftHsHaServer(ServerProcessor<Request, Response> processor){
		super(processor);
	}
	
	public int getMinWorkerThreads() {
		return minWorkerThreads;
	}

	public void setMinWorkerThreads(int minWorkerThreads) {
		this.minWorkerThreads = minWorkerThreads;
	}

	/**
	 * Thrift服务端的模式比较
	 * 
	 * {服务端采用HsHaServer模式}
	 * NonblockingServer工作模式，该模式也是单线程工作，但是该模式采用NIO的方式，所有的socket都被注册到selector中，
	 * 
	 * 在一个线程中通过seletor循环监控所有的socket，每次selector结束时，处理所有的处于就绪状态的socket，对于有数据到来
	 * 的socket进行数据读取操作，对于有数据发送的socket则进行数据发送，对于监听socket则产生一个新业务socket并将其注册到selector中
	 * 
	 * TNonblockingServer模式优点：
	 * 相比于TSimpleServer效率提升主要体现在IO多路复用上，TNonblockingServer采用非阻塞IO，同时监控多个socket的状态变化；
	 * 
	 * TNonblockingServer模式缺点：
	 * TNonblockingServer模式在业务处理上还是采用单线程顺序来完成，在业务处理比较复杂、耗时的时候，例如某些接口函数需要读取数据库执行时间较长，此时该模式效率也不高，因为多个调用请求任务依然是顺序一个接一个执行。
	 * 
	 * THsHaServer类是TNonblockingServer类的子类，在TNonblockingServer模式中，采用一个线程来完成对所有socket的监听和业务处理，造成了效率的低下，THsHaServer模式的引入则是部分解决了这些问题。THsHaServer模式中，引入一个线程池来专门进行业务处理。
	 * 
	 * THsHaServer的优点：
	 * 与TNonblockingServer模式相比，THsHaServer在完成数据读取之后，将业务处理过程交由一个线程池来完成，主线程直接返回进行下一次循环操作，效率大大提升；
	 *
	 * THsHaServer的缺点：
	 * 主线程需要完成对所有socket的监听以及数据读写的工作，当并发请求数较大时，且发送数据量较多时，监听socket上新连接请求不能被及时接受。
 	 *
 	 * TThreadPoolServer模式
	 * TThreadPoolServer模式采用阻塞socket方式工作,主线程负责阻塞式监听“监听socket”中是否有新socket到来，业务处理交由一个线程池来处理
 	 *
 	 * TThreadPoolServer模式优点：
     * 线程池模式中，数据读取和业务处理都交由线程池完成，主线程只负责监听新连接，因此在并发量较大时新连接也能够被及时接受。线程池模式比较适合服务器端能预知最多有多少个客户端并发的情况，这时每个请求都能被业务线程池及时处理，性能也非常高。
	 * 
	 * TThreadPoolServer模式缺点：
	 * 线程池模式的处理能力受限于线程池的工作能力，当并发请求数大于线程池中的线程数时，新请求也只能排队等待。
 	 *
 	 * TThreadedSelectorServer
	   TThreadedSelectorServer模式是目前Thrift提供的最高级的模式，它内部有如果几个部分构成：
	     （1）  一个AcceptThread线程对象，专门用于处理监听socket上的新连接；
	     （2）  若干个SelectorThread对象专门用于处理业务socket的网络I/O操作，所有网络数据的读写均是有这些线程来完成；
	     （3）  一个负载均衡器SelectorThreadLoadBalancer对象，主要用于AcceptThread线程接收到一个新socket连接请求时，决定将这个新连接请求分配给哪个SelectorThread线程。
	     （4）  一个ExecutorService类型的工作线程池，在SelectorThread线程中，监听到有业务socket中有调用请求过来，则将请求读取之后，交个ExecutorService线程池中的线程完成此次调用的具体执行；
 	 *
 	 *TThreadedSelectorServer模式中有一个专门的线程AcceptThread用于处理新连接请求，因此能够及时响应大量并发连接请求；另外它将网络I/O操作分散到多个
 	 *SelectorThread线程中来完成，因此能够快速对网络I/O进行读写操作，能够很好地应对网络I/O较多的情况；TThreadedSelectorServer对于大部分应用场景性
 	 *能都不会差，因此，如果实在不知道选择哪种工作模式，使用TThreadedSelectorServer就可以。
 	 *
 	 **/
	@Override
	protected TServer buildThriftServer(TProcessor thriftProcessor)
			throws TTransportException {
		TNonblockingServerSocket transport = new TNonblockingServerSocket(port,clientTimeout);
		THsHaServer.Args  args = new THsHaServer.Args(transport); // THsHaServer是TNonblockingServer的子类
		args.processor(thriftProcessor);
		args.transportFactory(new TFramedTransport.Factory());
		if(protocolFactory==null){
			protocolFactory=new TTupleProtocol.Factory();
		}
		setProtocol(protocol);
		args.protocolFactory(protocolFactory);
		args.minWorkerThreads(minWorkerThreads);
		args.maxWorkerThreads(workerThreads);
		args.stopTimeoutVal(stopTimeoutVal);
		args.stopTimeoutUnit(TimeUnit.MILLISECONDS);
		if(LOG.isWarnEnabled()){
			LOG.warn(">>>>>Init Server Thread By buildThriftServer,Publisher Port:"+this.port+"<<<<<");
		}
		return new THsHaServer(args);
	}

}
