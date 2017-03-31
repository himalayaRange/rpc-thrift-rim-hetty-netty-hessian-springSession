package com.github.wangyi.rmi.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.github.wangyi.rmi.entity.Company;
import com.github.wangyi.rmi.service.CompanyService;

public class CompanySupport implements CompanyService {

	@Override
	public Company registCompany(Company company) {

		return company;
	}

	@Override
	public Integer removeCompany(String uid) {

		return Integer.valueOf(uid);
	}

	@Override
	public Company modifyCompany(Company company) {
		company.setUid("修改的ID");
		return company;
	}

	@Override
	public List<Company> queryCompany(Company company) {
		List<Company> list=new ArrayList<Company>();
		for(int i=0;i<10;i++){
			Company item =new Company();
			item.setUid(UUID.randomUUID().toString());
			item.setName("中亿国星集团投资股份有限公司");
			item.setAddress("上海市闵行区古北路1699号");
			item.setCount(1200);
			item.setCreate_date(new Date());
			list.add(item);
		}
		return list;
	}

}
