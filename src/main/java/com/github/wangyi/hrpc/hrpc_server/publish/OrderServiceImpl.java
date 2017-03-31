package com.github.wangyi.hrpc.hrpc_server.publish;

import java.util.ArrayList;
import java.util.List;
import com.github.wangyi.hrpc.hrpc_api.entity.Order;
import com.github.wangyi.hrpc.hrpc_api.service.OrderService;
import com.github.wangyi.hrpc.hrpc_core.annotation.HRPCService;

@HRPCService(OrderService.class)
public class OrderServiceImpl implements OrderService{

	@Override
	public Order getOrder(String uuid) {
		//方便做负载均衡测试，这里做个数据区分
        Order order = new Order(uuid, 111, "service1", 15.2F);
        return order;
	}

	@Override
	public List<Order> getOrderList(long userId) {
		List<Order> list = new ArrayList<Order>();
        for (int i = 0; i < 20; i++) {
            Order order = new Order("uuid@" + i, 111, "service1@" + i, i + 0.1F);
            list.add(order);
        }
        return list;
	}

}
