package test;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.libs.Json;
import controllers.WAMPlayServer;

public class PubSubTest {
	TestClient client;
	TestClient client2;

	@Before
	public void setUp() {
		client = new TestClient(null);
		client2 = new TestClient(null);
		WAMPlayServer.addClient(client);
		WAMPlayServer.addClient(client2);
	}

	@After
	public void tearDown() {
		WAMPlayServer.removeClient(client);
		WAMPlayServer.removeClient(client2);
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

	private void subscribe(String topic, TestClient client) {
		JsonNode req = Json.parse("[5, \"" + topic + "\"]");
		WAMPlayServer.handleRequest(req, client);
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

	private void unsubscribe(String topic, TestClient client) {
		JsonNode req = Json.parse("[6, \"" + topic + "\"]");
		WAMPlayServer.handleRequest(req, client);
	}

	@Test
	public void publishTest() {
		String topic = "http://example.com/simple";
		
		subscribe(topic, client);
		subscribe(topic, client2);
		
		publish(topic, client2, true);		
		assertThat(client2.lastSent.toString()).doesNotContain("Hello, WAMP!");
		assertThat(client.lastSent.toString()).contains("Hello, WAMP!");
		
		publish(topic, client2, false);
		assertThat(client2.lastSent.toString()).contains("Hello, WAMP!");
	}

	private void publish(String topic, TestClient client, boolean excludeMe) {
		List<Object> res = new ArrayList<Object>();
		res.add(7);
		res.add(topic);
		res.add("Hello, WAMP!");
		if (excludeMe) {
			res.add(true);
		}
		JsonNode req = Json.toJson(res);
		WAMPlayServer.handleRequest(req, client);
	}
}
