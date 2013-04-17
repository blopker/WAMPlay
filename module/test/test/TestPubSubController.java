package test;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.annotations.onPublish;
import ws.wamplay.annotations.onSubscribe;
import ws.wamplay.controllers.WAMPlayContoller;


@URIPrefix("example.com")
public class TestPubSubController extends WAMPlayContoller{
	static ALogger log = Logger.of(WAMPlayContoller.class);
	
	@onSubscribe("/controller")
	public static boolean capitalSubscribe(String sessionID) {
		log.info(sessionID + " subscribed!");
		return true;
	}
	
	@onPublish("/controller")
	public static JsonNode capitalPublish(String sessionID, JsonNode eventJson){
		if(eventJson.toString().contains("cancel")){
			return cancel();
		}
		return eventJson;
	}
}
