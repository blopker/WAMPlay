package com.blopker.wamplay.models.messages;

import java.util.List;

public interface Message {
	public MessageTypes getType();
	public List<Object> toList();
}
