package com.sgl.rpcproxy;

public class RpcRequest {
	private String interfaceName;
	private String methodName;
	private Class[] parameterTypes;
	private Object[] args;
	
	public RpcRequest(String interfaceName,String methodName,Class[] parameterTypes
			,Object[] args) {
		this.args = args;
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public Class[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getArgs() {
		return args;
	}
	
	
}
