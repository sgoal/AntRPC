package com.sgl.netty;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sgl.zookeeper.ServiceFinder;
import com.sgl.zookeeper.ServiceRegister;

class NettyServerTest {

	@Test
	void test() throws Exception {
		NettyServer server = new NettyServer("127.0.0.1:4981");
		server.bindAndRegister(9112);
		Thread.sleep(1000);
		ServiceFinder serviceFinder = new ServiceFinder("127.0.0.1:4981");
		System.out.println(serviceFinder.getAddresses());
		
	}

}
