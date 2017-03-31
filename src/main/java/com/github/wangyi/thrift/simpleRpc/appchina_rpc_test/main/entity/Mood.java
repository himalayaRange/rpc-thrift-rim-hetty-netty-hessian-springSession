package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Mood implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id=UUID.randomUUID().toString();
	
	private Integer age=18;
	
	private String[] names =new String[]{"张三", "李四", "王五"} ;
	
	private List<String> values= new LinkedList<String>();
	
	public Mood(){
		super();
		values.add("1111111111");
		values.add("1111111111");
		values.add("1111111111");
		values.add("1111111111");
		values.add("1111111111");
		values.add("1111111111");
		values.add("1111111111");
		values.add("1111111111");
		values.add("1111111111");
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Mood [id=" + id + ", age=" + age + ", names=" + Arrays.toString(names) + ", values=" + values + "]";
	}

}
