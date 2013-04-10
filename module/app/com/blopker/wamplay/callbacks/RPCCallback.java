package com.blopker.wamplay.callbacks;

import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.models.WAMPlayClient;

public abstract class RPCCallback {
	public abstract JsonNode call(WAMPlayClient client, JsonNode... args) throws Throwable;
}
