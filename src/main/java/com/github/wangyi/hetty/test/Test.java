package com.github.wangyi.hetty.test;

import java.net.MalformedURLException;
import java.util.Date;
import com.caucho.hessian.client.HessianProxyFactory;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.hetty.test.demo.ClassRoom;
import com.github.wangyi.hetty.test.demo.ClassService;
import com.github.wangyi.hetty.test.demo.Hello;

/**
 * 
 * ClassName: Test  
 * Function: Http请求Hessian RPC
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-14 下午2:45:40 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class Test {
	public static void main(String[] args) throws MalformedURLException, IllegalArgumentException, IllegalAccessException {
		String url="http://localhost:8088/apis/hello";
		String urlClass="http://localhost:8088/apis/classService";
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setUser("hettyAdmin");
		factory.setPassword("123456");
		factory.setDebug(true);
		factory.setOverloadEnabled(true);
		final Hello basic = (Hello)factory.create(Hello.class, url);
		final ClassService classService = (ClassService)factory.create(ClassService.class, urlClass);
		System.out.println(basic.hello("汪谊"));
		ClassRoom cm=new ClassRoom();
		cm.setRoomNum("080210231");
		cm.setRoomName("国际贸易工程");
		cm.setScore(86);
		cm.setTotalPerson(36);
		cm.setCreate_date(new Date());
		System.out.println("insert ret:"+ObjectsUtil.stringhelper(classService.addClass(cm)));
		System.out.println("modifty ret:"+ObjectsUtil.stringhelper(classService.modiftyClass(cm)));
	}
}
