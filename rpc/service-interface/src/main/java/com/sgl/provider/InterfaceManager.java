package com.sgl.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sgl.annotation.PackageScan;
import com.sgl.annotation.RpcServiceImpl;
import com.sgl.utils.ClassUtils;

/**
 * 
 * @author liuhuadong
 *
 */
public class InterfaceManager {
	
	private static class InstanceHolder{
		static final InterfaceManager instance = new InterfaceManager();
	}
	
	static public InterfaceManager getInstance() {
		return InstanceHolder.instance;
	}
	
	private InterfaceManager() {
	}
	
	private Map<String, Class<?>> interfaceToImpl = new HashMap<>();
	
	public boolean register(String interfaceName, Class<?> impl) {
		if(interfaceToImpl.containsKey(interfaceName)) {
			return false;
		}
		interfaceToImpl.put(interfaceName,impl);
		return true;
	}
	
	public <T> Class<? extends T> findClass(Class<T> interface0) {
		String name = interface0.getSimpleName();
		return findClass(name);
	}
	
	public <T> Class<? extends T> findClass(String name) {
		if(interfaceToImpl.containsKey(name)) {
			return (Class<? extends T>) interfaceToImpl.get(name);
		}
		throw new IllegalArgumentException("no such interface implement :"+name);
	}
	
	public void setConfig(Object config) {
		PackageScan packageScan = config.getClass().getAnnotation(PackageScan.class);
		String packageString = packageScan.value();
		List<Class<?>> clazzes = ClassUtils.getClassFromPackage(packageString, RpcServiceImpl.class);
		for (Class<?>clazz : clazzes) {
			RpcServiceImpl impl = clazz.getAnnotation(RpcServiceImpl.class);
			register(impl.value().getName(), clazz);
		}
	}
	
}
