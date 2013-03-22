package controllers.messageHandlers;

import models.WAMPClient;

import org.codehaus.jackson.JsonNode;

public class UnsubscribeHandler implements MessageHandler {

	@Override
	public void process(JsonNode message, WAMPClient client) {
		String topic = message.get(1).asText();
		client.unsubscribe(topic);
	}

}
