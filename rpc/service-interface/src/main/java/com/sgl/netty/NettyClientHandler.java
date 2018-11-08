package com.sgl.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.sgl.rpcproxy.RpcFutrue;
import com.sgl.rpcproxy.RpcRequest;
import com.sgl.rpcproxy.RpcResponse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
@Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse>{
	
	private volatile Channel channel;
	private Map<String, RpcFutrue> futureMap = new ConcurrentHashMap<>();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		RpcRequest msg = new RpcRequest();
		msg.setMethodName("this is test");
		ctx.writeAndFlush(msg);
	}
   @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
       System.out.println("channelRegistered...");
    }
	   

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
		System.out.println("client recieve: " + msg);
		RpcFutrue futrue = futureMap.get(msg.getRequestId());
		futrue.setResponse(msg);
	}
	public Channel getChannel() {
		return channel;
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	cause.printStackTrace();
        ctx.close();
    }

	public Object handleRpcRequest(RpcRequest request) throws Exception {
		System.out.println(request);
		RpcFutrue futrue =  new RpcFutrue();
		futureMap.put(request.getRequestId(), futrue);
		channel.writeAndFlush(request);
		return futrue.get(1,TimeUnit.SECONDS);
	}

}
