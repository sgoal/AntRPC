package com.sgl.rpcproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.sgl.netty.NettyClient;

public class RpcProxy {
	NettyClient client;
	
	public RpcProxy() {
		client = new NettyClient();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				RpcRequest request = new RpcRequest(clazz.getSimpleName(), method.getName(), 
						method.getParameterTypes(), args);

				if(client==null) {
					client = new NettyClient();
				}
				System.out.println("invoke method:"+method.getName());
				return client.connectAndGet(request);
			}
		});
	}
	
	public static <T> RpcFutrue call(Class<T> clazz,String methodName,
			NettyClient client,Object[] args) throws Exception {
		RpcRequest request = new RpcRequest(clazz.getSimpleName(), methodName, 
				getArgsTypes(args), args);
		return client.handleRpcRequest(request);
	}
	
	public static Class[] getArgsTypes(Object[] args) {
		Class[] classes = new Class[args.length];
		for(int i=0;i< args.length;i++) {
			classes[i] = args[i].getClass();
		}
		return classes;
	}
}
