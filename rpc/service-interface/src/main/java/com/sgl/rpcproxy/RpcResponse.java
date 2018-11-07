package com.sgl.rpcproxy;

public class RpcResponse {
	private String requestId;
	private String methodNname;
	private Object result;
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getMethodNname() {
		return methodNname;
	}
	public void setMethodNname(String methodNname) {
		this.methodNname = methodNname;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
