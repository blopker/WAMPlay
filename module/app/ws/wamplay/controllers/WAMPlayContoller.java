package ws.wamplay.controllers;

import com.fasterxml.jackson.databind.JsonNode;

import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.callbacks.PubSubCallback;

public class WAMPlayContoller {

	/**
	 * Registers a topic for this controller without using annotations. If @URIPrefix
	 * is present it will be put at the beginning of topicSuffix.
	 *
	 * Same as calling WAMPlayServer.addTopic(URIPrefix.value() + topicSuffix);
	 *
	 * @param topicSuffix
	 */
	public void addTopic(String topicSuffix) {
		URIPrefix prefix = this.getClass().getAnnotation(URIPrefix.class);
		if (prefix != null) {
			topicSuffix = prefix.value() + topicSuffix;
		}
		WAMPlayServer.addTopic(topicSuffix);
	}

	/**
	 * Registers a topic for this controller without using annotations. If @URIPrefix
	 * is present it will be put at the beginning of topicSuffix.
	 *
	 * This version allows for a supplied callback.
	 *
	 * Same as calling WAMPlayServer.addTopic(URIPrefix.value() + topicSuffix, cb);
	 *
	 * @param topicSuffix
	 */
	public void addTopic(String topicSuffix, PubSubCallback cb) {
		URIPrefix prefix = this.getClass().getAnnotation(URIPrefix.class);
		if (prefix != null) {
			topicSuffix = prefix.value() + topicSuffix;
		}
		WAMPlayServer.addTopic(topicSuffix, cb);
	}

	public static JsonNode cancel() {
		return null;
	}
}
