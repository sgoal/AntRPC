package com.sgl.provider;

import com.sgl.impl.HelloServiceImpl;
import com.sgl.netty.NettyServer;

public class ServerApp {
	static {
		HelloServiceImpl.register();
	}
	
	public static void main(String[] args) throws Exception {
//		Server server = new Server();
//		server.start();
		NettyServer server = new NettyServer();
		server.bind(9112);
		System.out.println("server start at 9112");
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
