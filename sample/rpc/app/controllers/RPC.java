package controllers;

import org.codehaus.jackson.JsonNode;

import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.annotations.onRPC;
import ws.wamplay.controllers.WAMPlayContoller;

@URIPrefix("http://example.com/calc#")
public class RPC extends WAMPlayContoller {

	@onRPC("meaningOfLife")
	public static String getMeaningOfLife(String sessionID) {
		return "Meaning of life is: 42";
	}

	@onRPC("capital")
	public static String add(String sessionID, JsonNode[] args) {
		if (!args[0].isTextual() || args[0].isNumber()) {
			throw new IllegalArgumentException("Argument is not a word!");
		}
		String ans = args[0].asText().toUpperCase();
		return ans;
	}
}
