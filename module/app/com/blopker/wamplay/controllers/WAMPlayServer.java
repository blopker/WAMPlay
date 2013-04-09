package com.blopker.wamplay.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.WebSocket;

import com.blopker.wamplay.annotations.ControllerURIPrefix;
import com.blopker.wamplay.callbacks.PubSubCallback;
import com.blopker.wamplay.controllers.messageHandlers.MessageHandler;
import com.blopker.wamplay.controllers.messageHandlers.MessageHandlerFactory;
import com.blopker.wamplay.models.PubSub;
import com.blopker.wamplay.models.RPC;
import com.blopker.wamplay.models.WAMPlayClient;
import com.blopker.wamplay.models.messages.MessageTypes;
import com.blopker.wamplay.models.messages.Welcome;

public class WAMPlayServer extends Controller {
	public static String VERSION = "WAMPlay/0.0.1";
	public static int PROTOCOL_VERSION = 1;
	public static WAMPlayClient lastClient;

	static ALogger log = Logger.of(WAMPlayServer.class);
	static ConcurrentMap<String, WAMPlayClient> clients = new ConcurrentHashMap<String, WAMPlayClient>();


	/**
	 * Handle the websocket.
	 */
	public static WebSocket<JsonNode> connect() {
		return new WebSocket<JsonNode>() {

			// Called when the Websocket Handshake is done.
			public void onReady(WebSocket.In<JsonNode> in,
					final WebSocket.Out<JsonNode> out) {

				final WAMPlayClient client = new WAMPlayClient(out);
				WAMPlayServer.addClient(client);

				if (in != null) { // null if testing.
					in.onClose(new Callback0() {

						@Override
						public void invoke() throws Throwable {
							WAMPlayServer.removeClient(client);
						}
					});

					in.onMessage(new Callback<JsonNode>() {

						@Override
						public void invoke(JsonNode request) throws Throwable {
							handleRequest(client, request);
						}
					});
				}
				client.send(new Welcome(client.getID()));
				lastClient = client;
			}
		};
	}

	/**
	 * Sends a raw WAMP message to the correct controller. Method is public for easier testing.
	 * Do not use in your application.
	 * @param Raw WAMP JSON request.
	 * @param Originating client.
	 */
	public static void handleRequest(WAMPlayClient client, JsonNode request) {

		MessageTypes type;
		try {
			type = MessageTypes.getType(request.get(0).asInt());
		} catch (EnumConstantNotPresentException e) {
			log.error("Message type not implemented! " + request.toString());
			return;
		}

		MessageHandler handler = MessageHandlerFactory.get(type);
		handler.process(request, client);
	}

	private static void addClient(WAMPlayClient client) {
		clients.put(client.getID(), client);
		log.debug("WAMPClient: " + client.getID() + " connected.");
	}

	private static void removeClient(WAMPlayClient client) {
		clients.remove(client.getID());
		log.debug("WAMPClient: " + client.getID() + " disconnected.");
	}

	/**
	 * Gets a connected client with a ID. Use to interact with a specific client.
	 * @param Client's ID as a string.
	 * @return Connected WAMP client. Returns null if there is no client.
	 */
	public static WAMPlayClient getClient(String clientID) {
		return clients.get(clientID);
	}

	/**
	 * Gets a copy of the map of all the currently connected clients. 
	 * @return A map of the currently connected WAMP clients.
	 */
	public static Map<String, WAMPlayClient> getClients() {
		Map<String, WAMPlayClient> map = new HashMap<>();
		map.putAll(clients);
		return map;
	}

	public static PubSubCallback getPubSubCallback(String topic) {
		return PubSub.getPubSubCallback(topic);
	}
	
	public static void addTopic(String topic, PubSubCallback pubSubCallback) {
		PubSub.addTopic(topic, pubSubCallback);		
	}
	
	public static void removeTopic(String topic) {
		PubSub.removeTopic(topic);
	}
	
	public static void addTopic(String topic) {
		PubSub.addTopic(topic);
	}
	
	public static void addController(WAMPlayContoller controller) {
		String prefix = "";
		if (controller.getClass().isAnnotationPresent(ControllerURIPrefix.class)) {
			prefix = controller.getClass().getAnnotation(ControllerURIPrefix.class).value();
		}
		
		PubSub.addController(prefix, controller);	
		RPC.addController(prefix, controller);
	}
}
