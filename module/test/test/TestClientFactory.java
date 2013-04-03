package test;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.blopker.wamplay.controllers.WAMPlayServer;
import com.blopker.wamplay.models.WAMPlayClient;


public class TestClientFactory {
	static Set<Entry<String, WAMPlayClient>> oldSet = new HashSet<>(); 
	
	public static WAMPlayClient get() {
		WAMPlayServer.connect().onReady(null, null);
		return WAMPlayServer.lastClient;
	}
}
