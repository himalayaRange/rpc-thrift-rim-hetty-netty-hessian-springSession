package com.github.wangyi.hrpc.hrpc_core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;


/**
 * 
 * ClassName: HRPCService  
 * Function: 注解发布服务
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 上午11:31:03 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HRPCService {
	
	Class<?> value(); //发布的类对象
	
}
