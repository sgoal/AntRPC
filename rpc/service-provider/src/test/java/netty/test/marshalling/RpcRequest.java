package netty.test.marshalling;

import java.io.Serializable;

public class RpcRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8198111529871956113L;
	
	private int seqID;
	private String mehtodName;
	private String[] args;
	
	public int getSeqID() {
		return seqID;
	}
	public void setSeqID(int seqID) {
		this.seqID = seqID;
	}
	public String getMehtodName() {
		return mehtodName;
	}
	public void setMehtodName(String mehtodName) {
		this.mehtodName = mehtodName;
	}
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	@Override
	public String toString() {
		return "["+mehtodName + args +"]";
	}
	
}
