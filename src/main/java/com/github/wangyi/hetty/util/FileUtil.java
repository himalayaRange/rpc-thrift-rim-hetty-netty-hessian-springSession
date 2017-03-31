package com.github.wangyi.hetty.util;

import java.io.File;

/**
 * 
 * ClassName: FileUtil  
 * Function: 文件工具类
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-10 下午2:34:18 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class FileUtil {

	public static File getFile(String path){
		String applicationPath = FileUtil.class.getClassLoader().getResource("").getPath();
		return new File(applicationPath,path); //父类 子类
	}
	
}
