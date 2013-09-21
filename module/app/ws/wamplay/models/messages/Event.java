package ws.wamplay.models.messages;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class Event extends Message{
	final JsonNode event;
	final String topic;
	final List<Object> res = new ArrayList<Object>();

	public Event(String topic, JsonNode event) {
		this.topic = topic;
		this.event = event;

		res.add(getType().getTypeCode());
		res.add(topic);
		res.add(event);
	}

	@Override
	public MessageType getType() {
		return MessageType.EVENT;
	}

	@Override
	public List<Object> toList() {
		return res;
	}
}
