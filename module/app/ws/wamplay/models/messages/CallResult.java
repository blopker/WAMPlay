package ws.wamplay.models.messages;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class CallResult extends Message{
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
	public MessageType getType() {
		return MessageType.CALLRESULT;
	}

	@Override
	public List<Object> toList() {
		return res;
	}

}
