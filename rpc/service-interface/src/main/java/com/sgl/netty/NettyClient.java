package com.sgl.netty;

import java.util.concurrent.CountDownLatch;

import com.sgl.rpcproxy.RpcConnector;
import com.sgl.rpcproxy.RpcFutrue;
import com.sgl.rpcproxy.RpcRequest;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class NettyClient {
	
	private RpcConnector nettyClientConnector;
	
	public NettyClient() {
		nettyClientConnector =  NettyClientConnector.getInstance();
	}
	
	public void stop() {
		nettyClientConnector.stop();
	}

	public Object connectAndGet(String host, int port, RpcRequest request) throws Exception {
		nettyClientConnector.connect(host, port);
		System.out.println("start to send request");
		return handleRpcRequest(request);
	}
	
	public Object handleRpcRequest(RpcRequest request) throws Exception {
		RpcFutrue futrue =  new RpcFutrue();
		nettyClientConnector.putRequest(request, futrue);
		CountDownLatch latch = new CountDownLatch(1);
		nettyClientConnector.getChannelHandler().writeAndFlush(request).addListener(
				new ChannelFutureListener() {
					
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						if(future.isSuccess()) {
							latch.countDown();
						}else {
							future.channel().close();
							System.out.println("close.....");
						}
						
					}
				});
		latch.await();
		return futrue.get();
	}
}
