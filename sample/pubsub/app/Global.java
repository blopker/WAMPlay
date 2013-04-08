import play.Application;
import play.GlobalSettings;
import com.blopker.wamplay.controllers.WAMPlayServer;

import controllers.PubSub;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		WAMPlayServer.addTopic("http://example.com/chat");
		WAMPlayServer.addController(new PubSub());
	}
}
