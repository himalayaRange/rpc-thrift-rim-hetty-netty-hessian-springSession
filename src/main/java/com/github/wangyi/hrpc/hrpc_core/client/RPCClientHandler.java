package com.github.wangyi.hrpc.hrpc_core.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCRequest;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCResponse;
/**
 * 
 * ClassName: RPCClientHandler  
 * Function: 客户端处理
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-24 下午4:59:21 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public abstract class RPCClientHandler extends SimpleChannelInboundHandler<RPCResponse>{

	private static final Logger logger = LoggerFactory.getLogger(RPCClientHandler.class);
	
	private ConcurrentHashMap<String, RPCFuture> pending = new ConcurrentHashMap<String,RPCFuture>();
	
	private volatile Channel channel ; 
	
	private InetSocketAddress socketAddress;
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		channel=ctx.channel();
		socketAddress=(InetSocketAddress)ctx.channel().remoteAddress();
		handlerCallback(channel.pipeline().get(RPCClientHandler.class), true);
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		handlerCallback(channel.pipeline().get(RPCClientHandler.class), false);
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		 logger.error("client caught exception", cause);
	     ctx.close();
	}



	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RPCResponse response)
			throws Exception {
		String requestId = response.getRequestId();
		RPCFuture rpcFuture = pending.get(requestId);
		if(rpcFuture != null){
			pending.remove(requestId);
			rpcFuture.done(response);
		}
	}

	public abstract void handlerCallback(RPCClientHandler handler,boolean isActive);
	
	/**
	 * 发送同步请求
	 * @param request
	 * @return
	 *
	 */
	public RPCFuture sendRequestBySync(RPCRequest request){
		RPCFuture rpcFuture = new RPCFuture(request);
		pending.put(request.getRequestId(), rpcFuture);
		channel.writeAndFlush(request);
		
		return rpcFuture;
	}
	
	/**
	 * 发送异步请求
	 * @param request
	 * @param callbakc
	 * @return
	 *
	 */
	public RPCFuture sendRequestByAsync(RPCRequest request,AsyncRPCCallback  callback){
	    RPCFuture rpcFuture = new RPCFuture(request, callback);
        pending.put(request.getRequestId(), rpcFuture);
        channel.writeAndFlush(request);
       
        return rpcFuture;
	}
	
	
	/**
	 * 关闭请求处理
	 */
	public void close(){
		channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    @Override
    public String toString() {
        return "RPCClientHandler{" +"socketAddress=" + socketAddress + '}';
    }
}
