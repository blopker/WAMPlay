package controllers;

import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.annotations.ControllerURIPrefix;
import com.blopker.wamplay.annotations.onRPC;
import com.blopker.wamplay.controllers.WAMPlayContoller;
import com.blopker.wamplay.models.WAMPlayClient;

@ControllerURIPrefix("http://example.com/calc#")
public class RPC extends WAMPlayContoller {

	@onRPC("meaningOfLife")
	public static String getMeaningOfLife(WAMPlayClient client) {
		return "Meaning of life is: 42";
	}

	@onRPC("capital")
	public static String add(WAMPlayClient client, JsonNode[] args) {
		if (!args[0].isTextual() || args[0].isNumber()) {
			throw new IllegalArgumentException("Argument is not a word!");
		}
		String ans = args[0].asText().toUpperCase();
		return ans;
	}
}
