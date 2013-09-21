package ws.wamplay.callbacks;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class RPCCallback {
	public abstract JsonNode call(String string, JsonNode... args) throws Throwable;
}
