package com.sgl.rpcproxy;

import io.netty.channel.Channel;

public interface RpcConnector {
	
	public Channel getChannelHandler() throws Exception;
	
	public void stop();
	
	public void putRequest(RpcRequest request, RpcFutrue futrue);
	
	public void putResponse(RpcResponse response);
	
	public void connect(String host, int port) throws Exception;
}
