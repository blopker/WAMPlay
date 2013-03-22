package controllers.messageHandlers;

import models.WAMPClient;
import models.messages.Event;

import org.codehaus.jackson.JsonNode;

import controllers.WAMPlayServer;

public class PublishHandler implements MessageHandler {

	@Override
	public void process(JsonNode message, WAMPClient senderClient) {
		System.out.println( " to: " + senderClient.getID());
		String topic = message.get(1).asText();
		for (WAMPClient client : WAMPlayServer.getClients().values()) {
			if (client.isSubscribed(topic)) {
				client.send(new Event(topic, message.get(2)));
				System.out.println("Sent: " + topic + " to: " + client.getID());
			}
		}
	}
}
