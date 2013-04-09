package com.blopker.wamplay.models;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import play.Logger;
import play.Logger.ALogger;

import com.blopker.wamplay.annotations.onPublish;
import com.blopker.wamplay.annotations.onRPC;
import com.blopker.wamplay.callbacks.PubSubCallback;
import com.blopker.wamplay.callbacks.RPCCallback;
import com.blopker.wamplay.controllers.WAMPlayContoller;

public class RPC {

	static ALogger log = Logger.of(RPC.class.getSimpleName());
	static ConcurrentMap<String, RPCCallback> procURIs = new ConcurrentHashMap<String, RPCCallback>();
	
	public static void addController(String prefix, WAMPlayContoller controller) {
		for (final Method method : controller.getClass().getMethods()) {
			if (method.isAnnotationPresent(onRPC.class)) {
				String procURI = prefix + method.getAnnotation(onRPC.class).value();
				procURIs.put(procURI, new)
			}
		}
		
	}

}
