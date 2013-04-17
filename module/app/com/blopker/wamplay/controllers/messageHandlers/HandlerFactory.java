package com.blopker.wamplay.controllers.messageHandlers;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;

import com.blopker.wamplay.models.messages.MessageType;

public class HandlerFactory {
	static ALogger log = Logger.of(HandlerFactory.class);
	
	public static MessageHandler get(JsonNode request) throws IllegalArgumentException{
		if (request == null) {
			return MessageType.CONNECT.getHandler();
		}
		
		if (!request.isArray() || request.get(0).asInt(-1) == -1) {
			throw new IllegalArgumentException("Not valid WAMP request: " + request.toString());
		}
				
		try {
			MessageType type = MessageType.getType(request.get(0).asInt());
			return type.getHandler();
		} catch (EnumConstantNotPresentException e) {
			String error = "Message not implemented! " + request.toString();
			log.error(error);
			throw new IllegalArgumentException(error);
		}
	}
}
