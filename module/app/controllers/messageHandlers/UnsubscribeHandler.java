package controllers.messageHandlers;

import models.WAMPlayClient;

import org.codehaus.jackson.JsonNode;

public class UnsubscribeHandler implements MessageHandler {

	@Override
	public void process(JsonNode message, WAMPlayClient client) {
		String topic = message.get(1).asText();
		client.unsubscribe(topic);
	}

}
