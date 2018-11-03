package com.sgl.impl;

import com.sgl.interfaces.HelloService;

public class HelloServiceImpl implements HelloService {
	static public void register() {
		InterfaceManager.getInstance().register("HelloService", HelloServiceImpl.class);
	}
	
	public HelloServiceImpl() {

	}
	
	@Override
	public String sayHello(String name) {
		System.out.println("need hello to " + name);
		return "need hello to " + name;
	}
	

}
