package com.sgl.netty;

import com.sgl.rpcproxy.RpcConnector;
import com.sgl.rpcproxy.RpcFutrue;
import com.sgl.rpcproxy.RpcRequest;

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
		nettyClientConnector.getChannelHandler().writeAndFlush(request);
		return futrue.get();
	}
}
