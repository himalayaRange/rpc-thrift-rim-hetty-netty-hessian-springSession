package com.github.wangyi.springSession;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
/**
 * 
 * ClassName: HttpSessionConfig  
 * Function:使用Spring Session替换默认容器的HttpSession,Spring Session用Redis实现
 * 			该类可以替换spring-session.xml中id="redisHttpSessionConfiguration"中的配置
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-17 下午1:21:06 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */

//@EnableRedisHttpSession(maxInactiveIntervalInSeconds=60)
public class HttpSessionConfig {
	
	/*@Bean
	public JedisConnectionFactory connectionFactory(){
		JedisConnectionFactory connection = new JedisConnectionFactory();
		connection.setPort(6379);
		connection.setHostName("127.0.0.1");
		return connection;
	}*/
	
	//如果不是使用自己的数据库，使用的如阿里云的第三方数据库，可以在此配置
	/*@Bean
	public 	static ConfigureRedisAction configureRedisAction(){
		return ConfigureRedisAction.NO_OP;
	}*/
	
}
