package test;

import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.annotations.ControllerURIPrefix;
import com.blopker.wamplay.annotations.onRPC;
import com.blopker.wamplay.controllers.WAMPlayContoller;
import com.blopker.wamplay.models.WAMPlayClient;

@ControllerURIPrefix("test")
public class TestRPCController extends WAMPlayContoller {
	
	@onRPC("#meaningOfLife")
	public static String getMeaningOfLife(WAMPlayClient client) {
		return "Meaning of life is: 42";
	}
	
	@onRPC("#add")
	public static int add(WAMPlayClient client, JsonNode[] args){
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
