package com.sgl.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sgl.rpcproxy.RpcRequest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.test.marshalling.SubReqClient;
import netty.test.marshalling.SubReqClientHandler;

public class NettyClient {
	private volatile Bootstrap bootstrap;
	private Map<String, Channel> ipToChannels = new ConcurrentHashMap<>();
	private NettyClientHandler handler;
	
	public void connect(String host, int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
//			handler = new NettyClientHandler();
//			bootstrap = new Bootstrap();
//			bootstrap.group(group).channel(NioSocketChannel.class)
//			.option(ChannelOption.TCP_NODELAY, true)
//					.handler(new LoggingHandler(LogLevel.INFO))
//					.handler(new ChannelInitializer<SocketChannel>() {
//
//						@Override
//						protected void initChannel(SocketChannel ch) throws Exception {
//							// TODO Auto-generated method stub
//							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingEncoder());
//							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingDecoder());
////							ch.pipeline().addLast(handler);
//							ch.pipeline().addLast(new SubReqClientHandler());
//						}
//					});
//
//			ChannelFuture future = bootstrap.connect(host, port).sync();
//			System.out.println("connect.....");
//			future.channel().closeFuture().sync();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
					.handler(new LoggingHandler(LogLevel.INFO))
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingEncoder());
							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingDecoder());
							ch.pipeline().addLast(new SubReqClientHandler());

						}
					});

			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally {
			// TODO: handle finally clause
			group.shutdownGracefully();
		}
	}
	
	public Object connectAndGet(String host, int port, RpcRequest request) throws Exception {
		if(bootstrap == null) {
//			connect(host, port);
		}
		return null;//handler.handleRpcRequest(request);
	}
	public static void main(String[] args) throws Exception {
		new SubReqClient().connect("127.0.0.1", 9112);
	}
}
