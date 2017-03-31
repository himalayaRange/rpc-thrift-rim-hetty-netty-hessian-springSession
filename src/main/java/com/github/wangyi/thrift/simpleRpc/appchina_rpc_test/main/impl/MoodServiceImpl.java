package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api.MoodService;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.GENDER;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Mood;

public class MoodServiceImpl implements MoodService{

	@Override
	public void test() {
		
	}

	@Override
	public Map<String, Mood> test(Integer value) {
		Map<String,Mood> map = new HashMap<String,Mood>();
		map.put("mood", new Mood());
		return map;
	}

	@Override
	public Integer test(String value) {
		return Integer.valueOf(value);
	}

	@Override
	public String test(Integer value, String value1) {
		return new StringBuilder(value1).append("====").append(value1).toString();
	}

	@Override
	public Mood test(Mood value) {
		return new Mood();
	}

	@Override
	public List<Mood> test(List<Mood> value) {
		List<Mood> moods = new ArrayList<Mood>();
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		moods.add(new Mood());
		return moods;
	}

	@Override
	public Mood[] test(Mood[] value) {
		return new Mood[]{new Mood(), new Mood(), new Mood()};
	}

	@Override
	public int[] test(int[] value) {
		return new int[]{1, 2, 3, 4, 5};
	}

	@Override
	public int test(int value1, int value2) {
		return value1 + value2;
	}

	@Override
	public GENDER test(GENDER value) {
		return GENDER.M;
	}

}
