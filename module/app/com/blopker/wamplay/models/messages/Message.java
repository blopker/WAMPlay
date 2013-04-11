package com.blopker.wamplay.models.messages;

import java.util.List;

public abstract class Message {
	public abstract MessageType getType();
	public abstract List<Object> toList();
	public String toString() {
		return this.toList().toString();
	}
}
