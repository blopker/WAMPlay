package ws.wamplay.controllers.messageHandlers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import ws.wamplay.models.WAMPlayClient;
import ws.wamplay.models.messages.Welcome;


public class ConnectHandler implements MessageHandler {
	@Override
	public void process(WAMPlayClient client, JsonNode message) {
		List<Object> welcome = new Welcome(client.getSessionID()).toList();
		client.send(Json.toJson(welcome));
	}
}
