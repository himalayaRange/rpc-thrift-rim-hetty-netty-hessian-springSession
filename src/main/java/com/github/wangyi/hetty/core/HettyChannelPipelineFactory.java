package com.github.wangyi.hetty.core;

import static org.jboss.netty.channel.Channels.pipeline;
import java.util.concurrent.ExecutorService;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class HettyChannelPipelineFactory implements ChannelPipelineFactory{

	private ExecutorService threadpool;
	
	
	public HettyChannelPipelineFactory(ExecutorService threadpool) {
		this.threadpool = threadpool;
	}
	
	/**
	 * Channel: 是Netty最核心的接口，一个channel对应一个socket通道，通过channel可以对socket进行各种操作
	 * 
	 * ChannelHandler: 对Channel的包装，通过ChannelHandler间接操作Channel
	 * 
	 * ChannelPipeline:可以看做是ChannerlHandler的一个链表，当需要对Channel进行某种处理的时候，Pipeline负责依次调用每一个Handler进行处理。
	 *
	 * Netty中的Handler类似于servlet中的filter，可以对报文进行编码解码，拦截指定报文，控制Hander是否执行等。。。
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", new HettyHandler(threadpool)); //自定义Handler对请求进行拦截
		return pipeline;
	}

}
