package com.sgl.provider;

import com.sgl.impl.HelloServiceImpl;

public class ServerApp {
	static {
		HelloServiceImpl.register();
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
		System.out.println("server start at 9112");
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
