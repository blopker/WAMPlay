package com.blopker.wamplay.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;

import com.blopker.wamplay.annotations.ControllerURIPrefix;
import com.blopker.wamplay.annotations.onPublish;
import com.blopker.wamplay.annotations.onSubscribe;
import com.blopker.wamplay.callbacks.PubCallback;
import com.blopker.wamplay.callbacks.PubSubCallback;
import com.blopker.wamplay.callbacks.SubCallback;
import com.blopker.wamplay.controllers.WAMPlayContoller;

public class PubSub {
	static ALogger log = Logger.of(PubSub.class.getSimpleName());
	static ConcurrentMap<String, PubSubCallback> topics = new ConcurrentHashMap<String, PubSubCallback>();

	public static void addController(final WAMPlayContoller controller) {
		String prefix = "";
		if (controller.getClass().isAnnotationPresent(ControllerURIPrefix.class)) {
			prefix = controller.getClass().getAnnotation(ControllerURIPrefix.class).value();
		}
		
		for (final Method method : controller.getClass().getMethods()) {
			if (method.isAnnotationPresent(onPublish.class)) {
				String topic = prefix + method.getAnnotation(onPublish.class).value();
				PubCallback cb = new PubCallback() {
					
					@Override
					protected JsonNode onPublish(WAMPlayClient fromClient, JsonNode eventJson) {
						try {
							return (JsonNode) method.invoke(null, fromClient, eventJson);
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
				};
				addTopicCallback(topic, cb);
			}
			
			if (method.isAnnotationPresent(onSubscribe.class)) {
				String topic = prefix + method.getAnnotation(onSubscribe.class).value();
				SubCallback cb = new SubCallback() {
					
					@Override
					protected WAMPlayClient onSubscribe(WAMPlayClient subscribingClient) {
						try {
							return (WAMPlayClient) method.invoke(null, subscribingClient);
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
				};
				addTopicCallback(topic, cb);
			}
		}
	}

	private static void addTopicCallback(String topic, PubCallback cb) {
		createOrGet(topic).setPubCallback(cb);
	}

	private static void addTopicCallback(String topic, SubCallback cb) {
		createOrGet(topic).setSubCallback(cb);		
	}
	
	private static PubSubCallback createOrGet(String topic) {
		PubSubCallback pub = topics.get(topic);
		if (pub == null) {
			pub =  new PubSubCallback();
			topics.put(topic, pub);
		}
		return pub;
	}

	public static void addTopic(String topic) {
		// Just add a topic with no callback functions.
		addTopic(topic, new PubSubCallback() {
			
			@Override
			protected void onSubscribe(WAMPlayClient subscribingClient) {
			}
			
			@Override
			protected JsonNode onPublish(WAMPlayClient fromClient, JsonNode eventJson) {
				return eventJson;
			}
		});
		
	}

	public static void addTopic(String topic, PubSubCallback pubSubCallback) {
		topics.put(topic, pubSubCallback);
		
	}

	public static void removeTopic(String topic) {
		topics.remove(topic);
		
	}

	public static PubSubCallback getPubSubCallback(String topic) {
		return topics.get(topic);
	}

}
