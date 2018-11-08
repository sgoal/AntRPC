package com.sgl.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

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
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.test.marshalling.SubReqClient;
import netty.test.marshalling.SubReqClientHandler;

public class NettyClient {
	private volatile Bootstrap bootstrap;
	private Map<String, Channel> ipToChannels = new ConcurrentHashMap<>();
	private NettyClientHandler handler;
	private EventLoopGroup group;
	public void connect(String host, int port) throws Exception {
		group = new NioEventLoopGroup();
		try {
			handler = new NettyClientHandler();
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
//							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingEncoder());
//							ch.pipeline().addLast(MarshallingCodeCFactory.buildingMarshallingDecoder());
							ch.pipeline().addLast(new ObjectDecoder(1024*1024,ClassResolvers.cacheDisabled(getClass().getClassLoader())));
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(handler);

						}
					});

			ChannelFuture future = bootstrap.connect(host, port).sync();
			System.out.println("connected....");
			//will block
//			future.channel().closeFuture().sync();
		} finally {
			// TODO: handle finally clause
			//bug£º»á¹Ø±Õµô
//			group.shutdownGracefully();
		}
	}
	
	public void stop() {
		group.shutdownGracefully();
	}

	public Object connectAndGet(String host, int port, RpcRequest request) throws Exception {
		if(bootstrap == null) {
			connect(host, port);
		}
		System.out.println("start to send request");
		return handler.handleRpcRequest(request);
	}
}
