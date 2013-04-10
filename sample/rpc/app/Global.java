import play.Application;
import play.GlobalSettings;

import com.blopker.wamplay.controllers.WAMPlayServer;

import controllers.RPC;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		WAMPlayServer.addController(new RPC());
	}
}