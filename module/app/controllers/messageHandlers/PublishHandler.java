package controllers.messageHandlers;

import models.WAMPlayClient;
import models.messages.Event;

import org.codehaus.jackson.JsonNode;

import controllers.WAMPlayServer;

public class PublishHandler implements MessageHandler {

	@Override
	public void process(JsonNode message, WAMPlayClient senderClient) {
		String topic = message.get(1).asText();
		
		boolean excludeMe = false;
		if (message.has(3)) {
			excludeMe = message.get(3).asBoolean(false);
		}

		for (WAMPlayClient client : WAMPlayServer.getClients().values()) {
			if (excludeMe && client.getID() == senderClient.getID()) {
				continue;
			}
			
			if (client.isSubscribed(topic)) {
				client.send(new Event(topic, message.get(2)));
				System.out.println("Sent: " + topic + " to: " + client.getID());
			}
		}
	}
}
