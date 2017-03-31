package com.github.wangyi.hetty.plugin;

import java.util.List;
import com.github.wangyi.hetty.conf.HettyConfig;
import com.github.wangyi.hetty.conf.XmlConfigParser;
import com.github.wangyi.hetty.core.HettySecurity;
import com.github.wangyi.hetty.core.ServiceHandler;
import com.github.wangyi.hetty.object.Application;
import com.github.wangyi.hetty.object.Service;
import com.github.wangyi.hetty.object.ServiceVersion;

/**
 * 
 * ClassName: XmlConfigPlugin  
 * Function: XML插件
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-10 下午5:28:44 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class XmlConfigPlugin implements IPlugin{

	@Override
	public boolean start() {
		String configFile = HettyConfig.getInstance().getpropertiesFile();
		String[] fileArr = configFile.split(",");
		
		for(String file:fileArr){
			XmlConfigParser configParser = new XmlConfigParser(file);
			
			List<Application> appList = configParser.parseApplication();
			for(Application app:appList){
				HettySecurity.addToApplicationMap(app);
			}
			
			List<Service> serviceList = configParser.parseService();
			for(Service service:serviceList){
				ServiceHandler.addToServiceMap(service);
			}
			
			List<ServiceVersion>  versionList = configParser.parseSecurity();
			if(versionList != null){
				for(ServiceVersion version:versionList){
					ServiceHandler.addToVersionMap(version);
				}
			}
		}
		return true;
	}

	@Override
	public boolean stop() {
		return false;
	}

}
