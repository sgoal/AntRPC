package com.sgl.client;

import com.sgl.interfaces.HelloService;
import com.sgl.netty.NettyClient;

public class ConsumenrApp {
	public static void main(String[] args) throws Exception {
		NettyClient client = new NettyClient();
		client.connect("localhost", 9112);
		HelloService service = client.createProxy(HelloService.class);
		System.out.println("create proxy");
		System.out.println(service.sayHello("baby coffe"));
		Thread.sleep(10);
		System.out.println("end....");
	}
}
