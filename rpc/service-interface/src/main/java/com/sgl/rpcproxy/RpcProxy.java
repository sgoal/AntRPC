package com.sgl.rpcproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.sgl.client.Client;
import com.sgl.netty.NettyClient;

public class RpcProxy {
	NettyClient client;
	public Object getProxy(Class clazz, String host, int port) {
		return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				RpcRequest request = new RpcRequest(clazz.getSimpleName(), method.getName(), 
						method.getParameterTypes(), args);
//				return Client.connectAndGet(host, port, request);
				if(client==null) {
					client = new NettyClient();
				}
				System.out.println("invoke method:"+method.getName());
				return client.connectAndGet(host, port, request);
			}
		});
	}
}
