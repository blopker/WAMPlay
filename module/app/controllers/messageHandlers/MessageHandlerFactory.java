package controllers.messageHandlers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import models.messages.MessageTypes;

public class MessageHandlerFactory {
	
	static ConcurrentMap<MessageTypes, MessageHandler> handlerMap = new ConcurrentHashMap<MessageTypes, MessageHandler>();
	
	public static MessageHandler get(MessageTypes type) {
		if (handlerMap.containsKey(type)) {
			return handlerMap.get(type);
		} else {
			MessageHandler handler = createHandler(type);
			handlerMap.put(type, handler);
			return handler;
		}
	}
	
	private static MessageHandler createHandler(MessageTypes type) {
		Class <? extends MessageHandler> handlerClass = type.getMessageHandlerClass();
		MessageHandler handler = null;
		try {
			handler = handlerClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handler;
	}
}
