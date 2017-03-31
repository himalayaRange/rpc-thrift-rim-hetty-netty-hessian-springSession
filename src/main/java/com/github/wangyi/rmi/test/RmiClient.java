package com.github.wangyi.rmi.test;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.rmi.entity.Company;
import com.github.wangyi.rmi.service.CompanyService;

/**
 * 
 * ========================================================
 * 日 期：@2017-1-22
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：RMI客户端
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RmiClient {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		final ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("rmi/spring-rmi-customer.xml");
		CompanyService companyService = (CompanyService)ctx.getBean("companyServiceClient");
		Company company =new Company();
		company.setUid(UUID.randomUUID().toString());
		company.setName("中亿国星集团投资股份有限公司");
		company.setAddress("上海市闵行区古北路1699号");
		company.setCount(1200);
		company.setCreate_date(new Date());
		List<Company> queryCompany = companyService.queryCompany(company);
		for(Company item:queryCompany){
			System.out.println(ObjectsUtil.stringhelper(item));
		}
		
	}
}
