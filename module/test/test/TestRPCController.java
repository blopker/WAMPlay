package test;

import com.blopker.wamplay.annotations.ControllerURIPrefix;
import com.blopker.wamplay.annotations.onRPC;
import com.blopker.wamplay.controllers.WAMPlayContoller;

@ControllerURIPrefix("test")
public class TestRPCController extends WAMPlayContoller {
	
	@onRPC("#meaningOfLife")
	public static String getMeaningOfLife() {
		return "Meaning of life is: 42";
	}
}
