package ws.wamplay.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import ws.wamplay.annotations.onPublish;
import ws.wamplay.annotations.onSubscribe;
import ws.wamplay.callbacks.PubCallback;
import ws.wamplay.callbacks.PubSubCallback;
import ws.wamplay.callbacks.SubCallback;
import ws.wamplay.controllers.WAMPlayContoller;


public class PubSub {
	static ALogger log = Logger.of(PubSub.class.getSimpleName());
	static ConcurrentMap<String, PubSubCallback> topics = new ConcurrentHashMap<String, PubSubCallback>();

	public static void addController(String prefix, final WAMPlayContoller controller) {

		for (final Method method : controller.getClass().getMethods()) {
			if (method.isAnnotationPresent(onPublish.class)) {
				String topic = prefix + method.getAnnotation(onPublish.class).value();
				PubCallback cb = new PubCallback() {

					@Override
					protected JsonNode onPublish(String sessionID, JsonNode eventJson) {
						try {
							return (JsonNode) method.invoke(controller, sessionID, eventJson);
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
					protected boolean onSubscribe(String sessionID) {
						try {
							return (boolean) method.invoke(controller, sessionID);
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
						return false;
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
		addTopic(topic, new PubSubCallback());
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

	public static void reset() {
		topics.clear();
	}

}
