package com.sgl.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import com.sgl.constant.Config;

import org.apache.zookeeper.ZooKeeper;

public class ServiceRegister extends ZookeeperConnector{

	public ServiceRegister(String zkHost) throws Exception {
		super(zkHost);
	}

	public void create(String path, byte[] data) throws KeeperException, InterruptedException {
		zoo.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	public void createRootNode() throws Exception {
		if(zoo.exists(Config.ZOOKEEPER_SERVICE_PATH, false) == null) {
			create(Config.ZOOKEEPER_SERVICE_PATH, new byte[0]);
		}
	}

	public void addNode( byte[] data) throws KeeperException, InterruptedException {
		//创建临时节点,来进行注册
		zoo.create(Config.ZOOKEEPER_DATA_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}
	public void stop() throws Exception {
		zoo.close();
	}
	
	public ZooKeeper getZooKeeper() {
		return zoo;
	}
}
