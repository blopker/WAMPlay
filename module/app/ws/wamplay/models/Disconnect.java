package ws.wamplay.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import ws.wamplay.annotations.onDisconnect;
import ws.wamplay.callbacks.DisconnectCallback;
import ws.wamplay.controllers.WAMPlayContoller;
import ws.wamplay.models.WAMPlayClient;


public class Disconnect {
	static ALogger log = Logger.of(Disconnect.class.getSimpleName());
    static DisconnectCallback cb;
    static List<DisconnectCallback> callbacks = new ArrayList<DisconnectCallback>();


	public static void addController(final WAMPlayContoller controller) {

		for (final Method method : controller.getClass().getMethods()) {
			if (method.isAnnotationPresent(onDisconnect.class)) {
				cb = new DisconnectCallback() {

					@Override
					public void onDisconnect(WAMPlayClient client) {
						try {
							method.invoke(controller, client);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							log.error(controller.getClass().getSimpleName() + " " + method.getName() + " has incorrect arguments!");
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				addCallback(cb);
			}

		}
	}

	protected static void addCallback(DisconnectCallback aCallback) {
        callbacks.add(aCallback);
    }

    public static void call(WAMPlayClient client) {
        for (final DisconnectCallback cb: callbacks) {
            if (cb != null) cb.onDisconnect(client);
        }
    }

    public static DisconnectCallback getCallback() {
		return cb;
	}

}
