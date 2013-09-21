package ws.wamplay.models.messages;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;

public abstract class Message {
	public abstract MessageType getType();
	public abstract List<Object> toList();
	public String toString() {
		return this.toList().toString();
	}
	public JsonNode toJson(){
		return Json.toJson(toList());
	}
}
