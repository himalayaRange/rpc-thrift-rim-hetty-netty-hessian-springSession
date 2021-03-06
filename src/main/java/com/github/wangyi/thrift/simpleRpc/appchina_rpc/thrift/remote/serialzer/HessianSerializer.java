package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.Serializer;

/**
 * 
 * ========================================================
 * 日 期：@2017-1-4
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：使用Hessian2序列化和反序列化，Hessian原来的序列化比Hessian2要低
 * 		使用了二进制的RPC协议，很适合发送二进制数据
 
	 注意： 
	 
	 1. 在Hessian远程调用方法中，客户端中的接口类必须和服务器中的接口类一样，方法名称也一样 
	 
	 2. 在接口类中，不要写重构的方法，Hessian不能识别重构的方法。 
	 
	 3. 方法参数中，如果有自定义实体对象entity，则有以下几注意点： 
	 
	 a  entity的package名必须同服务器上的package，否则会在服务端上报找不到此类 
	 
	 b  entity必须是可序列化的，如果是组合对象，则可序列化应该可递归下去，除非不需要组合 
	 
	 4. 方法返回值中，如果有自定义对象，同2，如果是集合对象，则为List(lists and arrays) & map(maps and dictionaries) 
	 
	 5. Hessian 不支持文件传输，如需要文件传输，则传递数据流实现（下一文档说明） 
	 
	 
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class HessianSerializer implements Serializer {

	@Override
	public <T> byte[] getBytes(T obj) throws Exception {
		if(obj==null)
			throw new NullPointerException();
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
        HessianOutput ho = new HessianOutput(os);  
        ho.writeObject(obj); 
        return os.toByteArray();  
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(byte[] bytes,Class<T> clazz) throws Exception {
		 if (bytes == null)  
	         throw new NullPointerException(); 
		 ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
		 HessianInput hi=new HessianInput(bs);
		return (T) hi.readObject();
	}

}
