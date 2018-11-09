package com.sgl.netty;

import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sgl.interfaces.HelloService;

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
	
	@AfterAll
	static void afterTest() {

//		client.stop();
		System.out.println("end....");
	}

}
