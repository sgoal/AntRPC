package com.sgl.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.sgl.constant.Config;

public class ServiceFinder  extends ZookeeperConnector{
	private List<String> addresses = new ArrayList<>();
	public ServiceFinder(String zkHost) throws Exception {
		super(zkHost);
	}
	
	public void findServices() throws Exception {
		List<String> datas = new ArrayList<>();
		List<String> nodes = zoo.getChildren(Config.ZOOKEEPER_DATA_PATH, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				if(event.getType() == EventType.NodeChildrenChanged) {
					try {
						findServices();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		for(String node: nodes) {
			byte[] data = zoo.getData(Config.ZOOKEEPER_SERVICE_PATH+"/"+node, false, null);
			datas.add(new String(data));
		}
		synchronized (this) {
			addresses = datas;
		}
		
	}
	
	public synchronized List<String> getAddresses(){
		return addresses;
	}
}
