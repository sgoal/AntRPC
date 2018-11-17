package com.sgl.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.sgl.constant.Config;

public class ZookeeperConnector {
	protected String host;
	protected ZooKeeper zoo;
	private final CountDownLatch connectedSignal = new CountDownLatch(1);
	
	public ZookeeperConnector(String zkHost) throws Exception {
		host = zkHost;
		zoo = connect();
	}

	public ZooKeeper connect() throws Exception {
		zoo = new ZooKeeper(host, Config.ZK_SESSION_TIMEOUT, watchedEvent-> {
				if (watchedEvent.getState() == KeeperState.SyncConnected) {
					connectedSignal.countDown();
				}
		});
		connectedSignal.await();
		return zoo;
	}
}
