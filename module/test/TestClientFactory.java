package test;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import ws.wamplay.controllers.WAMPlayServer;
import ws.wamplay.models.WAMPlayClient;



public class TestClientFactory {
	static Set<Entry<String, WAMPlayClient>> oldSet = new HashSet<>(); 
	
	public static WAMPlayClient get() {
		WAMPlayServer.connect().onReady(null, null);
		return WAMPlayServer.lastClient;
	}
}
