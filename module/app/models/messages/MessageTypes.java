package models.messages;

import controllers.messageHandlers.*;

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
	
	private final int typeID;
	private final Class<? extends MessageHandler> messageHandlerClass;
	
	private MessageTypes(int typeID, Class<? extends MessageHandler> messageHandlerClass) {
		this.typeID = typeID;
		this.messageHandlerClass = messageHandlerClass;
	}
	
	public int getTypeID() {
		return typeID;
	}
	
	public Class<? extends MessageHandler> getMessageHandlerClass() {
		return this.messageHandlerClass;
	}
	
	public static MessageTypes getType(int type) throws EnumConstantNotPresentException{
		for (MessageTypes messageType : MessageTypes.values()) {
			if (messageType.getTypeID() == type) {
				return messageType;
			}
		}
		throw new EnumConstantNotPresentException(MessageTypes.class, Integer.toString(type));
	}
}
