package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.cluster;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：负载均衡接口，提供负载均衡的方式
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface LoadBalance {

	/**
	 * 给出一个总数，在0-count-1之间进行均衡
	 * @param count
	 */
	void allocat(int count);

	/**
	 * 设置指定位置无效，将不再被使用
	 * @param index
	 */
	void invalid(int index);
	
	/**
	 * 恢复指定位置有效，继续使用
	 * @param index
	 */
	void avaliable(int index);

	/**
	 * 负载均衡的方法，获取下一个使用的位置
	 * @return
	 */
	int nextIndex();
	
	/**
	 * 判断指定位置是否被使用
	 * @param index
	 * @return
	 */
	boolean isAvaliable(int index);

	/**
	 * 获取已分配的数量
	 * @return
	 */
	int getCount();
	
	/**
	 * 获取所有不可用的位置，数组内每个元素表示位置
	 * @return
	 */
	int[] getAllInvalid();
	
	
}
