package com.blopker.wamplay.callbacks;

import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.models.WAMPlayClient;

public abstract class PubCallback {
	protected abstract JsonNode onPublish(WAMPlayClient fromClient, JsonNode eventJson);
}
