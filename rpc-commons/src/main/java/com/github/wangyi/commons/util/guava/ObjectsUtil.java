package com.github.wangyi.commons.util.guava;

import java.lang.reflect.Field;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * 替换jdk7中的Objects.equals
 * @author wangyi
 * @date 2016-8-1 下午3:52:48 
 */
@SuppressWarnings("deprecation")
public class ObjectsUtil {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		equals();
		hashcode("ccc", "bbb");
		Person person = new Person();
		person.setUsername("汪谊");
		person.setPassword("123456");
		person.setAddress(null);
		String result = stringhelper(person);
		System.out.println(result);
		
	}
	
	/**
	 * Objects.equal中可以不用考虑参数是否为null
	 * 
	 * @Description
	 */
	public static void equals(){
		System.out.println(Objects.equal("a","b"));
		System.out.println(Objects.equal(null,"b"));
		System.out.println(Objects.equal("a",null));
		System.out.println(Objects.equal(null,null));
	}
	
	/**
	 * 散射值
	 * @Description
	 */
	public static void hashcode(String filed1,String filed2){
		int hashCode = Objects.hashCode(filed1);
		System.out.println(hashCode);
	}
	
	/**
	 * 通过反射和guava封装的toString方法
	 * stringHelper
	 * @param <T>
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @Description
	 */
	public static <T> String stringhelper(T t) throws IllegalArgumentException, IllegalAccessException{
		if(t==null){
			return null;
		}
		 Class<? extends Object> temp = t.getClass();
		 Field[] fields = temp.getDeclaredFields();
		 ToStringHelper result= Objects.toStringHelper(t) ;
		 for(Field filed:fields){
			filed.setAccessible(true);
			String parm_name = filed.getName();
			Object value=filed.get(t);
			result=result.add(parm_name, value);
		}
		return result.toString();
	}
}
