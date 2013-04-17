package ws.wamplay.callbacks;


import org.codehaus.jackson.JsonNode;

public class PubSubCallback {
	boolean canceled = false;
	PubCallback pub;
	SubCallback sub;
	
	protected JsonNode onPublish(String sessionID, JsonNode eventJson){
		if (pub != null) {
			JsonNode node = pub.onPublish(sessionID, eventJson);
			if (node == null) {
				cancel();
			}
			return node;
		}
		return eventJson;
	}
	
	protected void onSubscribe(String sessionID){
		if (sub != null) {
			if (sub.onSubscribe(sessionID) == false) {
				cancel();
			}
		}
	}
	
	public void setSubCallback(SubCallback sub) {
		this.sub = sub;
	}
	
	public void setPubCallback(PubCallback pub) {
		this.pub = pub;
	}
	
	public JsonNode runPubCallback(String sessionID, JsonNode eventJson) {
		canceled = false;
		return onPublish(sessionID, eventJson);
	}
	
	public void runSubCallback(String sessionID) {
		canceled = false;
		onSubscribe(sessionID);
	}
	
	
	protected void cancel() {
		canceled = true;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
}
