package com.sgl.client;

import com.sgl.interfaces.HelloService;
import com.sgl.netty.NettyClient;
import com.sgl.rpcproxy.RpcProxy;

import netty.test.marshalling.SubReqClient;

public class ConsumenrApp {
	public static void main(String[] args) throws Exception {
//		new NettyClient().connect("127.0.0.1", 9112);
		new SubReqClient().connect("127.0.0.1", 9112);
//		RpcProxy proxy = new RpcProxy();
//		HelloService service = (HelloService) proxy.getProxy(HelloService.class,
//				"127.0.0.1", 9112);
//		System.out.println(service.sayHello("baby coffe"));
	}
}
