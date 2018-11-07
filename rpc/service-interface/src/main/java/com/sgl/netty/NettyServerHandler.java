package com.sgl.netty;

import java.lang.reflect.Method;

import com.sgl.provider.InterfaceManager;
import com.sgl.rpcproxy.RpcRequest;
import com.sgl.rpcproxy.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;


public class NettyServerHandler  extends SimpleChannelInboundHandler<RpcRequest>{
	
	NettyServer  server;
	
	public NettyServerHandler(NettyServer  server) {
		this.server = server;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		System.out.println(msg+"11111111");
	}
	
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
		//��Զ��Ҫ�� Netty �� I/O �߳���ִ���κη� CPU �޶��Ĵ��롪���㽫��� Netty ͵ȡ�������Դ�������Ӱ�쵽����������������
		System.out.println(msg.getMethodName());
		Runnable task = new Runnable() {

			@Override
			public void run() {
				RpcRequest request = msg;
				System.out.println("read: "+ request.getInterfaceName());
				Class implClass = InterfaceManager.getInstance().findClass(request.getInterfaceName());
				
				Method method;
				try {
					method = implClass.getMethod(request.getMethodName(), request.getParameterTypes());
					Object res =  method.invoke(implClass.newInstance(), request.getArgs());
					
					RpcResponse response = new RpcResponse();
					response.setResult(res);
					response.setRequestId(request.getRequestId());
					response.setMethodNname(request.getMethodName());
					ctx.writeAndFlush(response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		};
		
		server.submitTask(task);
	}


//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
}
