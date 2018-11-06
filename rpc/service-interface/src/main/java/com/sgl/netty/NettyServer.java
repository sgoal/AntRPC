package com.sgl.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyServer {
	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, bossGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline()
							.addLast(MarshallingCodeCFactory.buildingMarshallingEncoder());
							ch.pipeline()
							.addLast(MarshallingCodeCFactory.buildingMarshallingDecoder());
							ch.pipeline().addLast(new NettyServerHandler());
							
						}
			});
			
			ChannelFuture future = bootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
			
		} finally {
			// TODO: handle finally clause
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
