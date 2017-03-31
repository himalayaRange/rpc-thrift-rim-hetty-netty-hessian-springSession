package com.github.wangyi.hetty.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;
import com.github.wangyi.hetty.object.Application;

public class HettySecurity {

	private static final Map<String,Application> applicationMap = new HashMap<String,Application>();
	
	public static void addToApplicationMap(Application app){
		applicationMap.put(app.getUser(), app);
	}

	/**
	 * 检查用户权限
	 * @param user
	 * @param password
	 * @return
	 *
	 */
	public static boolean checkPermission(String user,String password){
		if(StringUtils.isEmpty(user) || StringUtils.isEmpty(password)){
			throw new IllegalArgumentException("user or password is null,please check And retry");
		}
		if(applicationMap.containsKey(user) && applicationMap.get(user).getPassword().equals(password)){
			return true;
		}else{
			return false;
		}
	}
	
}
