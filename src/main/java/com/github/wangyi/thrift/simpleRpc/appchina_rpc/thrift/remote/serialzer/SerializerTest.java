
package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Order;

public class SerializerTest {
	
public static void main(String[] args) throws Exception {
	List<Order> list = new ArrayList<Order>();
	for(int i=0;i<10;i++){
		Order req=new Order();
		req.setStid("88888"+i);
		req.setName("三星盖乐世S7 edge"+i);
		req.setPrice(6888.00);
		req.setCreateTime(new Date());
		list.add(req);
	}
	long start=System.currentTimeMillis();
	for(int i=0;i<20000;i++){
		//Hessian2Serializer h2 = new Hessian2Serializer(); //603
		//HessianSerializer h2 = new HessianSerializer(); 2559 hessian对list的效果不是太好
		//KryoSerializer h2 = new KryoSerializer(); //829ms
		//JavaSerializer h2 = new JavaSerializer(); 1193ms 
		//FSTSerializer h2=new FSTSerializer(); //530ms
		FastjsonSerializer h2=new FastjsonSerializer(); //680ms
		//ProtobufSerializer h2 = new ProtobufSerializer();
		byte[] bytes = h2.getBytes(list);
		
		Object[] orders=(Object[]) h2.getObject(bytes,Object.class);
		for(int j=0;j<orders.length;j++){
			System.out.println(ObjectsUtil.stringhelper(orders[j]));
		}
	}
		long end=System.currentTimeMillis();
		System.out.println("序列化耗时:"+(end-start));
}
}
