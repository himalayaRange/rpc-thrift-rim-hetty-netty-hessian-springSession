package com.github.wangyi.commons.util.guava;

import com.google.common.base.Throwables;

/**
 *多重异常的抛出
 * @author wangyi
 * @date 2016-8-1 下午5:04:05 
 */
public class ThrowablesUtil {

	public static void main(String[] args) {
		try {
			throwException();
		} catch (NullPointerException e) {
			System.err.println("空指针异常....");
		}catch (ArithmeticException e) {
			System.err.println("分母为0异常");
		}catch(Exception e){
			System.err.println("系统异常");
		}
	}
	
	
	/**
	 * jdk7中通过类似RuntimeException | Error e的形式对多重异常进行抛出
	 * 建议使用Guava中异常机制
	 * 模型
	 * try {
			    someMethodThatCouldThrowAnything();
			} catch (IKnowWhatToDoWithThisException e) {
			    handle(e);
			} catch (Throwable t) {
			    Throwables.propagateIfInstanceOf(t, IOException.class);
			    Throwables.propagateIfInstanceOf(t, SQLException.class);
			    throw Throwables.propagate(t);
			}
	 * @Description
	 */
	public static void throwException(){
		try{
			Person p=null;
			p.getUsername();
			int a=2/0;
		}catch(Throwable t){
			//所有可能的异常全部在此处进行抛出，在栈顶进行处理
			Throwables.propagateIfInstanceOf(t, NullPointerException.class);
			Throwables.propagateIfInstanceOf(t, ArithmeticException.class);
			throw Throwables.propagate(t);
		}finally{
			System.out.println("释放资源....");
		}
	}
}
