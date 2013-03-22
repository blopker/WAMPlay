package test;

import static org.fest.assertions.Assertions.assertThat;
import models.messages.Welcome;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.WAMPlayServer;

public class WelcomeTest {
	TestClient client;

	@Before
	public void setUp() {
		client = new TestClient(null);
		WAMPlayServer.addClient(client);
	}

	@After
	public void tearDown(){
		WAMPlayServer.removeClient(client);
	}
	
	@Test
	public void welcomeTest() {
		client.send(new Welcome(client.getID()));
		assertThat(client.lastSent.toString()).contains(client.getID());
	}
}
