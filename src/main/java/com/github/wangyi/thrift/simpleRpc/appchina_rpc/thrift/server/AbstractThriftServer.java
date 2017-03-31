package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportException;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.server.ServerProcessor;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.client.ThriftClient;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.ThriftServce;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-15
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift Server实现，用来继承，子类提供{@link TServer}创建<br>
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public abstract class AbstractThriftServer extends ConfigurableThriftServer{

	private TServer server=null;
	
	private boolean running =false;
	
	public AbstractThriftServer(){
		
	}
	
	public AbstractThriftServer(ServerProcessor<Request, Response> processor){
		super(processor);
	}
	
	/**
	 * 启动服务
	 * @throws TTransportException 
	 **/
	@Override
	protected void startServer() throws TTransportException {
		TProcessor thriftProcessor = buildThriftProcessor();
		server = buildThriftServer(thriftProcessor);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					running = true;
					server.serve();
				} finally{
					running = false;
				}
			}
		}).start();
	}
	
	/**
	 * 通用服务处理器
	 * @return
	 */
	protected TProcessor buildThriftProcessor(){
		ThriftServce.Iface service= new ThriftServiceImpl();
		return new ThriftServce.Processor<ThriftServce.Iface>(service);
	}
	/**
	 * 
	 * @param thriftProcessor
	 * @return
	 * @throws TTransportException
	 */
	protected  abstract TServer buildThriftServer(TProcessor thriftProcessor)throws TTransportException;
	
	/**
	 * 此类是与Thrift的结合点<br>
	 * 在{@link ThriftClient}中调用的{@link ThriftServce.Iface}会被Thrift转发这个类进行处理<br>
	 */
	public class ThriftServiceImpl implements ThriftServce.Iface{

		/**
		 * 由{@link ThriftClient#send(Request)}发送的请求，会被Thrift框架转发到此方法。<br>
		 * <br>
		 * 与{@link ThriftClient}约定，这里出现了任何异常都会使用{@link ServerHelper#putError(Response, String)}将异常信息放入header<br>
		 * <br>
		 * {@link ThriftClient}需要使用{@link ClientHelper#validateError(Response)}验证。<br>
		 */
		@Override
		public Response execute(Request request) throws TException {
			try {
				if(security){
					ServerHelper.valudateCredential(request, allowedFromTokens);
				}
				Response response = doServerProcessor(request);
				return response==null?ServerHelper.newResponse():response;
			} catch (CredentialException  e) {
				Response response = ServerHelper.newResponse();
				return ServerHelper.putError(response, "Invalid credential.");
			}catch(Throwable e){
				if(LOG.isErrorEnabled()){
					LOG.error("Unexpected exception:"+e.getMessage(),e);
				}
				Response response = ServerHelper.newResponse();
				return ServerHelper.putError(response, new StringBuffer(e.getClass().getName()).append(":").append(e.getMessage()).toString());
			}
		}

		/**
		 * 由{@link ThriftClient#ping()}发送的请求，会被Thrift框架转发至此方法
		 * <br>
		 * @return Server的状态
		 **/
		@Override
		public boolean ping() throws TException {
			return running;
		}
		
	}
	
	/**
	 * {@link #stopTimeoutVal}指定了安全时间<br>
	 * 这段时间{@link ThriftServceImpl#ping}返回的值是false,表示server无法提供服务,但不影响请求的处理
	 * 客户端需要对此进行支持，这段时间不要再发送请求
	 **/
	@Override
	protected void stopServer() {
		if(server!=null){
			try {
				if(running){
					running=false;
					Thread.sleep(stopTimeoutVal);
				}
				server.stop();
			} catch (InterruptedException  e) {
				server.stop();
				Thread.interrupted();
			}
		}
	}
	
}

