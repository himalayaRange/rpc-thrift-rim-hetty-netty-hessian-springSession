package com.github.wangyi.hrpc.hrpc_client;

import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.hrpc.hrpc_api.entity.User;
import com.github.wangyi.hrpc.hrpc_api.service.UserService;
import com.github.wangyi.hrpc.hrpc_core.client.AsyncRPCCallback;
import com.github.wangyi.hrpc.hrpc_core.client.RPCClient;
import com.github.wangyi.hrpc.hrpc_core.client.RPCFuture;
import com.github.wangyi.hrpc.hrpc_core.proxy.AsyncRPCProxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:hrpc/spring-hrpc-client.xml")
public class ClientBaseTest {
	private static final Logger logger = LoggerFactory.getLogger(ClientBaseTest.class); 
	
	@Autowired
	private RPCClient rpcClient;
	
	@Test
	public void testInvokeBySync() throws IllegalArgumentException, IllegalAccessException{
		UserService userService = rpcClient.createProxy(UserService.class);
		User user = userService.getUser("13127636621");
		System.out.println("查询用户："+ObjectsUtil.stringhelper(user));
		
		User updateUser = userService.updateUser(user);
		System.out.println("更新用户："+ObjectsUtil.stringhelper(updateUser));
	}
	
	//异步测试
	@Test
	public void testInvokeByAsync(){
		AsyncRPCProxy asyncProxy = rpcClient.createAsyncProxy(UserService.class);
		logger.info("start invoke");
		
		RPCFuture rpcFuture = asyncProxy.call("getUser", new AsyncRPCCallback() {
			
			@Override
			public void success(Object result) {
				try {
					User user = (User)result;
					System.out.println("异步回调："+ObjectsUtil.stringhelper(user));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void fail(Exception e) {
				System.out.println("request exception :"+e.getMessage());
			}
		}, "13127636621");
		
		try {
			System.out.println("异步请求结果："+rpcFuture.get());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
	}
}
