package com.github.wangyi.thrift.simpleRpc.appchina_rpc.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.Serializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.ProtobufSerializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Order;


public class ProtostuffTest {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Order request=new Order();
		request.setStid("88888");
		request.setName("三星盖乐世S7 edge");
		request.setPrice(6888.00);
		request.setCreateTime(new Date());
		
		Order request1=new Order();
		request1.setStid("88888");
		request1.setName("三星盖乐世S7 edge");
		request1.setPrice(6888.00);
		request1.setCreateTime(new Date());
		
		Order[] parm = new Order[1];
		parm[0]=request;
		
		List<Order> list=new ArrayList<Order>();
		list.add(request);
		list.add(request1);
		
		Map<String,Order> map=new HashMap<String,Order>();
		map.put("item1", request);
		map.put("item2", request1);
		
		/*byte[] serializer = ProtostuffUtil.serialize(request);
		Object ret = ProtostuffUtil.deserialize(serializer, Order.class);
		System.out.println(ret.toString());
		
		byte[] serializer2 = ProtostuffUtil.serialize(map);
		Map<String,Order> deserializeMap =ProtostuffUtil.deserialize(serializer2, map.getClass());
		Set<Entry<String,Order>> entrySet = deserializeMap.entrySet();
		Iterator<Entry<String, Order>> iterator = entrySet.iterator();
		while(iterator.hasNext()){
			Entry<String,Order> entry = (Entry<String,Order>) iterator.next();
			String key = entry.getKey();
			Order value=entry.getValue();
			System.out.println("key:"+key+"==value:"+value.toString());
		}*/
		
		/*byte[] serializer1 = ProtostuffUtil.serializeList(list);
		List<Order> deserializeList = ProtostuffUtil.deserializeList(serializer1, Order.class);
		System.out.println(deserializeList.toString());*/
	
		Serializer protobufSerializer = new ProtobufSerializer();
		try {
			byte[] data = protobufSerializer.getBytes(request);
			Order myOrder =protobufSerializer.getObject(data,Order.class);
			System.out.println(ObjectsUtil.stringhelper(myOrder));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}