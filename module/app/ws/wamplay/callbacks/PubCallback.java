package ws.wamplay.callbacks;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class PubCallback {
	protected abstract JsonNode onPublish(String sessionID, JsonNode eventJson);
}
