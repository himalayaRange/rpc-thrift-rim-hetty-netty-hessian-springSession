package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api;

import java.util.List;
import java.util.Map;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.SPI;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.GENDER;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Mood;

public interface MoodService {

	@SPI
	public void test();
	@SPI
	public Map<String,Mood> test(Integer value);
	@SPI
	public Integer test(String value);
	@SPI
	public String test(Integer value, String value1);
	@SPI
	public Mood test(Mood value);
	@SPI
	public List<Mood> test(List<Mood> value);
	@SPI
	public Mood[] test(Mood[] value);
	@SPI
	public int[] test(int[] value);
	@SPI
	public int test(int value1, int value2);
	@SPI
	public GENDER test(GENDER value);
}
