package ws.wamplay.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

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
import ws.wamplay.controllers.messageHandlers.PublishHandler;
import ws.wamplay.models.PubSub;
import ws.wamplay.models.RPC;
import ws.wamplay.models.WAMPlayClient;

public class WAMPlayServer extends Controller {
	public static String VERSION = "WAMPlay/0.1.6";
	public static int PROTOCOL_VERSION = 1;
	public static WAMPlayClient lastClient;

	static ALogger log = Logger.of(WAMPlayServer.class);
	static Map<String, WAMPlayClient> clients = Collections
			.unmodifiableMap(new HashMap<String, WAMPlayClient>());

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
	 * Sends a raw WAMP message to the correct controller. Method is public for
	 * easier testing. Do not use in your application.
	 *
	 * @param Raw
	 *            WAMP JSON request.
	 * @param Originating
	 *            client.
	 */
	public static void handleRequest(WAMPlayClient client, JsonNode request) {
		try {
			MessageHandler handler = HandlerFactory.get(request);
			handler.process(client, request);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void addClient(WAMPlayClient client) {
		Map<String, WAMPlayClient> clientsNew = new HashMap<String, WAMPlayClient>();
		clientsNew.putAll(clients);
		clientsNew.put(client.getSessionID(), client);
		clients = Collections.unmodifiableMap(clientsNew);
		log.debug("WAMPClient: " + client.getSessionID() + " connected.");
	}

	private static synchronized void removeClient(WAMPlayClient client) {
		Map<String, WAMPlayClient> clientsNew = new HashMap<String, WAMPlayClient>();
		clientsNew.putAll(clients);
		clientsNew.remove(client.getSessionID());
		clients = Collections.unmodifiableMap(clientsNew);
		log.debug("WAMPClient: " + client.getSessionID() + " disconnected.");
	}

	/**
	 * Gets a connected client with a ID. Can be used to send arbitrary JSON to
	 * a specific client.
	 *
	 * @param sessionID
	 *            Client's session ID as a string.
	 * @return Connected WAMP client. Returns null if there is no client.
	 */
	public static WAMPlayClient getClient(String clientID) {
		return clients.get(clientID);
	}

	/**
	 * Gets a copy of the map of all the currently connected clients. This map
	 * is immutable.
	 *
	 * @return A map of the currently connected WAMP clients.
	 */
	public static Map<String, WAMPlayClient> getClients() {
		return clients;
	}

	/**
	 * Add a PubSub topic and callbacks for clients to interact with. Topics
	 * must be specifically added or clients could kill the server by filling it
	 * up with useless topics. Adding topics through a controller's @onPublish
	 * or @onSubscribe annotation is the preferred method.
	 *
	 * @param topicURI
	 * @param pubSubCallback
	 */
	public static void addTopic(String topicURI, PubSubCallback pubSubCallback) {
		PubSub.addTopic(topicURI, pubSubCallback);
	}

	/**
	 * Remove a topic from the server. Clients will no longer be able to publish
	 * or subscribe to this topic.
	 *
	 * @param topicURI
	 */
	public static void removeTopic(String topicURI) {
		PubSub.removeTopic(topicURI);
	}

	/**
	 * Add a PubSub topic with no callbacks for clients to interact with. Topics
	 * must be specifically added or clients could kill the server by filling it
	 * up with useless topics. Adding topics through a controller's @onPublish
	 * or @onSubscribe annotation is the preferred method.
	 *
	 * @param topicURI
	 */
	public static void addTopic(String topicURI) {
		PubSub.addTopic(topicURI);
	}

	/**
	 * Checks if topicURI is a valid (subscribable) topic.
	 *
	 * @param topicURI
	 * @return if the URI points to a valid topic
	 */
	public static boolean isTopic(String topicURI) {
		return PubSub.getPubSubCallback(topicURI) == null ? false : true;
	}

	/**
	 * Registers a controller for RPC and/or PubSub. Only one onPublish or
	 * onSubscribe annotation is needed to add a topic.
	 *
	 * @param controller
	 */
	public static void addController(WAMPlayContoller controller) {
		String prefix = "";
		if (controller.getClass().isAnnotationPresent(URIPrefix.class)) {
			prefix = controller.getClass().getAnnotation(URIPrefix.class)
					.value();
		}

		PubSub.addController(prefix, controller);
		RPC.addController(prefix, controller);
	}

	/**
	 * Resets WAMPlayServer's state. Removes all controllers, topics, and
	 * clients.
	 */
	public static synchronized void reset() {
		clients = Collections
				.unmodifiableMap(new HashMap<String, WAMPlayClient>());
		PubSub.reset();
		RPC.reset();
	}

	/**
	 * Publish an event to all clients with sessionIDs not in the exclude
	 * collection.
	 *
	 * @param topicURI
	 * @param event
	 * @param exclude
	 *            , Collection of sessionIDs to exclude.
	 */
	public static void publishExclude(String topicURI, JsonNode event,
			Collection<String> exclude) {
		PublishHandler.publish(topicURI, event, exclude, null);
	}

	/**
	 * Publish an event to all clients with sessionIDs in the eligible
	 * collection.
	 *
	 * @param topicURI
	 * @param event
	 * @param exclude
	 *            , Collection of eligible sessionIDs.
	 */
	public static void publishEligible(String topicURI, JsonNode event,
			Collection<String> eligible) {
		PublishHandler.publish(topicURI, event, null, eligible);
	}

	/**
	 * Publish an event to all clients.
	 *
	 * @param topicURI
	 * @param event
	 */
	public static void publish(String topicURI, JsonNode event) {
		PublishHandler.publish(topicURI, event, null, null);
	}
}
