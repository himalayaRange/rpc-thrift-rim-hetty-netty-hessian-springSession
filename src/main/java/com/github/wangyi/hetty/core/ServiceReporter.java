package com.github.wangyi.hetty.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.github.wangyi.hetty.object.RequestWrapper;

/**
 * 
 * ClassName: ServiceReporter  
 * Function: 服务报告
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 下午2:22:52 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ServiceReporter  {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static final boolean reportBeforeInvoke(RequestWrapper request){
		
		doReport(request);
		
		return true;
	}

	private static void doReport(RequestWrapper request) {
		StringBuilder tip= new StringBuilder("\nHetty request report -------- ").append(sdf.format(new Date())).append(" ------------------------------\n");
		String serviceName = request.getServiceName();
		String serviceClass = ServiceHandler.getServiceByName(serviceName).getClass().getName();
		tip.append("user    : ").append(request.getUser()).append("\n");
		tip.append("password: ").append(request.getPassword()).append("\n");
		tip.append("clientIP: ").append(request.getClientIP()).append("\n");
		tip.append("service : ").append(serviceName).append(".(").append(serviceClass).append(".java)");
		tip.append("method  : ").append(request.getMethodName()).append("\n");
		tip.append("--------------------------------------------------------------------------------\n");
		System.out.print(tip.toString());
	}
}
