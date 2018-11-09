package com.sgl.client;

import com.sgl.interfaces.HelloService;
import com.sgl.rpcproxy.RpcProxy;

public class ConsumenrApp {
	public static void main(String[] args) throws Exception {
		RpcProxy proxy = new RpcProxy();
		HelloService service = (HelloService) proxy.getProxy(HelloService.class,
				"localhost", 9112);
		System.out.println("create proxy");
		System.out.println(service.sayHello("baby coffe"));
		Thread.sleep(10);
		System.out.println("end....");
	}
}
