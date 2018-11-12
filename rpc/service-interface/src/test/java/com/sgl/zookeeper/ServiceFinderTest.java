package com.sgl.zookeeper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ServiceFinderTest {
	static ServiceFinder serviceFinder;
	
	@BeforeAll
	static void beforeTest() throws Exception {
		serviceFinder = new ServiceFinder("127.0.0.1:4981");
	}
	@Test
	void test() {
		System.out.println(serviceFinder.getAddresses());
	}

}
