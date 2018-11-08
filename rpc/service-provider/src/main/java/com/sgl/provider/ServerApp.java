package com.sgl.provider;

import com.sgl.impl.HelloServiceImpl;
import com.sgl.netty.NettyServer;

import netty.test.marshalling.SubReqServer;

public class ServerApp {
	static {
		HelloServiceImpl.register();
	}
	
	public static void main(String[] args) throws Exception {
//		Server server = new Server();
//		server.start();
		NettyServer server = new NettyServer();
		System.out.println("server start at 9112");
		server.bind(9112);

		Thread.currentThread().join();
		System.out.println("close.....");
	}
}
