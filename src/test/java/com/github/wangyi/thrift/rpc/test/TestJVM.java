package com.github.wangyi.thrift.rpc.test;

public class TestJVM {
	public static void main(String[] args) {
		new Thread(new Runnable() {
			
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				while(true){
					long maxMemory = Runtime.getRuntime().maxMemory()/1024/1024;
					
					long totalMemory = Runtime.getRuntime().totalMemory()/1024/1024;
					
					long freeMemory = Runtime.getRuntime().freeMemory()/1024/1024;
					
					System.out.println(maxMemory+"M---"+totalMemory+"M---"+freeMemory+"M");

					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		while(true){
			try {
				A a= new A();
				B b= new B(a);
				a=null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class A{
	
}

class B{
	
	private A a;
	
	public B(){
		
	}
	
	public B(A a){
		this.a=a;
	}
}

