package ws.wamplay.controllers;

import org.codehaus.jackson.JsonNode;

import ws.wamplay.annotations.URIPrefix;

public class WAMPlayContoller {

	/**
	 * Registers a topic with this controller without using annotations. If @URIPrefix
	 * is present it will be put at the beginning of topicSuffix.
	 * 
	 * @param topicSuffix
	 */
	public void addTopic(String topicSuffix) {
		URIPrefix prefix = this.getClass().getAnnotation(URIPrefix.class);
		if (prefix != null) {
			topicSuffix = prefix.value() + topicSuffix;
		}
		System.out.println("adding" + topicSuffix);
		WAMPlayServer.addTopic(topicSuffix);
	}

	public static JsonNode cancel() {
		return null;
	}
}
