package ws.wamplay.controllers.messageHandlers;


import com.fasterxml.jackson.databind.JsonNode;

import ws.wamplay.models.WAMPlayClient;


public class UnsubscribeHandler implements MessageHandler {

	@Override
	public void process(WAMPlayClient client, JsonNode message) {
		String topic = message.get(1).asText();
		client.unsubscribe(topic);
	}

}
