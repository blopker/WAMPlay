package test;


import static org.fest.assertions.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.libs.Json;

import com.blopker.wamplay.controllers.WAMPlayServer;
import com.blopker.wamplay.models.WAMPlayClient;

public class RPCTest {
	WAMPlayClient client;
	WAMPlayClient client2;
	
	@Before
	public void setUp() {
		WAMPlayServer.addController(new TestRPCController());
		client = TestClientFactory.get();
		client2 = TestClientFactory.get();
	}
	
	@After
	public void tearDown() {
		client.kill();
		client2.kill();
	}
	
	@Test
	public void getMeaningOfLife() {
		call(client, "test#meaningOfLife");
		assertThat(client.lastMessage().toList().toString()).contains("Meaning of life is: 42");
	}
	
	@Test
	public void testAdd() {
		callAdd(client, 42, 100);
		assertThat(client.lastMessage().toList().toString()).contains(", 142");
	}
	
	public void call(WAMPlayClient client, String URI) {
		Object[] req = {"2", "rpcid" + URI, URI};
		WAMPlayServer.handleRequest(client, Json.toJson(req));
	}
	
	public void callAdd(WAMPlayClient client, int a, int b) {
		String URI = "test#add";
		Object[] req = {"2", "rpcid" + a + b, URI, a, b};
		WAMPlayServer.handleRequest(client, Json.toJson(req));
	}
}
