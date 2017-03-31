package com.github.wangyi.hetty.object;

import java.io.Serializable;
/**
 * 
 * ClassName: Application  
 * Function: 应用
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-10 下午5:32:28 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class Application implements Serializable{

	private static final long serialVersionUID = -2243389057287205923L;

	private String user;
	
	private String password;
	
	public Application(){
		
	}
	public Application(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
