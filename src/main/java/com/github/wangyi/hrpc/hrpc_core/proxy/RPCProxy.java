package com.github.wangyi.hrpc.hrpc_core.proxy;

//import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import net.sf.cglib.proxy.InvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.client.RPCClientHandler;
import com.github.wangyi.hrpc.hrpc_core.client.RPCFuture;
import com.github.wangyi.hrpc.hrpc_core.exception.NoSuchServiceException;
import com.github.wangyi.hrpc.hrpc_core.manager.ConnectManage;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCRequest;

/**
 * 
 * ClassName: RPCProxy Function: RPC调用代理 <br>
 * 
 * @author wangyi <br>
 * @date: 2017-2-27 上午11:10:02 <br>
 * @version <br>
 * @since JDK 1.7
 */
public class RPCProxy<T> implements InvocationHandler {

	private static final Logger logger = LoggerFactory.getLogger(RPCProxy.class);
	
	private Class<T> clazz;

	public RPCProxy(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 同步调用方法
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		RPCRequest request = new RPCRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        /*logger.info("invoke class: {} method: {}", method.getDeclaringClass().getName(), method.getName());*/
        RPCClientHandler handler = ConnectManage.getInstance().chooseHandler(method.getDeclaringClass().getName());
        if(handler==null){
            logger.error("NoSuchServiceException:",
            		new NoSuchServiceException("no such service about"+method.getDeclaringClass().getName()));
            return null;
        }
        RPCFuture RPCFuture = handler.sendRequestBySync(request);
        return RPCFuture.get();
	}

}
