package com.github.wangyi.commons.util.guava;

import com.google.common.base.Preconditions;

/**
 * 检查字段的状态Preconditions类
 * @author wangyi
 * @date 2016-7-29 下午5:49:44 
 */
public class PreconditionsUtil {
	
	public static void main(String[] args) {
		check(true, "this is test");
		check(true, null);
	}
	
	/**
	 * checkArgument(boolean) 检查类型是否正确
	 * checkNotNull(T) 检查value是否为null，该方法直接返回value，因此可以内嵌使用checkNotNull。
	 * checkState(boolean) 检查属性的状态
	 */
	public static String check(boolean b,String value){
		Preconditions.checkArgument(b);
		Preconditions.checkState(b);
		try {
			//空的时候赋值异常
			String checkNotNull = Preconditions.checkNotNull(value,"空指针异常");
			System.out.println(checkNotNull);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
