package com.sgl.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

public class NettyClient {
	private Bootstrap bootstrap;
	private Map<String, Channel> ipToChannels = new ConcurrentHashMap<>();
	public void connect(String host, int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
					.handler(new LoggingHandler(LogLevel.INFO))
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingEncoder());
							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingDecoder());
							ch.pipeline().addLast(new NettyClientHandler());

						}
					});

			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.channel().closeFuture().sync();

		} finally {
			// TODO: handle finally clause
			group.shutdownGracefully();
		}
	}
	
	public void connectAndGet() {
		
		
	}
}
