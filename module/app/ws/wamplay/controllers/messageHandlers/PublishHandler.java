package ws.wamplay.controllers.messageHandlers;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import ws.wamplay.callbacks.PubSubCallback;
import ws.wamplay.controllers.WAMPlayServer;
import ws.wamplay.models.PubSub;
import ws.wamplay.models.WAMPlayClient;
import ws.wamplay.models.messages.Event;


public class PublishHandler implements MessageHandler {
	static ALogger log = Logger.of(PublishHandler.class);

	@Override
	public void process(WAMPlayClient senderClient, JsonNode message) {
		String topic = message.get(1).asText();

		PubSubCallback cb = PubSub.getPubSubCallback(topic);

		if (cb == null) {
			log.error("Topic not found: " + topic);
			return;
		}

		JsonNode event = cb.runPubCallback(senderClient.getSessionID(), message.get(2));

		if (event == null) {
			log.debug("Callback for " + topic + " canceled.");
			return;
		}

		Set<String> exclude = getExcludes(senderClient.getSessionID(), message);
		Set<String> eligible = getEligible(senderClient.getSessionID(), message);

		publish(topic, event, exclude, eligible);

	}

	private static Set<String> getEligible(String sessionID, JsonNode message) {
		if (!message.has(4) || message.get(4).isNull()) {
			return null;
		}

		JsonNode eligNode = message.get(4);
		Set<String> eligible = new HashSet<String>();

		if (eligNode.isArray()) {
			// eligible array is specified
			for (JsonNode sessionNode : eligNode) {
				if (sessionNode.isTextual()) {
					eligible.add(sessionNode.asText());
				}
			}
		}

		return eligible;
	}

	private static Set<String> getExcludes(String sessionID, JsonNode message) {
		if (!message.has(3) || message.get(3).isNull()) {
			return null;
		}

		Set<String> exclude = new HashSet<String>();

		if (message.get(3).isArray()) {
			// exclude array is specified
			for (JsonNode sessionNode : message.get(3)) {
				if (sessionNode.isTextual()) {
					exclude.add(sessionNode.asText());
				}
			}
		} else if (message.get(3).isBoolean()){
			// excludeme is given
			exclude.add(sessionID);
		}

		return exclude;
	}

	public static void publish(String topicURI, JsonNode event, Collection<String> exclude, Collection<String> eligible){
		for (WAMPlayClient client : WAMPlayServer.getClients().values()) {
			if (client.isSubscribed(topicURI)) {

				if (isExcluded(client.getSessionID(), exclude)) {
					continue;
				}

				if(isEligible(client.getSessionID(), eligible)){
					client.send(new Event(topicURI, event).toJson());
					log.debug("Sent: "  + topicURI + " to: " + client.getSessionID());
				}
			}
		}
	}

	private static boolean isExcluded(String sessionID, Collection<String> exclude) {
		if (exclude == null) {
			return false;
		}

		if (exclude.size() == 0) {
			return false;
		}

		if (exclude.contains(sessionID)) {
			return true;
		}
		return false;
	}

	private static boolean isEligible(String sessionID, Collection<String> eligible) {
		if (eligible == null) {
			return true;
		}

		if (eligible.size() == 0) {
			return false;
		}

		if (eligible.contains(sessionID)) {
			return true;
		}
		return false;
	}
}
