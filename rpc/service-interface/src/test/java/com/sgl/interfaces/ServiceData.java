package com.sgl.interfaces;

import java.io.Serializable;

public class ServiceData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ServiceData(String s) {
		testString = s;
	}
	public String testString;
}
