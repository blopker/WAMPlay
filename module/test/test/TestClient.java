package test;

import models.WAMPlayClient;
import models.messages.Message;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.mvc.WebSocket.Out;

public class TestClient extends WAMPlayClient {
	public JsonNode lastSent = Json.parse("[]");
	
	public TestClient(Out<JsonNode> out) {
		super(out);
	}
	
	public void send(Message message){
		JsonNode node = Json.toJson(message.toList());
		lastSent = node;
	}
}
