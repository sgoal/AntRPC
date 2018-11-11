package com.sgl.rpcproxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.sgl.netty.NettyClient;

public class RpcFutrue  implements Future<Object>{
	private CountDownLatch latch;
	private volatile RpcResponse response;
	private List<RpcAsyncListener> listeners = new ArrayList<>();
	private NettyClient client;
	
	public RpcFutrue(NettyClient client){
		latch = new CountDownLatch(1);
		this.client = client;
	}
	
	public void setResponse(RpcResponse response) {
		this.response = response;
		latch.countDown();
		client.executeTask(new Runnable() {
			
			@Override
			public void run() {
				synchronized (listeners) {
					for(RpcAsyncListener listener: listeners) {
						listener.onResultArrived(RpcFutrue.this);
					}
				}
			}
		});
		
	}
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDone() {
		return latch.getCount()==0;
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		latch.await();
		return response.getResult();
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		latch.await(timeout, unit);
		return response.getResult();
	}
	
	public void addResultListener(RpcAsyncListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

}
