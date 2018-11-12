package com.sgl.zookeeper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sgl.constant.ServiceChangeType;

class ServiceFinderTest {
	static ServiceFinder serviceFinder;
	
	@BeforeAll
	static void beforeTest() throws Exception {
		serviceFinder = new ServiceFinder("127.0.0.1:4981");
	}
//	@Test
	void test() throws Exception {
		serviceFinder.findServices();
		System.out.println(serviceFinder.getAddresses());
		Thread.sleep(12000);
		System.out.println(serviceFinder.getAddresses());
	}
	
	@Test
	void testFindAddress() throws Exception {
		String address = serviceFinder.findAddress();
		System.out.println(address);
	}
	
	@Test
	void testFindAddressListener() throws Exception {

		final String address = serviceFinder.findAddress(new ServiceChangeListener() {

			@Override
			public void onServiceChange(String address, ServiceChangeType type) {
				System.out.println(address + " disconnect");
			}
			
		});
		System.out.println(address);
		Thread.sleep(12000);
	}

}
