package controllers.messageHandlers;

import models.WAMPlayClient;

import org.codehaus.jackson.JsonNode;

public interface MessageHandler {
	public void process(JsonNode message, WAMPlayClient client);
}
