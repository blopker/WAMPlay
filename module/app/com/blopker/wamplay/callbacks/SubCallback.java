package com.blopker.wamplay.callbacks;

import com.blopker.wamplay.models.WAMPlayClient;

public abstract class SubCallback {
	protected abstract WAMPlayClient onSubscribe(WAMPlayClient subscribingClient);
}
