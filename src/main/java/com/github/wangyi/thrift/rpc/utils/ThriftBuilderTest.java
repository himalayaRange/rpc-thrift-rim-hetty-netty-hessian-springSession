package com.github.wangyi.thrift.rpc.utils;

import org.junit.Test;
import com.github.wangyi.thrift.rpc.pojo.resource.HelloWorldIface;
import com.github.wangyi.thrift.rpc.utils.generator.builder.ThriftFileBuilder;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-30
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：通过接口生成对应的Thrift文件
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftBuilderTest {
private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
	
	@Test
	public void toOutputstream() throws Exception {
		//this.fileBuilder.buildToOutputStream(ICommonUserService.class, System.out);
		this.fileBuilder.buildToOutputStream(HelloWorldIface.class, System.out);
	}
}
