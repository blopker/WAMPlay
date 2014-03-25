package ws.wamplay.callbacks;

import ws.wamplay.models.WAMPlayClient;

public abstract class DisconnectCallback {
	public abstract void onDisconnect(WAMPlayClient client);
}
