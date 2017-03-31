package com.github.wangyi.thrift.rpc.monitor.statistic;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-24
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：数据可视化接口
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface VisualService extends Monitor {

	/**
	 * 绘制表格
	 */
	public void draw();

}
