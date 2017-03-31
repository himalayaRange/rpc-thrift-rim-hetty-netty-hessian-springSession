package com.github.wangyi.hrpc.hrpc_core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import com.github.wangyi.hrpc.hrpc_core.constant.HConstant;

/**
 * 
 * ClassName: Config  
 * Function: 读取配置文件
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 上午10:38:22 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class Config {

	public static final int ZK_SESSION_TIMEOUT = HConstant.ZK_SESSION_TIMEOUT;
	
	public static final String ZK_ROOT_PATH = HConstant.ZK_ROOT_PATH;
	
	private static Properties properties;
	
	static{
		try{
			InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(HConstant.HRPC_CONFIG_PATH);
			
			BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			
			properties=new Properties();
			
			properties.load(bf);
		}catch(IOException e1){
			e1.printStackTrace();
		}
	}
	
	
	public static int getIdntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static String getSdtringProperty(String key) {
        return properties.getProperty(key);
    }
	
	
}
