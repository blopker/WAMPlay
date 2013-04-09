package com.blopker.wamplay.models.messages;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

public class CallResult implements Message{
	final JsonNode result;
	final String procURI;
	final List<Object> res = new ArrayList<Object>();
	
	public CallResult(String procURI, JsonNode result) {
		this.procURI = procURI;
		this.result = result;
		
		res.add(getType().getTypeCode());
		res.add(procURI);
		res.add(result);
	}	
	
	@Override
	public MessageTypes getType() {
		return MessageTypes.CALLRESULT;
	}

	@Override
	public List<Object> toList() {
		return res;
	}

}
