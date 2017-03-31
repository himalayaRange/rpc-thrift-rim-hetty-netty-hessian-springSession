package com.github.wangyi.hetty.object;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * ClassName: Service  
 * Function: 服务
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-10 下午5:46:58 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class Service implements Serializable{

	private static final long serialVersionUID = 7319171328291118726L;

	public Class<?> typeClass;
	
	public String id;
	
	public String name;
	
	public String defaultVersion = null;
	
	private Map<String,ServiceProvider> providerMap = new LinkedHashMap<String,ServiceProvider>(); //版本号--服务提供者
	
	private boolean overload = false; 
	
	public Service(){
		
	}
	
	public Service(String id,String name){
		this.id=id;
		this.name = name;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultVersion() {
		return defaultVersion;
	}

	public void setDefaultVersion(String defaultVersion) {
		this.defaultVersion = defaultVersion;
	}
	
	public void addServiceProvider(String version,ServiceProvider provider){
		providerMap.put(version, provider);
	}
	
	public Map<String,ServiceProvider> getServiceProvider(){
		return Collections.unmodifiableMap(providerMap);
	}
	
	public boolean isOverload() {
		return overload;
	}

	public void setOverload(boolean overload) {
		this.overload = overload;
	}

	/**
	 * return serviceProvider according serviceName
	 * @param version
	 * @return
	 */
	public ServiceProvider getProviderByVersion(String version){
		if(version == null){
			return providerMap.get(defaultVersion);
		}else{
			return providerMap.get(version);
		}
	}
}
