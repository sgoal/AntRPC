package com.sgl.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.sgl.rpcproxy.RpcFutrue;
import com.sgl.rpcproxy.RpcRequest;

public class NettyClient {
	
	private NettyClientConnector nettyClientConnector;
	private Map<String, RpcFutrue> futureMap = new ConcurrentHashMap<>();
	
	public NettyClient() {
		nettyClientConnector = new NettyClientConnector();
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
		futureMap.put(request.getRequestId(), futrue);
		nettyClientConnector.getChannelHandler().writeAndFlush(request);
		return futrue.get(1,TimeUnit.SECONDS);
	}
}
