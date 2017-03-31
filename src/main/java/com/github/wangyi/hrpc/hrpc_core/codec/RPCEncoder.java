package com.github.wangyi.hrpc.hrpc_core.codec;

import com.github.wangyi.hrpc.hrpc_core.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 
 * ClassName: RPCEncoder  
 * Function: RPC 编码<Netty自定义编码器>
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 下午2:52:10 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
@SuppressWarnings("rawtypes")
public class RPCEncoder extends MessageToByteEncoder {

	private Class<?> genericClass;
	
	public RPCEncoder(Class<?> genericClass){
		this.genericClass=genericClass;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out)
			throws Exception {
		if(genericClass.isInstance(in)){
			byte[] data = SerializationUtil.serialize(in);
			out.writeInt(data.length);
			 out.writeBytes(data);
		}
	}

}
