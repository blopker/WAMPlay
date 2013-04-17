package com.blopker.wamplay.controllers.messageHandlers;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;

import com.blopker.wamplay.models.WAMPlayClient;
import com.blopker.wamplay.models.messages.Welcome;

public class ConnectHandler implements MessageHandler {
	@Override
	public void process(WAMPlayClient client, JsonNode message) {
		List<Object> welcome = new Welcome(client.getSessionID()).toList(); 
		client.send(Json.toJson(welcome));
	}
}
