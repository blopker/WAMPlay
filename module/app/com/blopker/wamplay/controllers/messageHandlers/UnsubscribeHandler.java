package com.blopker.wamplay.controllers.messageHandlers;


import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.models.WAMPlayClient;

public class UnsubscribeHandler implements MessageHandler {

	@Override
	public void process(JsonNode message, WAMPlayClient client) {
		String topic = message.get(1).asText();
		client.unsubscribe(topic);
	}

}
