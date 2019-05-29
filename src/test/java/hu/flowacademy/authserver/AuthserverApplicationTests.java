package hu.flowacademy.authserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthserverApplicationTests {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@LocalServerPort
	private int randomServerPort;

	@Test
	public void contextLoads() {
		logger.info("Context init ended! Port is: {}", randomServerPort);
	}

}
