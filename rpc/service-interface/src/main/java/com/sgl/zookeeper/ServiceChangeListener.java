package com.sgl.zookeeper;

import com.sgl.constant.ServiceChangeType;

public interface ServiceChangeListener {
	public void onServiceChange(String address, ServiceChangeType type);
}
