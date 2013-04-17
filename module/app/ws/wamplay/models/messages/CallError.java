package ws.wamplay.models.messages;

import java.util.ArrayList;
import java.util.List;

public class CallError extends Message {
	final List<Object> res = new ArrayList<Object>();
	
	public CallError(String callID, String errorURI, String errorDesc) {
		construct(callID, errorURI, errorDesc, null);
	}
	
	public CallError(String callID, String errorURI, String errorDesc, String errorDetails){
		construct(callID, errorURI, errorDesc, errorDetails);
	}
	
	private void construct(String callID, String errorURI, String errorDesc, String errorDetails){
		res.add(getType().getTypeCode());
		res.add(callID);
		res.add(errorURI);
		res.add(errorDesc);
		if (errorDesc != null) {
			res.add(errorDetails);
		}
	}
	
	@Override
	public MessageType getType() {
		return MessageType.CALLERROR;
	}

	@Override
	public List<Object> toList() {
		return res;
	}

}
