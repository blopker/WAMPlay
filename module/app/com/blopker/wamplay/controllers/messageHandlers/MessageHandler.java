package com.blopker.wamplay.controllers.messageHandlers;


import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.models.WAMPlayClient;

public interface MessageHandler {
	public void process(WAMPlayClient client, JsonNode message);
}
