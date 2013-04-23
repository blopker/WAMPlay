package test;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;


import org.codehaus.jackson.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;




import play.libs.Json;
import ws.wamplay.callbacks.PubSubCallback;
import ws.wamplay.controllers.WAMPlayContoller;
import ws.wamplay.controllers.WAMPlayServer;
import ws.wamplay.models.WAMPlayClient;

public class PubSubTest {
	WAMPlayClient client;
	WAMPlayClient client2;
	String SIMPLE = "http://example.com/simple";
	String HELLO = "Hello, WAMP!";

	@Before
	public void setUp() {
		client = TestClientFactory.get();
		client2 = TestClientFactory.get();
	}

	@After
	public void tearDown() {
		client.kill();
		client2.kill();
		WAMPlayServer.reset();
	}

	@Test
	public void subscribeTest() {
		WAMPlayServer.addTopic(SIMPLE);
		
		subscribe(SIMPLE, client);
		assertThat(client.isSubscribed("http://example.com/simple")).isTrue();

		WAMPlayServer.addTopic("http://example.com/hello");
		subscribe("http://example.com/hello", client);
		assertThat(client.isSubscribed("http://example.com/hello")).isTrue();

		assertThat(client.isSubscribed("http://example.com/notatopic"))
				.isFalse();
	}

	private void subscribe(String topic, WAMPlayClient client) {
		JsonNode req = Json.parse("[5, \"" + topic + "\"]");
		WAMPlayServer.handleRequest(client, req);
	}

	@Test
	public void unsubscribeTest() {
		WAMPlayServer.addTopic(SIMPLE);

		assertThat(client.isSubscribed(SIMPLE)).isFalse();
		assertThat(client2.isSubscribed(SIMPLE)).isFalse();

		subscribe(SIMPLE, client);
		assertThat(client.isSubscribed(SIMPLE)).isTrue();
		assertThat(client2.isSubscribed(SIMPLE)).isFalse();

		unsubscribe(SIMPLE, client);
		assertThat(client.isSubscribed(SIMPLE)).isFalse();
		assertThat(client2.isSubscribed(SIMPLE)).isFalse();
	}

	private void unsubscribe(String topic, WAMPlayClient client) {
		JsonNode req = Json.parse("[6, \"" + topic + "\"]");
		WAMPlayServer.handleRequest(client, req);
	}

	@Test
	public void publishTest() {
		WAMPlayServer.addTopic(SIMPLE);
		subscribe(SIMPLE, client);
		subscribe(SIMPLE, client2);

		publishExcludeMe(SIMPLE, HELLO, client2, false);
		
		assertThat(client2.lastMessage().toString()).contains(
				HELLO);
		assertThat(client.lastMessage().toString()).contains(
				HELLO);
	}
	
	@Test
	public void publishExcludeMeTest() {
		WAMPlayServer.addTopic(SIMPLE);
		subscribe(SIMPLE, client);
		
		publishExcludeMe(SIMPLE, HELLO, client, true);
		assertThat(client.lastMessage().toString()).doesNotContain(HELLO);
	}
	
	@Test
	public void publishExcludeTest() {
		WAMPlayServer.addTopic(SIMPLE);
		subscribe(SIMPLE, client);
		String[] exclude = {client.getSessionID()};
		publishExclude(SIMPLE, HELLO, client, exclude);
		
		assertThat(client.lastMessage().toString()).doesNotContain(HELLO);
	}
	
	@Test
	public void publishEligibleTest() {
		WAMPlayServer.addTopic(SIMPLE);
		subscribe(SIMPLE, client);
		subscribe(SIMPLE, client2);
		
		String[] eligible = {client2.getSessionID()};
		
		publishEligible(SIMPLE, HELLO, client, eligible);
		
		assertThat(client.lastMessage().toString()).doesNotContain(HELLO);
		assertThat(client2.lastMessage().toString()).contains(HELLO);
	}
	
	@Test
	public void serverPublishTest(){
		WAMPlayServer.addTopic(SIMPLE);
		subscribe(SIMPLE, client);
		subscribe(SIMPLE, client2);
		
		List<String> eligible = new ArrayList<String>();
		eligible.add(client2.getSessionID());
		
		WAMPlayServer.publishEligible(SIMPLE, Json.toJson(HELLO), eligible);
		
		assertThat(client.lastMessage().toString()).doesNotContain(HELLO);
		assertThat(client2.lastMessage().toString()).contains(HELLO);		
		
		WAMPlayServer.publish(SIMPLE, Json.toJson(HELLO));
		assertThat(client.lastMessage().toString()).contains(HELLO);	
		assertThat(client.lastMessage().isArray()).isTrue();
	}
	
	private void publishExcludeMe(String topic, String message, WAMPlayClient client, boolean excludeMe) {
		List<Object> res = getPublishBeginning(topic, message);
		if (excludeMe) {
			res.add(excludeMe);
		}
		send(client, res);
	}
	
	private void publishExclude(String topic, String message, WAMPlayClient client, String[] exclude) {
		List<Object> res = getPublishBeginning(topic, message);
		res.add(exclude);
		send(client, res);
	}
	
	private void publishEligible(String topic, String message, WAMPlayClient client, String[] eligible) {
		List<Object> res = getPublishBeginning(topic, message);
		res.add(new String[0]);
		res.add(eligible);
		send(client, res);
	}
	
	private List<Object> getPublishBeginning(String topic, String message) {
		List<Object> res = new ArrayList<Object>();
		res.add(7);
		res.add(topic);
		res.add(message);
		
		return res;
	}
	
	private void send(WAMPlayClient client, List<Object> res) {
		JsonNode req = Json.toJson(res);
//		System.out.println(req);
		WAMPlayServer.handleRequest(client, req);
	}

	@Test
	public void pubSubCallbackTest() {
		String topic = "http://example.com/capital";
		WAMPlayServer.addTopic(topic, new PubSubCallback() {
			
			@Override
			public boolean onSubscribe(String sessionID) {
				return true;
			}
			
			@Override
			public JsonNode onPublish(String sessionID, JsonNode eventJson) {
				if(eventJson.toString().contains("cancel")){
					return null;
				}
				return eventJson;
			}
		});
		
		subscribe(topic, client);
		
		publishExcludeMe(topic, "cancel this message", client, false);
		assertThat(client.lastMessage().toString()).doesNotContain("cancel");
		
		publishExcludeMe(topic, "not this message though", client, false);
		assertThat(client.lastMessage().toString()).contains("message");
		
	}
	
	@Test
	public void pubSubControllerTest(){
		WAMPlayContoller c = new TestPubSubController();
		WAMPlayServer.addController(c);
		
		String topic = "example.com/controller";
		
		subscribe(topic, client);
		
		publishExcludeMe(topic, "cancel this message", client, false);
		assertThat(client.lastMessage().toString()).doesNotContain("cancel");
		
		publishExcludeMe(topic, "not this message though", client, false);
		assertThat(client.lastMessage().toString()).contains("message");
		
		subscribe("example.com/easyTopic", client);
		publishExcludeMe("example.com/easyTopic", "test easyTopic", client, false);
		assertThat(client.lastMessage().toString()).contains("test easyTopic");
		
		subscribe("example.com/unsubscribable", client);
		publishExcludeMe("example.com/unsubscribable", "test unsubscribable", client, false);
		assertThat(client.lastMessage().toString()).doesNotContain("test unsubscribable");
		
	}
}
