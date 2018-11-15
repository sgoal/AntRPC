package com.sgl.provider;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sgl.annotation.RpcServiceImpl;
import com.sgl.interfaces.HelloService;
import com.sgl.interfaces.PackageConfig;
import com.sgl.utils.ClassUtils;

class InterfaceManagerTest {

	@Test
	void test() {
		System.out.println(ClassUtils.getAllClassByInterface(RpcServiceImpl.class, HelloService.class));
		System.out.println(ClassUtils.getClassFromPackage("/com/sgl/impl", null));
		InterfaceManager.getInstance().setConfig(new PackageConfig());
		assertNotNull(InterfaceManager.getInstance().findClass(HelloService.class.getName()));
		System.out.println(InterfaceManager.getInstance().findClass(HelloService.class.getName()));
	}

}
