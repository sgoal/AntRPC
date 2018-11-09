package com.sgl.rpcproxy;

import io.netty.channel.Channel;

public interface RpcConnector {
	
	Channel getChannelHandler() throws Exception;
	
}
