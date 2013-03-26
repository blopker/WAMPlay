$(function() {



  // connect to WAMP server
  ab.connect(wsuri,

    // WAMP session was established
    function (session) {
      console.log("Connected!");

      session.subscribe("http://example.com/simple", onEvent);
      session.subscribe("http://example.com/simple/subtopic", onEvent);

      $('#simple-publish').click(function() {session.publish("http://example.com/simple", "Simple!");});
      $('#subtopic-publish').click(function() {session.publish("http://example.com/simple/subtopic", "Subtopic!");});
    },

    // WAMP session is gone
    function (code, reason) {
      console.log("Lost Connection!");
    },
    {skipSubprotocolCheck:true, skipSubprotocolAnnounce:true} // Important!!
  );
});

function onEvent (topicURI, event) {
  console.log(topicURI);
  console.log(event);
}
