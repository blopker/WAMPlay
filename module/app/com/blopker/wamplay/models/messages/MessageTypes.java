package com.blopker.wamplay.models.messages;

import com.blopker.wamplay.controllers.messageHandlers.*;

public enum MessageTypes {
	WELCOME(0, null),
//	PREFIX(1),
	CALL(2, new RPCHandler()),
	CALLRESULT(3, null),
	CALLERROR(4, null),
	SUBSCRIBE(5, new SubscribeHandler()),
	UNSUBSCRIBE(6, new UnsubscribeHandler()),
	PUBLISH(7, new PublishHandler()),
	EVENT(8, null);
	
	private final int typeCode;
	private final MessageHandler handler;
	
	private MessageTypes(int typeCode, MessageHandler handler) {
		this.typeCode = typeCode;
		this.handler = handler;
	}
	
	public int getTypeCode() {
		return typeCode;
	}
	
	public MessageHandler getHandler(){
		return this.handler;
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
