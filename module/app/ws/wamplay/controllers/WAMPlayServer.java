package ws.wamplay.controllers;

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
import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.callbacks.PubSubCallback;
import ws.wamplay.controllers.messageHandlers.HandlerFactory;
import ws.wamplay.controllers.messageHandlers.MessageHandler;
import ws.wamplay.models.PubSub;
import ws.wamplay.models.RPC;
import ws.wamplay.models.WAMPlayClient;


public class WAMPlayServer extends Controller {
	public static String VERSION = "WAMPlay/0.0.4";
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
				handleRequest(client, null);
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
		try{
			MessageHandler handler = HandlerFactory.get(request);
			handler.process(client, request);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}		
	}

	private static void addClient(WAMPlayClient client) {
		clients.put(client.getSessionID(), client);
		log.debug("WAMPClient: " + client.getSessionID() + " connected.");
	}

	private static void removeClient(WAMPlayClient client) {
		clients.remove(client.getSessionID());
		log.debug("WAMPClient: " + client.getSessionID() + " disconnected.");
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
		if (controller.getClass().isAnnotationPresent(URIPrefix.class)) {
			prefix = controller.getClass().getAnnotation(URIPrefix.class).value();
		}
		
		PubSub.addController(prefix, controller);	
		RPC.addController(prefix, controller);
	}
}
