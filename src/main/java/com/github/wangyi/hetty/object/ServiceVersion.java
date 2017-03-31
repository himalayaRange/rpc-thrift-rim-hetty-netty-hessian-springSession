package com.github.wangyi.hetty.object;
/**
 * 
 * ClassName: ServiceVersion  
 * Function: 服务的版本号
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 上午10:05:49 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ServiceVersion {

	private String user;
	
	private String service;
	
	private String version;

	public StringBuffer getUser() {
		return new StringBuffer(user);
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getService() {
		return service;
	}
	
	public void setService(String service) {
		this.service = service;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

}
