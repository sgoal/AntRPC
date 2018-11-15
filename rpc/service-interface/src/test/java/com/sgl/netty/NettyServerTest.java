package com.sgl.netty;




import org.junit.jupiter.api.Test;

import com.sgl.impl.HelloServiceImpl;
import com.sgl.zookeeper.ServiceFinder;
import com.sgl.zookeeper.ServiceRegister;

class NettyServerTest {
	static {
		HelloServiceImpl.register();
	}
	@Test
	void test() throws Exception {
		NettyServer server = new NettyServer("127.0.0.1:4981");
		server.bindAndRegister(9112);
		Thread.sleep(1000);
		ServiceFinder serviceFinder = new ServiceFinder("127.0.0.1:4981");
		System.out.println(serviceFinder.getAddresses());
		
	}

}
