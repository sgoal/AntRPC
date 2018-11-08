package netty.test.marshalling;

import java.io.Serializable;

public class RpcResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1604615397534886255L;
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
