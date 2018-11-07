package com.sgl.client;

import com.sgl.interfaces.HelloService;
import com.sgl.rpcproxy.RpcProxy;

public class ConsumenrApp {
	public static void main(String[] args) {
		RpcProxy proxy = new RpcProxy();
		HelloService service = (HelloService) proxy.getProxy(HelloService.class,
				"127.0.0.1", 9112);
		System.out.println(service.sayHello("baby coffe"));
	}
}
