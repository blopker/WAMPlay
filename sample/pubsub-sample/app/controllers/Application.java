package controllers;

import play.*;
import com.blopker.wamplay.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	
    public static Result index() {
    	WAMPlayServer.addTopic("hi");
        return ok(index.render("Your new application is ready."));
    }
  
}
