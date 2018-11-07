package com.sgl.netty;

import java.lang.reflect.Method;

import com.sgl.provider.InterfaceManager;
import com.sgl.rpcproxy.RpcRequest;
import com.sgl.rpcproxy.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class NettyServerHandler  extends ChannelInboundHandlerAdapter{
	
	NettyServer  server;
	
	public NettyServerHandler(NettyServer  server) {
		this.server = server;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//永远不要在 Netty 的 I/O 线程上执行任何非 CPU 限定的代码——你将会从 Netty 偷取宝贵的资源，并因此影响到服务器的吞吐量。
		
		Runnable task = new Runnable() {

			@Override
			public void run() {
				RpcRequest request = (RpcRequest)msg;
				System.out.println("read: "+ request.getInterfaceName());
				Class implClass = InterfaceManager.getInstance().findClass(request.getInterfaceName());
				
				Method method;
				try {
					method = implClass.getMethod(request.getMethodName(), request.getParameterTypes());
					Object res =  method.invoke(implClass.newInstance(), request.getArgs());
					
					RpcResponse response = new RpcResponse();
					response.setResult(res);
					response.setRequestId(request.getRequestId());
					response.setMethodNname(request.getMethodName());
					ctx.writeAndFlush(response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		};
		
		server.submitTask(task);
	}
}
