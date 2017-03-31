package com.github.wangyi.thrift.rpc.monitor.statistic;

import com.github.wangyi.thrift.rpc.constant.SystemConfig;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-24
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：监听器服务接口
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface Monitor {

	String[] TYPES = {"success","error","concurrent"};
	
	String SUCCESS = "success";
	
	String ERROR = "error";
	
	String CONCURRENT = "concurrent";
	
	String PROVIDER = "provider";
	
	//统计生成的文件目录
	String STATISTICS_DIRECTORY = SystemConfig.STATISTICS_DIRECTORY;
	
	//统计绘图路径
	String CHARTS_DIRECTORY = SystemConfig.CHARTS_DIRECTORY;
	
}
