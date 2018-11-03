package com.sgl.impl;

import com.sgl.interfaces.HelloService;

public class HelloServiceImpl implements HelloService {
	static {
		InterfaceManager.getInstance().regeister("HelloService", HelloServiceImpl.class);
	}
	
	public HelloServiceImpl() {

	}
	
	@Override
	public void sayHello(String name) {
		System.out.println("need hello to " + name);
	}
	

}
