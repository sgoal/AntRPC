package com.sgl.zookeeper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.sgl.constant.Config;
import com.sgl.constant.ServiceChangeType;

public class ServiceFinder  extends ZookeeperConnector{
	private List<String> addresses = new ArrayList<>();
	private Map<String, ServiceChangeListener> listners = new ConcurrentHashMap<>();
	
	public ServiceFinder(String zkHost) throws Exception {
		super(zkHost);
		findServices();
	}
	
	public void findServices() throws Exception {
		List<String> datas = new ArrayList<>();
		List<String> nodes = zoo.getChildren(Config.ZOOKEEPER_SERVICE_PATH, event->{

				if(event.getType() == EventType.NodeChildrenChanged) {
					try {
						findServices();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				
			}
		});
		for(String node: nodes) {
			byte[] data = zoo.getData(Config.ZOOKEEPER_SERVICE_PATH+"/"+node, false, null);
			datas.add(new String(data));
		}
		synchronized (this) {
			HashSet<String> set = new HashSet<>(datas);
			for(String old:addresses) {
				if(!set.contains(old)) {
					if(listners.containsKey(old)) {
						listners.get(old).onServiceChange(old, 
								ServiceChangeType.ServiceDisconnect);
						listners.remove(old);
					}
				}
			}
			addresses = datas;
		}
		System.out.println("addresses: " + addresses);
		
	}
	
	public synchronized List<String> getAddresses(){
		return addresses;
	}
	
	public synchronized String findAddress() {
		if(addresses.size()==1) {
			return addresses.get(0);
		}else if (addresses.size()>1) {
			return addresses.get(new Random().nextInt(addresses.size()));
		}else {
			return null;
		}
		
	}
	
	public String findAddress(ServiceChangeListener listener) {
		String address = findAddress();
		if(address!=null) {
			listners.put(address, listener);
		}
		return address;
		
	}
}
