package com.sgl.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sgl.annotation.RpcServiceImpl;
import com.sgl.interfaces.HelloService;
import com.sgl.interfaces.ServiceData;
import com.sgl.provider.InterfaceManager;

@RpcServiceImpl(HelloService.class)
public class HelloServiceImpl implements HelloService {
	static public void register() {
//		InterfaceManager.getInstance().register(HelloService.class.getName(), HelloServiceImpl.class);
	}
	
	public HelloServiceImpl() {

	}
	
	@Override
	public String sayHello(String name) {
		System.out.println("need hello to " + name);
		return "need hello to " + name;
	}

	@Override
	public List<ServiceData> getData() {
		return Arrays.asList(new ServiceData("test1"),new ServiceData("test2"));
	}
	

}
