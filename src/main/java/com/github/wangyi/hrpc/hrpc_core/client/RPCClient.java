package com.github.wangyi.hrpc.hrpc_core.client;

//import java.lang.reflect.Proxy;
import java.util.List;
import net.sf.cglib.proxy.Proxy;
import com.github.wangyi.hrpc.hrpc_core.manager.ConnectManage;
import com.github.wangyi.hrpc.hrpc_core.proxy.AsyncRPCProxy;
import com.github.wangyi.hrpc.hrpc_core.proxy.RPCProxy;
import com.github.wangyi.hrpc.hrpc_core.registry.ServiceDiscovery;



/**
 * 
 * ClassName: RPCClient  
 * Function: RPC client
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-24 下午4:12:42 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class RPCClient {

	private ServiceDiscovery serviceDiscovery;
	
	public RPCClient(String zookeeper ,List<String> interfaces){
		this.serviceDiscovery = new ServiceDiscovery(zookeeper, interfaces);
	}
	
	/**
	 * 创建用于同步调用的代理对象
	 * CGLIB是通过生成字节码生成工具ASM并通过反射获取动态代理类
	 * @param interfaceClass
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	public static <T>  T createProxy(Class<T> interfaceClass){
		 // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RPCProxy<T>(interfaceClass)
        );
	}
	
	/**
	 * 创建用于异步调用的代理对象
	 * @param interfaceClass
	 * @return
	 *
	 */
	public static <T> AsyncRPCProxy  createAsyncProxy(Class<T> interfaceClass){
		
		return new AsyncRPCProxy<T>(interfaceClass);
	}
	
	/**
	 * 关闭zookepper连接
	 * 关闭netty连接
	 */
	public void stop(){
		serviceDiscovery.stop();
		ConnectManage.getInstance().stop();
	}
}
