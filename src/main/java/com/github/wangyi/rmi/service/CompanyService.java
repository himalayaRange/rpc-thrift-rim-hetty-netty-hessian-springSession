package com.github.wangyi.rmi.service;

import java.util.List;

import com.github.wangyi.rmi.entity.Company;

/**
 * 
 * ========================================================
 * 日 期：@2017-1-22
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：公司服务
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */

public interface CompanyService {

	/**
	 * 注册公司
	 * @param company
	 * @return
	 */
	public Company registCompany(Company company);
	
	/**
	 * 移除公司
	 * @param uid
	 * @return
	 */
	public Integer removeCompany(String uid);

	/**
	 * 修改公司
	 * @param company
	 * @return
	 */
	public Company modifyCompany(Company company);
	
	/**
	 *查询公司
	 * @param company
	 * @return
	 */
	public List<Company> queryCompany(Company company);
	
}
