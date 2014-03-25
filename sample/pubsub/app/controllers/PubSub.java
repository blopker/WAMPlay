package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.annotations.onPublish;
import ws.wamplay.annotations.onSubscribe;
import ws.wamplay.controllers.WAMPlayContoller;

// Prefix is optional, but helps remove duplicate code.
@URIPrefix("http://example.com/")
public class PubSub extends WAMPlayContoller {
    static int MAX_MESSAGE_LENGTH = 10;

    /**
     * Method that truncates an event message before it's published.
     *
     * @param client WAMP client that sent the event
     * @param event  Event to be truncated
     * @return Modified json event, null to halt publish
     */
    @onPublish("truncate")
    public static JsonNode truncatePublish(String sessionID, JsonNode event) {
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
     *
     * @param subscribingClient
     * @return True if client is allowed to subscribe, false otherwise.
     */
    @onSubscribe("truncate")
    public static boolean capitalSubscribe(String sessionID) {
        return true;
    }
}
