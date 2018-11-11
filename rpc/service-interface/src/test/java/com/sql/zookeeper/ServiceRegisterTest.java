package com.sql.zookeeper;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ServiceRegisterTest {
	static ServiceRegister serviceRegister;
	
	@BeforeAll
	static void beforeTest() throws Exception {
		serviceRegister = new ServiceRegister("127.0.0.1:4981");
	}
	
	@Test
	void test() throws KeeperException, InterruptedException {
		try {
		serviceRegister.create("/testNode", "this is test".getBytes());
		}catch (NodeExistsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Test
	void testGetChildren() throws Exception {
		Stat stat = serviceRegister.getZooKeeper().exists("/testNode", false);
		if(stat!=null) {
			System.out.println("version " + stat.getAversion());
			Stat stat2 = new Stat();
			byte[] ds = serviceRegister.getZooKeeper().getData("/testNode", new Watcher() {

				@Override
				public void process(WatchedEvent event) {
					if(event.getType() == EventType.None) {
						switch (event.getState()) {
						case Expired:
							
							break;

						default:
							break;
						}
					}else {
						String path = "/testNode";
						try {
							byte[] bs = serviceRegister.getZooKeeper().getData("/testNode", false, null);
							String data = new String(bs,"utf8");
							System.out.println("data : " + data);
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}
				}
				
			},stat2);
			
			System.out.println("data1 : " + new String(ds,"utf-8"));
			
		}else {
			System.out.println("not exists..");
		}
	}

}
