package com.blopker.wamplay.models.messages;

import com.blopker.wamplay.controllers.messageHandlers.*;

public enum MessageTypes {
	WELCOME(0, null),
//	PREFIX(1),
//	CALL(2),
//	CALLRESULT(3),
//	CALLERROR(4),
	SUBSCRIBE(5, SubscribeHandler.class),
	UNSUBSCRIBE(6, UnsubscribeHandler.class),
	PUBLISH(7, PublishHandler.class),
	EVENT(8, null);
	
	private final int typeCode;
	private final Class<? extends MessageHandler> messageHandlerClass;
	
	private MessageTypes(int typeCode, Class<? extends MessageHandler> messageHandlerClass) {
		this.typeCode = typeCode;
		this.messageHandlerClass = messageHandlerClass;
	}
	
	public int getTypeCode() {
		return typeCode;
	}
	
	public Class<? extends MessageHandler> getMessageHandlerClass() {
		return this.messageHandlerClass;
	}
	
	public static MessageTypes getType(int type) throws EnumConstantNotPresentException{
		for (MessageTypes messageType : MessageTypes.values()) {
			if (messageType.getTypeCode() == type) {
				return messageType;
			}
		}
		throw new EnumConstantNotPresentException(MessageTypes.class, Integer.toString(type));
	}
}
