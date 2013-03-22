package models.messages;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import controllers.WAMPlayServer;

public class Event implements Message{
	final Object event;
	final String topic;
	final List<Object> res = new ArrayList<Object>();
	
	public Event(String topic, JsonNode event) {
		this.topic = topic;
		this.event = event;
		
		res.add(getType());
		res.add(topic);
		res.add(event);
	}	
	
	@Override
	public MessageTypes getType() {
		return MessageTypes.EVENT;
	}

	@Override
	public List<Object> toList() {
		return res;
	}
}
