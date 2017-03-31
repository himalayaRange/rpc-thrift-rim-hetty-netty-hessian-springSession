package com.github.wangyi.hetty.object;
/**
 * 
 * ClassName: HettyException  
 * Function: Wrapper for protocol exceptions thrown in the proxy.
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-10 下午5:34:42 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class HettyException extends RuntimeException {

	private static final long serialVersionUID = -3389027130589584744L;

	private Throwable rootCase;
	
	public HettyException(){
		
	}
	
	public HettyException(String emsg){
		super(emsg);
	}

	public HettyException(String emsg,Throwable rootCase){
		super(emsg);
		this.rootCase=rootCase;
	}
	
	public HettyException(Throwable rootCase){
		super(String.valueOf(rootCase));
		this.rootCase=rootCase;
	}
	
	public Throwable getRootCase(){
		return this.rootCase;
	}
	
	public Throwable getCase(){
		return getRootCase();
	}
	
}
