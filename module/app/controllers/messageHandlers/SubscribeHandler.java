package controllers.messageHandlers;

import models.WAMPlayClient;

import org.codehaus.jackson.JsonNode;

public class SubscribeHandler implements MessageHandler {

	@Override
	public void process(JsonNode message, WAMPlayClient client) {
		String topic = message.get(1).asText();
		client.subscribe(topic);
	}

}
