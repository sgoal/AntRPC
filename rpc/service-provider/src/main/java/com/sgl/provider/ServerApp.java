package com.sgl.provider;

public class ServerApp {
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
