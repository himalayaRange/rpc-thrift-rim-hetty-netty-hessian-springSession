package com.github.wangyi.hrpc.hrpc_api.service;

import java.util.List;

import com.github.wangyi.hrpc.hrpc_api.entity.Order;

public interface OrderService {

	 public Order getOrder(String uuid);

	 public List<Order> getOrderList(long userId);
}
