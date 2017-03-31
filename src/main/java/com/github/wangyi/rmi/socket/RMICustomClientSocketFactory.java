package com.github.wangyi.rmi.socket;

import java.io.IOException;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
/**
 * 
 * ========================================================
 * 日 期：@2017-1-23
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： 自定一个SCOKECT连接，可配置超时时间 
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RMICustomClientSocketFactory implements RMIClientSocketFactory{

	private int timeout;
	
	@Override
	public Socket createSocket(String host, int port) throws IOException {
		Socket socket=new Socket(host, port);
		socket.setSoTimeout(timeout);
		return socket;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	
}
