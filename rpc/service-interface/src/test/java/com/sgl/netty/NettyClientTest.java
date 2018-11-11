package com.sgl.netty;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sgl.interfaces.HelloService;
import com.sgl.rpcproxy.RpcAsyncListener;
import com.sgl.rpcproxy.RpcFutrue;

class NettyClientTest {
	private static NettyClient client = new NettyClient();
	
	@BeforeAll
	static void beforeTest() throws Exception {
		System.out.println("connect...");
		client.connect("localhost", 9112);

	
	}
	
	@Test
	void test() {
		HelloService service = client.createProxy(HelloService.class);
		System.out.println("create proxy");
		System.out.println(service.sayHello("baby coffe"));
	}
	
	@Test
	void testAsync() throws Exception {
		RpcFutrue futrue =  client.createAsyncCall(HelloService.class, 
				"sayHello", new Object[] {"this is async call"});
		
		futrue.addResultListener(new RpcAsyncListener() {
			
			@Override
			public void onResultArrived(RpcFutrue futrue) {
				try {
					Object res = futrue.get();
					System.out.println("async get res: " + res);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	@AfterAll
	static void afterTest() {

		client.stop();
		System.out.println("end....");
	}

}
