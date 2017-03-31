package com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-12
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：请求路径处理类
 * 		将接口的方法解析为服务地址（因为方法的参数使用了简介名，可能导致重复）
 * 		可通过{@link AService}注解设置服务地址，避免重复
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public final class PathUtils {

	/**
	 * 构建服务路径
	 * @param serviceName 服务的名称
	 * @param proxyInterface 服务的接口
	 * @param method 服务的方法
	 * @param requestIP 请求的IP
	 * @param needCheck 是否需要进行检查，发布服务时不检查，远程Rpc请求时进行IP，是否有效性等等检查
	 * @return
	 */
	public static String buildServicePath(String serviceName,Class<?> proxyInterface,Method method,String requestIP,boolean needCheck,String version){
		StringBuilder sb=new StringBuilder("/");
		if(serviceName!=null){
			sb.append(serviceName).append("/");
		}
		sb.append(proxyInterface.getName()).append("/");
		Object relative = paseRelativePath(proxyInterface,method,requestIP,needCheck);
		if(relative==null){
			return null;
		}
		SPI spi = proxyInterface.getAnnotation(SPI.class);
		if(spi!=null){
			String defVersion = spi.version();
			return sb.append(paseRelativePath(proxyInterface,method,requestIP,needCheck)).append("?version=").append(defVersion).toString();
		}
		return sb.append(paseRelativePath(proxyInterface,method,requestIP,needCheck)).append("?version=").append(version!=null&&version!=""?version:"1.0.0").toString();
	}

	/**
	 * 反射解析注解
	 * @param proxyInterface
	 * @param method
	 * @param requestIp
	 * @param needCheck
	 * @return
	 */
	private static Object paseRelativePath(Class<?> proxyInterface,Method method,String requestIp,boolean needCheck) {
		// 1.类注解
		SPI typeAnnotation = proxyInterface.getAnnotation(SPI.class);//获取类注解
		if(typeAnnotation!=null){
			if(needCheck){
				//1-1接口是否暂停使用
				boolean available = typeAnnotation.available();
				if(!available){
					throw new RuntimeException("@SPI(available=false)The method does not support your request,case the method is invalid :"+method);
				}
				//1-2判读是否在黑名单中
				String blackList = typeAnnotation.blackList();
				if(!Objects.equals("",blackList)){
					String[] checkIP=blackList.split(",");
					List<String> asList = Arrays.asList(checkIP);
					if(asList.contains(requestIp)){
						throw new RuntimeException("@SPI(blackList='xx.xx.xx.xx')The method does not support your request,case your request has put into White and black lists :"+method);
					}
				}
			}
			//1-2方法上标注了@SPI()默认方式获取PATH
			StringBuilder sb=new StringBuilder(method.getName());
			sb.append("/");
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(parameterTypes!=null&&parameterTypes.length>0){
				for(Class<?> type:parameterTypes){
					sb.append(type.getSimpleName());
					sb.append("/");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		
		// 2.方法注解
		SPI methodAnnotation  = method.getAnnotation(SPI.class);//获取Method服务的注解
		if(needCheck){
			//2-1接口是否暂停使用
			boolean available = methodAnnotation.available();
			if(!available){
				throw new RuntimeException("@SPI(available=false)The method does not support your request,case the method is invalid :"+method);
			}
			//2-2加入黑白名单
			String MethodBlackList = methodAnnotation.blackList();
			if(!Objects.equals("",MethodBlackList)){
				String[] checkIP=MethodBlackList.split(",");
				List<String> asList = Arrays.asList(checkIP);
				if(asList.contains(requestIp)){
					throw new RuntimeException("The method does not support your request,case your request has put into White and black lists :"+method);
				}
			}
		}
		if(methodAnnotation==null){
			//2-1未标注的不进行发布
			return null;
		}
		else if("".equals(methodAnnotation.value())){
			//2-2方法上标注了@SPI()默认方式获取PATH
			StringBuilder sb=new StringBuilder(method.getName());
			sb.append("/");
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(parameterTypes!=null&&parameterTypes.length>0){
				for(Class<?> type:parameterTypes){
					sb.append(type.getSimpleName());
					sb.append("/");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}else{
			//2-3方法上标注了@SPI(value="XXX")可以定义方法名
			return methodAnnotation.value();
		}
	}
}
