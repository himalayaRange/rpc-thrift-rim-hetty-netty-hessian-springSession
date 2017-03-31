package com.github.wangyi.hrpc.hrpc_core.proxy;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.client.AsyncRPCCallback;
import com.github.wangyi.hrpc.hrpc_core.client.RPCClientHandler;
import com.github.wangyi.hrpc.hrpc_core.client.RPCFuture;
import com.github.wangyi.hrpc.hrpc_core.manager.ConnectManage;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCRequest;

/**
 * 
 * ClassName: AsyncRPCProxy  
 * Function: 异步调用代理对象
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-27 上午10:52:34 
   <br> 
 * @version @param <T> 
   <br>
 * @since JDK 1.7
 */
public class AsyncRPCProxy<T> {

	private static final Logger logger = LoggerFactory.getLogger(AsyncRPCProxy.class);
	
	private Class<T> clazz;
	
	public AsyncRPCProxy(Class<T> clazz){
		this.clazz=clazz;
	}

	/**
	 * 异步调用
	 * @param funcName
	 * @param callback
	 * @param args
	 * @return
	 *
	 */
	public RPCFuture call(String funcName,AsyncRPCCallback callback,Object...args){
		RPCRequest request = new RPCRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setClassName(this.clazz.getName());
		request.setMethodName(funcName);
		request.setParameters(args);
		
		Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);
       /* logger.info("invoke class: {} method: {}", this.clazz.getName(), funcName);*/
	
        RPCClientHandler handler = ConnectManage.getInstance().chooseHandler(this.clazz.getName());
        
        RPCFuture rpcFuture = handler.sendRequestByAsync(request, callback);
        return rpcFuture;
	}
	
	
	   //基本类型转换
    private Class<?> getClassType(Object obj) {
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        if ("java.lang.Integer".equals(typeName)) {
            return Integer.TYPE;
        } else if ("java.lang.Long".equals(typeName)) {
            return Long.TYPE;
        } else if ("java.lang.Float".equals(typeName)) {
            return Float.TYPE;
        } else if ("java.lang.Double".equals(typeName)) {
            return Double.TYPE;
        } else if ("java.lang.Character".equals(typeName)) {
            return Character.TYPE;
        } else if ("java.lang.Boolean".equals(typeName)) {
            return Boolean.TYPE;
        } else if ("java.lang.Short".equals(typeName)) {
            return Short.TYPE;
        } else if ("java.lang.Byte".equals(typeName)) {
            return Byte.TYPE;
        }
        return classType;
    }

}
