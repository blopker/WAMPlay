package controllers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import models.WAMPClient;
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

public class WAMPlayServer extends Controller {
	public static String VERSION = "WAMPlay/0.0.1";
	public static int PROTOCOL_VERSION = 1;

	static ALogger log = Logger.of(WAMPlayServer.class);
	static ConcurrentMap<String, WAMPClient> clients = new ConcurrentHashMap<String, WAMPClient>();

	/**
	 * Handle the websocket.
	 */
	public static WebSocket<JsonNode> connect() {
		return new WebSocket<JsonNode>() {

			// Called when the Websocket Handshake is done.
			public void onReady(WebSocket.In<JsonNode> in,
					final WebSocket.Out<JsonNode> out) {

				final WAMPClient client = new WAMPClient(out);
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

	public static void handleRequest(JsonNode request, WAMPClient client) {
		MessageTypes type;
		try{
			type = MessageTypes.getType(request.get(0).asInt());
		} catch (EnumConstantNotPresentException e){
			log.error("Message type not implemented! " + request.toString());
			return;
		}
		
		try {
			MessageHandler handler = type.getMessageHandlerClass().newInstance();
			handler.process(request, client);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// For testing
	public static void addClient(WAMPClient client) {
		clients.put(client.getID(), client);
		log.debug("WAMPClient: " + client.getID() + " connected.");
	}

	// For testing
	public static void removeClient(WAMPClient client) {
		clients.remove(client.getID());
		log.debug("WAMPClient: " + client.getID() + " disconnected.");
	}

	public static WAMPClient getClient(String clientID) {
		return clients.get(clientID);
	}

	public static ConcurrentMap<String, WAMPClient> getClients() {
		return clients;
	}
}
