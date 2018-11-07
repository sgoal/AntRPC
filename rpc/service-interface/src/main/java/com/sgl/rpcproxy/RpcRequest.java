package com.sgl.rpcproxy;

import java.io.Serializable;
import java.util.UUID;

public class RpcRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8912477931664988954L;
	
	//TODO 改为不可能出错的id
	private String requestId = UUID.randomUUID().toString();
	
	private String interfaceName;
	private String methodName;

	
	private Class[] parameterTypes;
	private Object[] args;
	
	public RpcRequest() {
		// TODO Auto-generated constructor stub
	}
	
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
	
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParameterTypes(Class[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getRequestId() {
		return requestId;
	}

}
