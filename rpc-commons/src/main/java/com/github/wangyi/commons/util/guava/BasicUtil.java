package com.github.wangyi.commons.util.guava;

import com.google.common.base.Optional;

/**
 * 是否为空返回工具类
 * @author wangyi
 * @date 2016-7-29 下午5:03:11 
 */
public class BasicUtil {
	
	/**
	 * 对null值得处理不进行异常处理
	 * 使用Optional除了赋予null语义，增加了可读性，最大的优点在于它是一种傻瓜式的防护
	 * @Description
	 * @param arge
	 */
	public static void main(String arge[]) {
		//创建指定引用的Optional实例，若引用为null则默认为值缺失
		Optional<Integer> p=Optional.fromNullable(null);
		Integer or = p.or(-1);//返回Optional所包含的引用，若引用缺失，返回指定的值为-1
		System.out.println(or);  
		
		Optional<Integer> pp=Optional.fromNullable(null);
		Integer orNull = pp.orNull(); // 返回Optional所包含的引用，若引用缺失，返回null
		System.out.println(orNull);
		
		
		Integer result = (Integer) Optional.fromNullable(null).or(-1);
		System.out.println(result);
	
	}
}
