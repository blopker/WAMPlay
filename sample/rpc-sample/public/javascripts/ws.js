function say (message, toClass) {
  $(toClass).append($('<h2>').text(message));
}

function wsSay (message) {
  say(message, ".websocket");
}

function wampSay (message) {
  say(message, ".wamplay");
}

function WebSocketTest()
{
  if ("WebSocket" in window)
  {
     wsSay("WebSocket is supported by your Browser!");
     // Let us open a web socket
     var ws = new WebSocket(wsuri);
     ws.onopen = function()
     {
        // Web Socket is connected, send data using send()
        ws.send(JSON.stringify(
              {text: "Message"}
          ));
        wsSay("Message is sent...");
     };
     ws.onmessage = function (evt)
     {
        var received_msg = evt.data;
        console.log(received_msg);
        wsSay("Message is received...");
     };
     ws.onclose = function()
     {
        // websocket is closed.
        wsSay("Connection is closed...");
     };
  }
  else
  {
     // The browser doesn't support WebSocket
     wsSay("WebSocket NOT supported by your Browser!");
  }
}

function WAMPTest() {
  wampSay("WAMPTest");
   // WAMP server
   ab.connect(wsuri,

      // WAMP session was established
      function (session) {
        wampSay("Connected to WAMP!");
        console.log(session);
         // asynchronous RPC, returns promise object
         session.call("http://example.com/simple/calc#add",
                      23, 7).then(

            // RPC success callback
            function (res) {
               wampSay("got result: " + res);
            },

            // RPC error callback
            function (error, desc) {
               wampSay("error: " + desc);
            }
         );

         session.subscribe("http://example.com/simple", function(topicUri, event) {
          wampSay("got result: " + event);
          console.log(topicUri);
          console.log(event);
         });
      },

      // WAMP session is gone
      function (code, reason) {
         console.log(reason);
         wampSay("WAMP session gone: " + desc);
      },
      {skipSubprotocolCheck:true, skipSubprotocolAnnounce:true}); // Important!!
}


// WebSocketTest();
WAMPTest();
