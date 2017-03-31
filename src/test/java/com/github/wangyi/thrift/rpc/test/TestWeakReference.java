package com.github.wangyi.thrift.rpc.test;

import java.lang.ref.WeakReference;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-9
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：弱引用WeakReference
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class TestWeakReference {

	public static void main(String[] args) {
		Car car=new Car(220000,"silver");
		
		WeakReference<Car> weakCar = new WeakReference<Car>(car); //即使没有引用，也会被回收的，比如cache
		
		int i=0;
		while(true){
			System.out.println("here is the strong reference 'car' "+car.toString());//有引用指向car，所以不会被GC
			if(weakCar.get()!=null){
				//未被回收
				i++;
				System.out.println("Object is alive for "+i+" loops - "+weakCar);
			}else{
				//被回收了
				System.err.println("Object has been collected.");
				break;
			}
		}
	}
}
