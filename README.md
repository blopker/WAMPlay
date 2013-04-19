![Logo](https://raw.github.com/blopker/WAMPlay/master/wamplay.png)

About
-----
This is a WAMP implementation for the Play! Framework. Use it to add RPC and Pub/Sub websocket functionality to your site!

**If this project helps you, please star it!**

Getting Started
---------------

### Build.scala

First add WAMPlay to appDependencies in the [Build.scala](https://github.com/blopker/WAMPlay/blob/master/sample/rpc/project/Build.scala) file:

```scala
"ws.wamplay" %% "wamplay" % "0.1.4"
```

Then add the WAMPlay repo in the same file:

```scala
val main = play.Project(appName, appVersion, appDependencies).settings(
  resolvers ++= Seq("WAMPlay Repository" at "http://blopker.github.com/maven-repo/")
)
```

### routes

Add a WAMP endpoint to your [routes](https://github.com/blopker/WAMPlay/blob/master/sample/rpc/conf/routes) file:

```
# Send websocket connections to the WAMPlay server
GET     /wamp                     ws.wamplay.controllers.WAMPlayServer.connect()
```

### WAMPlayController

Create a class that extends [WAMPlayController](https://github.com/blopker/WAMPlay/blob/master/sample/rpc/app/controllers/RPC.java). This is where your application will interact with clients. For more information on the annotations check out [Annotations](https://github.com/blopker/WAMPlay/wiki/Annotations).

```java
@URIPrefix("http://example.com/sample")
public class SampleController extends WAMPlayContoller {

	@onRPC("#meaningOfLife")
	public static String getMeaningOfLife(String sessionID) {
		return "Meaning of life is: 42";
	}

	@onRPC("#capital")
	public static String add(String sessionID, JsonNode[] args) {
		String ans = args[0].asText().toUpperCase();
		return ans;
	}

	@onSubscribe("/chat")
	public static boolean capitalSubscribe(String sessionID) {
		return true;
	}

	@onPublish("/chat")
	public static JsonNode truncatePublish(String sessionID, JsonNode event) {
		return Json.toJson(event);
	}
}
```

### Global.java

In your [Global.java](https://github.com/blopker/WAMPlay/blob/master/sample/rpc/app/Global.java) file override onStart and add your controller to the WAMPlayServer.

```java
public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		WAMPlayServer.addController(new SampleController());
	}
}
```

### Your server is ready!

Now head over to the [RPC Sample Index](https://github.com/blopker/WAMPlay/blob/master/sample/rpc/app/views/index.scala.html) to see how to use [AutobahnJS](http://autobahn.ws/js) with your new server!

Limitations
-----------
- No support for prefix messages

**This is currently in beta state, there may be bugs and the API may change**

Check out the [samples](https://github.com/blopker/WAMPlay/tree/master/sample) to see how to include WAMPlay in your app!
