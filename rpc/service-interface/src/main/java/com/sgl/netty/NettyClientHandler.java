package com.sgl.netty;

import com.sgl.rpcproxy.RpcResponse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
@Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse>{
	
	private volatile Channel channel;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
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
		NettyClientConnector.getInstance().putResponse(msg);
	}
	public Channel getChannel() {
		return channel;
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	cause.printStackTrace();
        ctx.close();
    }
    
    

}
