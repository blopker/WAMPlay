package test;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.blopker.wamplay.models.WAMPlayClient;
import com.blopker.wamplay.models.messages.Welcome;

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
