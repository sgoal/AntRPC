package com.sgl.netty;

import java.net.Inet4Address;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sgl.zookeeper.ServiceRegister;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyServer {
	private ExecutorService pool = null;
	private ServiceRegister serviceRegister = null;
	private ServerBootstrap bootstrap = null;
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workGroup = null;
	
	public NettyServer() {
		int coreNum = Runtime.getRuntime().availableProcessors(); 
		pool = Executors.newFixedThreadPool(coreNum*2);
	}
	
	public NettyServer(String zkHost) throws Exception {
		this();
		serviceRegister = new ServiceRegister(zkHost);
	}
	
	private ChannelFuture bind0(int port)throws Exception {
		
		bossGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 100)
		.handler(new LoggingHandler(LogLevel.ERROR))
		.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
//						ch.pipeline()
//						.addLast(MarshallingCodeCFactory.buildingMarshallingEncoder());
//						ch.pipeline()
//						.addLast(MarshallingCodeCFactory.buildingMarshallingDecoder());
//						ch.pipeline().addLast(new TestDecode());
						ch.pipeline().addLast(new ObjectDecoder(1024*1024,ClassResolvers.weakCachingResolver(getClass().getClassLoader())));
						ch.pipeline().addLast(new ObjectEncoder());
						ch.pipeline().addLast(new NettyServerHandler(NettyServer.this));
						
					}
		});
		return bootstrap.bind(port).sync();
		
	} 
	public void bind(int port) throws Exception {
		
		try {
			
			
			ChannelFuture future = bind0(port);
			future.channel().closeFuture().sync();
			
		} finally {
			// TODO: handle finally clause
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public void bindAndRegister(int port) throws Exception {
		
		try {
			ChannelFuture future = bind0(port);
			
			serviceRegister.createRootNode();
			String address = Inet4Address.getLocalHost().getHostAddress();
			address = address +":" +Integer.valueOf(port);
			serviceRegister.addNode(address.getBytes());
			
			future.channel().closeFuture().sync();
			
		} finally {
			// TODO: handle finally clause
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	
	public void submitTask(Runnable task) {
		pool.execute(task);
	}
	
}
