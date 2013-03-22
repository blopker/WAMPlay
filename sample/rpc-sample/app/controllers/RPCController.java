package controllers;

import actions.WAMPlayProcedure;
import actions.WAMPlayURI;

@WAMPlayURI("rpc")
public class RPCController {
	
	@WAMPlayProcedure("add")
	public static int add(int x, int y) {
		return x + y;
	}
	
	@WAMPlayProcedure("subtract")
	public static int subtract(int x, int y) {
		return x - y;
	}
}
