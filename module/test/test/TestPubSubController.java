package test;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;

import com.blopker.wamplay.annotations.onPublish;
import com.blopker.wamplay.annotations.onSubscribe;
import com.blopker.wamplay.annotations.ControllerURIPrefix;
import com.blopker.wamplay.controllers.WAMPlayContoller;
import com.blopker.wamplay.models.WAMPlayClient;

@ControllerURIPrefix("example.com")
public class TestPubSubController extends WAMPlayContoller{
	static ALogger log = Logger.of(WAMPlayContoller.class);
	
	@onSubscribe("/controller")
	public static WAMPlayClient capitalSubscribe(WAMPlayClient subscribingClient) {
		log.info(subscribingClient.getID() + " subscribed!");
		return subscribingClient;
	}
	
	@onPublish("/controller")
	public static JsonNode capitalPublish(WAMPlayClient fromClient, JsonNode eventJson){
		if(eventJson.toString().contains("cancel")){
			return cancel();
		}
		return eventJson;
	}
}
