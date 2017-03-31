package com.github.wangyi.thrift.rpc.pojo.resource;

import com.github.wangyi.thrift.rpc.pojo.Order;
/**
 * 
 * ========================================================
 * 日 期：@2016-11-30
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：用来生成Thrift接口原接口
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface HelloWorldIface {


    public String sayHello(String username);

    public Order sendOrder(Order order);
    

}
