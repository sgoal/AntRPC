package com.sql.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;

import com.sgl.constant.Config;

import org.apache.zookeeper.ZooKeeper;

public class ServiceRegister {
	private String host;
	private ZooKeeper zoo;
	private final CountDownLatch connectedSignal = new CountDownLatch(1);

	public ServiceRegister(String zkHost) throws Exception {
		host = zkHost;
		zoo = connect();
	}

	public ZooKeeper connect() throws Exception {
		zoo = new ZooKeeper(host, Config.ZK_SESSION_TIMEOUT, new Watcher() {
			public void process(WatchedEvent we) {
				if (we.getState() == KeeperState.SyncConnected) {
					connectedSignal.countDown();
				}
			}
		});
		connectedSignal.await();
		return zoo;
	}

	public void create(String path, byte[] data) throws KeeperException, InterruptedException {
		zoo.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	public void stop() throws Exception {
		zoo.close();
	}
	
	public ZooKeeper getZooKeeper() {
		return zoo;
	}
}
