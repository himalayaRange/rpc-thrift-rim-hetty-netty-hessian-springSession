package com.github.wangyi.hetty.test;

import java.net.MalformedURLException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import com.caucho.hessian.client.HessianProxyFactory;
import com.github.wangyi.hetty.test.demo.Hello;

/**
 * 
 * ClassName: TestSSL  
 * Function: HTTPS请求Hessian RPC
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-14 下午4:06:40 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class TestSSL {
	public static void main(String[] args) throws MalformedURLException {
		String url = "https://localhost:9000/apis/hello/";
		HostnameVerifier  hv= new HostnameVerifier() {
			
			@Override
			public boolean verify(String urlHostName, SSLSession session) {
				System.out.println("urlHostName:"+urlHostName);
				System.out.println("session:"+session.getPeerHost());
				return urlHostName.equals(session.getPeerHost());
			}
		};
		
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setUser("hettyAdmin");
		factory.setPassword("123456");
		factory.setDebug(true);
		factory.setOverloadEnabled(true);
		final Hello basic = (Hello) factory.create(Hello.class, url);
		// 测试方法重载
		System.out.println(basic.hello("Https测试！"));
	}
}
