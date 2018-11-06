package com.sgl.netty;

import com.sgl.rpcproxy.RpcRequest;
import com.sgl.rpcproxy.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse>{
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel active...");
		for(int i=0;i<10;i++) {
			RpcRequest rpcRequest = new RpcRequest();
			rpcRequest.setMethodName("test" + i);
			ctx.writeAndFlush(rpcRequest);
			
		}
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
		System.out.println("client recieve: " + msg);
		
	}
	
}
