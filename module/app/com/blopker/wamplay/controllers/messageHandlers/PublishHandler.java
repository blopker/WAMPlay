package com.blopker.wamplay.controllers.messageHandlers;


import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;

import com.blopker.wamplay.callbacks.PubSubCallback;
import com.blopker.wamplay.controllers.WAMPlayServer;
import com.blopker.wamplay.models.WAMPlayClient;
import com.blopker.wamplay.models.messages.Event;



public class PublishHandler implements MessageHandler {
	static ALogger log = Logger.of(PublishHandler.class.getSimpleName());
	
	@Override
	public void process(WAMPlayClient senderClient, JsonNode message) {
		String topic = message.get(1).asText();
		
		PubSubCallback cb = WAMPlayServer.getPubSubCallback(topic);
		
		if (cb == null) {
			log.error("Topic not found: " + topic);
			return;
		}
		
		JsonNode event = cb.runPubCallback(senderClient, message.get(2));
		
		if (cb.isCanceled()) {
			log.info("Callback for " + topic + " canceled.");
			return;
		}
		
		boolean excludeMe = false;
		if (message.has(3)) {
			excludeMe = message.get(3).asBoolean(false);
		}

		for (WAMPlayClient client : WAMPlayServer.getClients().values()) {
			if (excludeMe && client.getSessionID().equals(senderClient.getSessionID())) {
				// Client does not want to get its own event.
				continue;
			}
			
			if (client.isSubscribed(topic)) {
				client.send(new Event(topic, event));
				log.info("Sent: "  + topic + " to: " + client.getSessionID());
			}
		}
	}
}
