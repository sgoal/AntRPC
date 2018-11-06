package com.sgl.netty;

import com.sgl.rpcproxy.RpcRequest;
import com.sgl.rpcproxy.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class NettyServerHandler  extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcRequest request = (RpcRequest)msg;
		System.out.println("read: "+ request.getMethodName());
		RpcResponse response = new RpcResponse();
//		response.setMehtodName(request.getMethodName()+"lalalala");
		ctx.writeAndFlush(response);
//		super.channelRead(ctx, msg);
	}
}
