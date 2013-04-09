package com.blopker.wamplay.controllers.messageHandlers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.blopker.wamplay.callbacks.RPCCallback;
import com.blopker.wamplay.models.RPC;
import com.blopker.wamplay.models.WAMPlayClient;
import com.blopker.wamplay.models.messages.CallResult;

public class RPCHandler implements MessageHandler{

	@Override
	public void process(WAMPlayClient client, JsonNode message) {
		String callID = message.get(1).asText();
		String procURI = message.get(2).asText();
		
		List<JsonNode> args = new ArrayList<JsonNode>();
		
		for (int i = 3; i < message.size(); i++) {
			args.add(message.get(i));
		}

		RPCCallback cb = RPC.getCallback(procURI);
		if (cb == null) {
			//procURI not found
		}
		JsonNode response = cb.call(client, args.toArray(new JsonNode[args.size()]));
		client.send(new CallResult(callID, response));
	}

}
