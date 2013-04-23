package ws.wamplay.controllers.messageHandlers;


import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;

import ws.wamplay.controllers.WAMPlayServer;
import ws.wamplay.models.WAMPlayClient;


public class SubscribeHandler implements MessageHandler {
	static ALogger log = Logger.of(SubscribeHandler.class);
	
	@Override
	public void process(WAMPlayClient client, JsonNode message) {
		String topic = message.get(1).asText();
		if (WAMPlayServer.isTopic(topic)) {
			client.subscribe(topic);
			return;
		}
		log.error("Client tried to subscribe to nonexistant topic: " + topic);		
	}

}
