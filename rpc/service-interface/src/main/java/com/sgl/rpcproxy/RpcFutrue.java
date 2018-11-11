package com.sgl.rpcproxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RpcFutrue  implements Future<Object>{
	CountDownLatch latch;
	volatile RpcResponse response;
	List<RpcAsyncListener> listeners = new ArrayList<>();
	
	
	public RpcFutrue(){
		latch = new CountDownLatch(1);
	}
	
	public void setResponse(RpcResponse response) {
		this.response = response;
		latch.countDown();
		synchronized (listeners) {
			for(RpcAsyncListener listener: listeners) {
				listener.onResultArrived(this);
			}
		}
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
