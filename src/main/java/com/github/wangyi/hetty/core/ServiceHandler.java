package com.github.wangyi.hetty.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.github.wangyi.hetty.conf.HettyConfig;
import com.github.wangyi.hetty.object.HettyException;
import com.github.wangyi.hetty.object.RequestWrapper;
import com.github.wangyi.hetty.object.Service;
import com.github.wangyi.hetty.object.ServiceProvider;
import com.github.wangyi.hetty.object.ServiceVersion;

/**
 * 
 * ClassName: ServiceHandler  
 * Function: TODO
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 下午2:46:23 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ServiceHandler {

	private static final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);
	//key:service name value:service
	public static final Map<String,Service> serviceMap = new HashMap<String,Service>();
	//key:client user # service name value:version
	public static final Map<String,String> versionMap = new HashMap<String,String>();
	
	public static void addToServiceMap(Service service){
		if(service.getDefaultVersion()==null){
			Iterator<String> iterator = service.getServiceProvider().keySet().iterator();
			if(iterator.hasNext()){
				String defaultVersion=iterator.next();
				service.setDefaultVersion(defaultVersion);
			}else{
				throw new HettyException("your have a wrong in service config.check service["+service.getName()+"]'s provider.");
			}
		}
		serviceMap.put(service.getName(), service);
	}
	
	public static boolean isServiceExists(String serviceName){
		
		return 	serviceMap.containsKey(serviceName);
	}
	
	
	public static void addToVersionMap(ServiceVersion version){
		if(version.getVersion()==null){
			return;
		}
		if(!isServiceExists(version.getService())){
			throw new HettyException("please check your configure file,service["+version.getService()+"] can't find.");
		}
		versionMap.put(version.getUser().append("#").append(version.getService()).toString(),version.getVersion());
	}
	
	public static Object handleRequest(RequestWrapper request){
		Object result = null;
		
		// 开发模式下打印出相关的请求信息
		if(HettyConfig.getInstance().getDevMod()){
			ServiceReporter.reportBeforeInvoke(request);
		}
		
		try {
			String user=request.getUser();
			String password=request.getPassword();
			String serviceName=request.getServiceName();
			String methodName=request.getMethodName();
		
			boolean checkPermission = HettySecurity.checkPermission(user, password);
			if(!checkPermission){
				throw new RuntimeException("the user or password wrong,please check your user or password.");
			}
			StringBuffer serviceKey=new StringBuffer(user).append("#").append(serviceName);
			String version=versionMap.get(serviceKey.toString());
			Service service=serviceMap.get(serviceName);
			
			if (service == null) {
				throw new RuntimeException("we cannot find service["
						+ serviceName + "].");
			}
	
			ServiceProvider serviceProvider = service.getProviderByVersion(version);
			Class<?> processorClass = serviceProvider.getProcessorClass();
			Object processor  = processorClass.newInstance();
			Object[] args = request.getArgs();
			MethodAccess methodAccess = serviceProvider.getMethodAccess();
			int methodIndex = methodAccess.getIndex(methodName,request.getArgsTypes());
			result  = methodAccess.invoke(processor, methodIndex, args);	
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return result;
	}
	
	
	
	public static Map<String,Service> getServiceMap(){
		
		return Collections.unmodifiableMap(serviceMap);
	}
	
	public static Service getServiceByName(String serviceName){
		
		return serviceMap.get(serviceName);
	}
	
	
}
