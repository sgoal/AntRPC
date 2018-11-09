package com.sgl.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.sgl.rpcproxy.RpcConnector;
import com.sgl.rpcproxy.RpcFutrue;
import com.sgl.rpcproxy.RpcRequest;
import com.sgl.rpcproxy.RpcResponse;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class NettyClientConnector implements RpcConnector{

	private Map<String, RpcFutrue> futureMap = new ConcurrentHashMap<>();
	private volatile Bootstrap bootstrap;
	private NettyClientHandler handler;
	private EventLoopGroup group;
	private CountDownLatch latch = new CountDownLatch(1);
	
	private NettyClientConnector(){};
	
	private static class InstanceHandler{
		private static final RpcConnector instance = new NettyClientConnector(); 
	}
	
	public static RpcConnector getInstance() {
		return InstanceHandler.instance;
	}
	
	@Override
	public Channel getChannelHandler() throws Exception {
		latch.await();
		return handler.getChannel();
	}
	
	public void connect(String host, int port) throws Exception {
		group = new NioEventLoopGroup();
		try {
			handler = new NettyClientHandler();
			bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
					.handler(new LoggingHandler(LogLevel.INFO))
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
//							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingEncoder());
//							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingDecoder());
							ch.pipeline().addLast(new ObjectDecoder(1024*1024,ClassResolvers.cacheDisabled(getClass().getClassLoader())));
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(handler);

						}
					});

			ChannelFuture future = bootstrap.connect(host, port).sync();
			System.out.println("connected....");
			future.addListener(new FutureListener() {
				@Override
				public void operationComplete(Future future) throws Exception {
					latch.countDown();
				}
			});
			//will block
//			future.channel().closeFuture().sync();
		} finally {
			// TODO: handle finally clause
			//bug 会突然结束，然后没法接收消息
//			group.shutdownGracefully();
		}
	}
	
	
	
	public void stop() {
		group.shutdownGracefully();
	}

	@Override
	public void putRequest(RpcRequest request, RpcFutrue futrue) {
		futureMap.put(request.getRequestId(), futrue);
	}

	@Override
	public void putResponse(RpcResponse response) {
		futureMap.get(response.getRequestId()).setResponse(response);
		
	}
}
