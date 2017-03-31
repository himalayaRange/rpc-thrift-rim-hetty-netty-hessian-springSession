package com.github.wangyi.hetty.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.github.wangyi.hetty.object.Service;

public class MetadataProcessor {

	private static Map<String,ServiceMetaData> hessianServiceMetaMap = new HashMap<String,ServiceMetaData>();
	
	public static void initMetaDataMap(){
		Map<String, Service> serviceMap = ServiceHandler.getServiceMap();
		Set<String> serviceNames = serviceMap.keySet();
		for(String name : serviceNames){
			Service service = serviceMap.get(name);
			Class<?> clazz = service.getTypeClass();
			ServiceMetaData smd =new ServiceMetaData(clazz, service.isOverload());
			hessianServiceMetaMap.put(name, smd);
		}
	}
	
	public static ServiceMetaData getServiceMetaData(String name){
		
		return hessianServiceMetaMap.get(name);
	}
	
}
