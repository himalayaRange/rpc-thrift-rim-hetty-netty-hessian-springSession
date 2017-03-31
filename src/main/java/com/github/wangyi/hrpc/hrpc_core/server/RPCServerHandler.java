package com.github.wangyi.hrpc.hrpc_core.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import java.util.Map;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCRequest;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCResponse;
/**
 * 
 * ClassName: RPCServerHandler  
 * Function: RPC request processor
 * 一般Netty来接收和发送数据都会继承SimpleChannelInboundHandler和ChannelInboundHandlerAdapter
 * 客户端：继承SimpleChannelInboundHandler，会自动释放资源
 * 服务端：继承ChannelInboundHandlerAdapter,不自动释放，要保证数据传输完成后由客户端释放
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-24 下午2:58:24 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<RPCRequest>{

	private static final Logger logger = LoggerFactory.getLogger(RPCServerHandler.class);
	
	private final Map<String,Object> serviceBeanMap;
	
	public RPCServerHandler(Map<String,Object> serviceBeanMap){
		this.serviceBeanMap=serviceBeanMap;
	}
	
	/**
	 * 服务器监听到客户端的活动
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		 /*logger.info("======rpc server channel active：" + ctx.channel().remoteAddress());*/
	}

	/**
	 * 服务器监听客户端不活动信息
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("======rpc server channel inactive：" + ctx.channel().remoteAddress());
	}


	/**
	 * 当出现Throwable时候被调用
	 * Netty由于IO错误或者处理器在处理事件时候抛出异常
	 * 大部分情况下应该记录下来并把关联的channel关闭
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("rpc server caught exception:"+ctx.channel().remoteAddress()+"|"+cause.getMessage());
		ctx.close();
	}


	/**
	 * 读取客户端端请求
	 */
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, final RPCRequest request)
			throws Exception {
		/*logger.info("=====rpc server channelRead0:"+ctx.channel().remoteAddress());*/
		RPCServer.submit(new Runnable() {
			
			@Override
			public void run() {
				RPCResponse response = new RPCResponse();
				response.setRequestId(request.getRequestId());
				try {
					//通过反射找到对应的服务类和方法
					String className = request.getClassName();
					Object serviceBean = serviceBeanMap.get(className);
					
					String methodName = request.getMethodName();
					Object[] parameters = request.getParameters();
					Class<?>[] parameterTypes = request.getParameterTypes();
					
					//JDK reflect
					/*Class<?> serviceClass = Class.forName(className);
					Method method = serviceClass.getMethod(methodName, parameterTypes);
					method.setAccessible(true);
					Object result = method.invoke(serviceBean, parameters);*/

					//使用CGlib提供反射API
					FastClass serviceFastClass = FastClass.create(serviceBean.getClass());
					FastMethod fastMethod = serviceFastClass.getMethod(methodName,parameterTypes);
					Object result = fastMethod.invoke(serviceBean, parameters);
					
					response.setResult(result);
				} catch (Exception e) {
					response.setError(e.getMessage());
					logger.error("",e);
				}
				
				ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture channelFuture) throws Exception {
							/*logger.info("send response success for request:"+request.getRequestId());*/
					}
				});
			 }
		});
	}

}
