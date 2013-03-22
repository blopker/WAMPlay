package test;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;
import models.WAMPClient;
import models.messages.Message;

public class TestClient extends WAMPClient {
	public JsonNode lastSent = Json.parse("[]");
	
	public TestClient(Out<JsonNode> out) {
		super(out);
	}
	
	public void send(Message message){
		JsonNode node = Json.toJson(message.toList());
		lastSent = node;
	}

}
