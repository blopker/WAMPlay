package com.blopker.wamplay.models.messages;

import java.util.ArrayList;
import java.util.List;

import com.blopker.wamplay.controllers.WAMPlayServer;


public class Welcome extends Message{
	final String clientID;
	
	public Welcome(String clientID) {
		this.clientID = clientID;
	}
	
	@Override
	public MessageTypes getType() {
		return MessageTypes.WELCOME;
	}

	@Override
	public List<Object> toList() {
		List<Object> res = new ArrayList<Object>();
		res.add(getType().getTypeCode());
		res.add(this.clientID);
		res.add(WAMPlayServer.PROTOCOL_VERSION);
		res.add(WAMPlayServer.VERSION);
		return res;
	}

}
