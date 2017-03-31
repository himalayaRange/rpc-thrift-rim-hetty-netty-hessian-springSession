package com.github.wangyi.hrpc.hrpc_server.publish;

import java.util.Date;
import com.github.wangyi.hrpc.hrpc_api.entity.JD;
import com.github.wangyi.hrpc.hrpc_api.service.JDService;
import com.github.wangyi.hrpc.hrpc_core.annotation.HRPCService;
@HRPCService(JDService.class)
public class JDServiceImpl implements JDService{

	@Override
	public JD insert(JD order) {
		return order;
	}

	@Override
	public String delete(String stid) {
		return "return:"+stid;
	}

	@Override
	public JD update(JD order) {
		order.setStid("update:"+order.getStid());
		return order;
	}

	@Override
	public JD query(String stid) {
		JD order = new JD();
		order.setStid("查询的商品STID："+stid);
		order.setName("三星盖乐世S6");
		order.setPrice(5000.00);
		order.setCreateTime(new Date());
		return order;
	}

	@Override
	public JD sysInit(JD order, String stid) {
		order.setStid(stid);
		return order;
	}

}
