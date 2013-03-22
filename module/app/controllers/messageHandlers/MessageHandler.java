package controllers.messageHandlers;

import models.WAMPClient;

import org.codehaus.jackson.JsonNode;

public interface MessageHandler {
	public void process(JsonNode message, WAMPClient client);
}
