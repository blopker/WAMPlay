package com.blopker.wamplay.controllers.messageHandlers;


import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.callbacks.PubSubCallback;
import com.blopker.wamplay.controllers.WAMPlayServer;
import com.blopker.wamplay.models.WAMPlayClient;
import com.blopker.wamplay.models.messages.Event;

import play.Logger;
import play.Logger.ALogger;



public class PublishHandler implements MessageHandler {
	static ALogger log = Logger.of(PublishHandler.class.getSimpleName());
	
	@Override
	public void process(JsonNode message, WAMPlayClient senderClient) {
		String topic = message.get(1).asText();
		
		PubSubCallback cb = WAMPlayServer.getPubSubCallback(topic);
		
		if (cb == null) {
			log.error("Topic not found: " + topic);
			return;
		}
		
		message = cb.runPubCallback(senderClient, message);
	
		if (cb.isCanceled()) {
			log.info("Callback for " + topic + " canceled.");
			return;
		}
		
		boolean excludeMe = false;
		if (message.has(3)) {
			excludeMe = message.get(3).asBoolean(false);
		}

		for (WAMPlayClient client : WAMPlayServer.getClients().values()) {
			if (excludeMe && client.getID().equals(senderClient.getID())) {
				// Client does not want to get its own event.
				continue;
			}
			
			if (client.isSubscribed(topic)) {
				client.send(new Event(topic, message.get(2)));
				log.info("Sent: "  + topic + " to: " + client.getID());
			}
		}
	}
}
