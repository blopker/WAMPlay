package com.blopker.wamplay.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import play.libs.Json;

import com.blopker.wamplay.annotations.onRPC;
import com.blopker.wamplay.callbacks.RPCCallback;
import com.blopker.wamplay.controllers.WAMPlayContoller;

public class RPC {

	static ALogger log = Logger.of(RPC.class.getSimpleName());
	static ConcurrentMap<String, RPCCallback> procURIs = new ConcurrentHashMap<String, RPCCallback>();

	public static void addController(String prefix, final WAMPlayContoller controller) {
		for (final Method method : controller.getClass().getMethods()) {
			if (method.isAnnotationPresent(onRPC.class)) {
				String procURI = prefix
						+ method.getAnnotation(onRPC.class).value();
				procURIs.put(procURI, new RPCCallback() {
					
					@Override
					public JsonNode call(WAMPlayClient client, JsonNode... args) {
						try {
							if (args.length == 0) {
								log.debug("No RPC arguments!");
								return Json.toJson(method.invoke(null, client));
							}
							return Json.toJson(method.invoke(null, client, args));
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							log.error(controller.getClass().getSimpleName() + " " + method.getName() + " has incorrect arguments!");
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
				});
			}
		}

	}

	public static void addCallback(String procURI, RPCCallback cb) {
		procURIs.put(procURI, cb);
	}
	
	public static RPCCallback getCallback(String procURI) {
		return procURIs.get(procURI);
	}

}
