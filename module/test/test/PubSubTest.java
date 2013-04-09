package test;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;


import org.codehaus.jackson.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.blopker.wamplay.callbacks.PubSubCallback;
import com.blopker.wamplay.controllers.WAMPlayContoller;
import com.blopker.wamplay.controllers.WAMPlayServer;
import com.blopker.wamplay.models.WAMPlayClient;



import play.libs.Json;

public class PubSubTest {
	WAMPlayClient client;
	WAMPlayClient client2;

	@Before
	public void setUp() {
		client = TestClientFactory.get();
		client2 = TestClientFactory.get();
	}

	@After
	public void tearDown() {
		client.kill();
		client2.kill();
	}

	@Test
	public void subscribeTest() {
		subscribe("http://example.com/simple", client);
		assertThat(client.isSubscribed("http://example.com/simple")).isTrue();

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
		String topic = "http://example.com/simple";

		assertThat(client.isSubscribed(topic)).isFalse();
		assertThat(client2.isSubscribed(topic)).isFalse();

		subscribe(topic, client);
		assertThat(client.isSubscribed(topic)).isTrue();
		assertThat(client2.isSubscribed(topic)).isFalse();

		unsubscribe(topic, client);
		assertThat(client.isSubscribed(topic)).isFalse();
		assertThat(client2.isSubscribed(topic)).isFalse();
	}

	private void unsubscribe(String topic, WAMPlayClient client) {
		JsonNode req = Json.parse("[6, \"" + topic + "\"]");
		WAMPlayServer.handleRequest(client, req);
	}

	@Test
	public void publishTest() {
		String topic = "http://example.com/simple";
		WAMPlayServer.addTopic(topic);
		subscribe(topic, client);
		subscribe(topic, client2);

		publish(topic, client2, true);
		assertThat(client2.testLastSent().toList().toString()).doesNotContain(
				"Hello, WAMP!");
		assertThat(client.testLastSent().toList().toString()).contains(
				"Hello, WAMP!");

		publish(topic, client2, false);
		assertThat(client2.testLastSent().toList().toString()).contains(
				"Hello, WAMP!");
	}

	private void publish(String topic, WAMPlayClient client, boolean excludeMe) {
		publish(topic, "Hello, WAMP!", client, excludeMe);
	}
	
	private void publish(String topic, String message, WAMPlayClient client, boolean excludeMe) {
		List<Object> res = new ArrayList<Object>();
		res.add(7);
		res.add(topic);
		res.add(message);
		if (excludeMe) {
			res.add(true);
		}
		JsonNode req = Json.toJson(res);
		WAMPlayServer.handleRequest(client, req);
	}

	@Test
	public void pubSubCallbackTest() {
		String topic = "http://example.com/capital";
		WAMPlayServer.addTopic(topic, new PubSubCallback() {
			
			@Override
			public void onSubscribe(WAMPlayClient subscribingClient) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public JsonNode onPublish(WAMPlayClient fromClient, JsonNode eventJson) {
				if(eventJson.toString().contains("cancel")){
					cancel();
				}
				return eventJson;
			}
		});
		
		subscribe(topic, client);
		
		publish(topic, "cancel this message", client, false);
		assertThat(client.testLastSent().toList().toString()).doesNotContain("cancel");
		
		publish(topic, "not this message though", client, false);
		assertThat(client.testLastSent().toList().toString()).contains("message");
		
	}
	
	@Test
	public void pubSubControllerTest(){
		WAMPlayContoller c = new TestPubSubController();
		WAMPlayServer.addController(c);
		
		String topic = "example.com/controller";
		
		subscribe(topic, client);
		
		publish(topic, "cancel this message", client, false);
		assertThat(client.testLastSent().toList().toString()).doesNotContain("cancel");
		
		publish(topic, "not this message though", client, false);
		assertThat(client.testLastSent().toList().toString()).contains("message");
	}
}
