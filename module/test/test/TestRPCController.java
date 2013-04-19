package test;

import org.codehaus.jackson.JsonNode;

import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.annotations.onRPC;
import ws.wamplay.controllers.WAMPlayContoller;


@URIPrefix("test")
public class TestRPCController extends WAMPlayContoller {
	
	@onRPC("#meaningOfLife")
	public String getMeaningOfLife(String sessionID) {
		return "Meaning of life is: 42";
	}
	
	@onRPC("#add")
	public static int add(String sessionID, JsonNode... args){
		int ans = 0;
		for (JsonNode jsonNode : args) {
			if (!jsonNode.isInt()) {
				throw new IllegalArgumentException("Argument is not a number!");
			}
			ans += jsonNode.asInt();
		}
		return ans;
	}
}
