package test;

import static org.fest.assertions.Assertions.assertThat;
import models.WAMPlayClient;
import models.messages.Welcome;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WelcomeTest {
	WAMPlayClient client;

	@Before
	public void setUp() {
		client = TestClientFactory.get();
	}

	@After
	public void tearDown() {
		client.kill();
	}

	@Test
	public void welcomeTest() {
		client.send(new Welcome(client.getID()));
		assertThat(client.testLastSent().toList().toString()).contains(client.getID());
	}
}
