package com.github.wangyi.hetty.conf;

import java.util.List;
import com.github.wangyi.hetty.object.Application;
import com.github.wangyi.hetty.object.Service;
import com.github.wangyi.hetty.object.ServiceVersion;

/**
 * 
 * ClassName: ConfigParser  
 * Function: 配置解析器
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 上午10:09:25 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public interface ConfigParser {

	/**
	 * 获取所有的服务
	 * parseService:
	 */
	List<Service> parseService();
	
	/**
	 * 获取所有的应用
	 * parseApplication:
	 */
	List<Application> parseApplication();

	/**
	 * 获取所有的服务版本
	 * parseSecurity:
	 */
	List<ServiceVersion> parseSecurity();
	
}
