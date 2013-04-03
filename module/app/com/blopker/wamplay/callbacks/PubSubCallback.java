package com.blopker.wamplay.callbacks;


import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.models.WAMPlayClient;

public class PubSubCallback {
	boolean canceled = false;
	PubCallback pub;
	SubCallback sub;
	
	protected JsonNode onPublish(WAMPlayClient fromClient, JsonNode eventJson){
		if (pub != null) {
			JsonNode node = pub.onPublish(fromClient, eventJson);
			if (node == null) {
				cancel();
			}
			return node;
		}
		return eventJson;
	}
	
	protected void onSubscribe(WAMPlayClient subscribingClient){
		if (sub != null) {
			if (sub.onSubscribe(subscribingClient) == null) {
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
	
	public JsonNode runPubCallback(WAMPlayClient fromClient, JsonNode eventJson) {
		canceled = false;
		return onPublish(fromClient, eventJson);
	}
	
	public void runSubCallback(WAMPlayClient subscribingClient) {
		canceled = false;
		onSubscribe(subscribingClient);
	}
	
	
	protected void cancel() {
		canceled = true;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
}
