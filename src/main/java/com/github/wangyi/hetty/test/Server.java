package com.github.wangyi.hetty.test;

import com.github.wangyi.hetty.core.Hetty;

public class Server {
	public static void main(String[] args) {
		new Hetty("hetty/server.properties").start();
	}
}
