package test;


import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.libs.Json;
import ws.wamplay.controllers.WAMPlayServer;
import ws.wamplay.models.WAMPlayClient;


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
		assertThat(client.lastMessage().toString()).contains("Meaning of life is: 42");
		assertThat(client.lastMessage().toString()).contains("rpcidtest#meaningOfLife");
	}
	
	@Test
	public void testAdd() {
		callAdd(client, 42, 100);
		assertThat(client.lastMessage().toString()).contains(",142");
	}
	
	@Test
	public void testError() {
		call(client, "notARealThing");
		assertThat(client.lastMessage().toString()).contains("404");
	}
	
	@Test
	public void testIllegalArgumentError() {
		callAdd(client, 1, "not a number");
		assertThat(client.lastMessage().toString()).contains("Argument is not a number!");
	}
	
	public void call(WAMPlayClient client, String URI) {
		Object[] req = {2, "rpcid" + URI, URI};
		WAMPlayServer.handleRequest(client, Json.toJson(req));
	}
	
	public void callAdd(WAMPlayClient client, int a, Object b) {
		String URI = "test#add";
		Object[] req = {"2", "rpcidadd", URI, a, b};
		WAMPlayServer.handleRequest(client, Json.toJson(req));
	}
}
