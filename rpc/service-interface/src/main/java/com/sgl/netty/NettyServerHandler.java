package com.sgl.netty;

import java.lang.reflect.Method;

import com.sgl.provider.InterfaceManager;
import com.sgl.rpcproxy.RpcRequest;
import com.sgl.rpcproxy.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class NettyServerHandler  extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcRequest request = (RpcRequest)msg;
		System.out.println("read: "+ request.getInterfaceName());
		Class implClass = InterfaceManager.getInstance().findClass(request.getInterfaceName());
		
		Method method =  implClass.getMethod(request.getMethodName(), request.getParameterTypes());
		Object res =  method.invoke(implClass.newInstance(), request.getArgs());
		
		RpcResponse response = new RpcResponse();
		response.setResult(res);
		response.setRequestId(request.getRequestId());
		response.setMethodNname(request.getMethodName());
		ctx.writeAndFlush(response);
	}
}
