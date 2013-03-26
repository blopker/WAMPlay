package controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import models.WAMPlayClient;
import models.messages.MessageTypes;
import models.messages.Welcome;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.WebSocket;
import controllers.messageHandlers.MessageHandler;
import controllers.messageHandlers.MessageHandlerFactory;

public class WAMPlayServer extends Controller {
	public static String VERSION = "WAMPlay/0.0.1";
	public static int PROTOCOL_VERSION = 1;

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

				in.onClose(new Callback0() {

					@Override
					public void invoke() throws Throwable {
						WAMPlayServer.removeClient(client);
					}
				});

				in.onMessage(new Callback<JsonNode>() {

					@Override
					public void invoke(JsonNode request) throws Throwable {
						handleRequest(request, client);
					}
				});

				client.send(new Welcome(client.getID()));
			}
		};
	}

	public static void handleRequest(JsonNode request, WAMPlayClient client) {
		
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

	// For testing
	public static void addClient(WAMPlayClient client) {
		clients.put(client.getID(), client);
		log.debug("WAMPClient: " + client.getID() + " connected.");
	}

	// For testing
	public static void removeClient(WAMPlayClient client) {
		clients.remove(client.getID());
		log.debug("WAMPClient: " + client.getID() + " disconnected.");
	}

	public static WAMPlayClient getClient(String clientID) {
		return clients.get(clientID);
	}

	public static ConcurrentMap<String, WAMPlayClient> getClients() {
		return clients;
	}
}
