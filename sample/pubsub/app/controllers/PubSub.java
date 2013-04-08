package controllers;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;

import com.blopker.wamplay.annotations.ControllerURIPrefix;
import com.blopker.wamplay.annotations.onPublish;
import com.blopker.wamplay.annotations.onSubscribe;
import com.blopker.wamplay.controllers.WAMPlayContoller;
import com.blopker.wamplay.models.WAMPlayClient;

// Prefix is optional, but helps remove duplicate code.
@ControllerURIPrefix("http://example.com/")
public class PubSub extends WAMPlayContoller {
	static int MAX_MESSAGE_LENGTH = 10;
	
	/**
	 * Method that truncates an event message before it's published. 
	 * @param client WAMP client that sent the event
	 * @param event Event to be truncated
	 * @return Modified json event
	 */
	@onPublish("truncate")
	public static JsonNode truncatePublish(WAMPlayClient client, JsonNode event) {
		System.out.println(event);
		if (!event.isTextual()) {
			return cancel();
		}		
		String message = event.asText();
		if (message.length() > 10) {
			message = message.substring(0, MAX_MESSAGE_LENGTH);
		}
		return Json.toJson(message);
	}
	
	/**
	 * Only one onPublish or onSubscribe annotation is necessary to create a topic.
	 * @param subscribingClient
	 * @return The client, or a cancel() to stop the client from subscribing.
	 */
	@onSubscribe("truncate")
	public static WAMPlayClient capitalSubscribe(WAMPlayClient subscribingClient) {
		System.out.println(subscribingClient.getID() + " subscribed!");
		return subscribingClient;
	}
}
