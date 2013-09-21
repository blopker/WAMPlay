package ws.wamplay.callbacks;


import com.fasterxml.jackson.databind.JsonNode;

public class PubSubCallback {
	PubCallback pub;
	SubCallback sub;

	protected JsonNode onPublish(String sessionID, JsonNode eventJson){
		if (pub != null) {
			return pub.onPublish(sessionID, eventJson);
		}
		return eventJson;
	}

	protected boolean onSubscribe(String sessionID){
		if (sub != null) {
			return sub.onSubscribe(sessionID);
		}
		return true;
	}

	public void setSubCallback(SubCallback sub) {
		this.sub = sub;
	}

	public void setPubCallback(PubCallback pub) {
		this.pub = pub;
	}

	public JsonNode runPubCallback(String sessionID, JsonNode eventJson) {
		return onPublish(sessionID, eventJson);
	}

	public boolean runSubCallback(String sessionID) {
		return onSubscribe(sessionID);
	}
}
