package com.github.wangyi.hetty.test.demo;

public class HelloImpl implements Hello{

	@Override
	public String hello() {
		System.out.println("revision 2");
		return "revision 2";
	}

	@Override
	public String hello(String name) {
		// TODO Auto-generated method stub
		System.out.println("revision 2："+name);
		return "hetty测试--hello--"+name;
	}

	@Override
	public String hello(String name1, String name2) {
		// TODO Auto-generated method stub
		System.out.println("revision 2");
		return "revision 2";
	}

	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		System.out.println("revision 2");
		return null;
	}

	@Override
	public String getAppSecret(String key) {
		// TODO Auto-generated method stub
		System.out.println("revision 2");
		return "revision 2";
	}
}
