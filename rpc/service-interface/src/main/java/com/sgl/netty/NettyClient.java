package com.sgl.netty;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sgl.constant.ServiceChangeType;
import com.sgl.rpcproxy.RpcConnector;
import com.sgl.rpcproxy.RpcFutrue;
import com.sgl.rpcproxy.RpcProxy;
import com.sgl.rpcproxy.RpcRequest;
import com.sgl.zookeeper.ServiceChangeListener;
import com.sgl.zookeeper.ServiceFinder;
import com.sgl.zookeeper.ServiceRegister;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class NettyClient {
	private ExecutorService pool;
	private RpcConnector nettyClientConnector;
	private ServiceFinder serviceFinder;
	
	public NettyClient() {
		nettyClientConnector =  NettyClientConnector.getInstance();
		pool = Executors.newFixedThreadPool(2*Runtime.getRuntime().availableProcessors());
	}
	
	public NettyClient(String zkHost) throws Exception {
		this();
		serviceFinder = new ServiceFinder(zkHost);
		String address1 = serviceFinder.findAddress((String address, ServiceChangeType type)->{
				System.out.println(String.format("service %s change: %s" , address , type));
				stop();
				
				String newAddress =  serviceFinder.findAddress();
				if(newAddress!=null) {
					try {
						connect(newAddress);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		});
		connect(address1);
	}
	
	public void stop() {
		nettyClientConnector.stop();
	}
	
	public void connect(String host, int port) throws Exception {
		nettyClientConnector.connect(host, port);
	}
	
	public void connect(String hostAndPort) throws Exception {
		String[] datas = hostAndPort.split(":");
		String host = datas[0];
		int port = Integer.valueOf(datas[1]);
		nettyClientConnector.connect(host, port);
	}
	
	public Object connectAndGet(String host, int port, RpcRequest request) throws Exception {
		nettyClientConnector.connect(host, port);
		System.out.println("start to send request");
		return handleRpcRequest(request).get();
	}
	
	public Object connectAndGet(RpcRequest request) throws Exception {
		System.out.println("start to send request");
		return handleRpcRequest(request).get();
	}
	
	public RpcFutrue handleRpcRequest(RpcRequest request) throws Exception {
		RpcFutrue futrue =  new RpcFutrue(this);
		nettyClientConnector.putRequest(request, futrue);
		CountDownLatch latch = new CountDownLatch(1);
		nettyClientConnector.getChannelHandler().writeAndFlush(request).addListener(
				(ChannelFuture future) ->{
						if(future.isSuccess()) {
							latch.countDown();
						}else {
							future.channel().close();
							System.out.println("close.....");
						}
				});
		latch.await();
		return futrue;
	}
	
	public <T> T createProxy(Class<T> clazz) {
		RpcProxy rpcProxy =  new RpcProxy();
		return (T) rpcProxy.getProxy(clazz);
	}
	
	public <T> RpcFutrue createAsyncCall(Class<T> clazz, String methodName, Object[] args) throws Exception {
		return RpcProxy.call(clazz, methodName, this, args);
	}
	
	public <V> Future<V> submitTask(Callable<V> task) {
		return pool.submit(task);
	}
	
	public void executeTask(Runnable task) {
		pool.execute(task);
	}
}
