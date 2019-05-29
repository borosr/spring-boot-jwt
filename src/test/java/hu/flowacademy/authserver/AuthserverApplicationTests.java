package hu.flowacademy.authserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.expression.spel.ast.Literal;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthserverApplicationTests {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@LocalServerPort
	private int randomServerPort;
	private String url;
	private HttpClient client;
	private Map<String, String> authBody = Map.of(
			"grant_type", "password",
			"client_id", "fooClientIdPassword",
			"username", "john",
			"password", "123"
	);
	private String authBodyString = authBody.entrySet().stream()
			.map(Object::toString)
			.collect(Collectors.joining("&"));
	private String basicToken = "Basic " + Base64.getEncoder().encodeToString("fooClientIdPassword:secret".getBytes());

	@Before
	public void init() {
		url = "http://localhost:" + randomServerPort + "/";
		client = HttpClient.newHttpClient();
		logger.info("Context init ended! Port is: {}", randomServerPort);
	}

	@Test
	public void isPublicWorking() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.GET()
				.build();

		HttpResponse<String> send = client.send(request, BodyHandlers.ofString());
		Assert.assertEquals(HttpStatus.OK.value(), send.statusCode());
		Assert.assertEquals("Welcome on my JWT example page!", send.body());
		logger.info("Public works!");
	}

	@Test
	public void getSecureFail() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + "secure/"))
				.GET()
				.build();

		HttpResponse<String> send = client.send(request, BodyHandlers.ofString());
		Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), send.statusCode());
		logger.info("Secure is secure!");
	}

	@Test
	public void authenticateValid() throws IOException, InterruptedException {
		Assert.assertNotNull(authenticateAndGetResponse());
		logger.info("Auth success!");
  }

  @Test
	public void getSecureData() throws IOException, InterruptedException {
		JWTResponse token = authenticateAndGetResponse();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + "secure/"))
				.GET()
				.header("Authorization", "Bearer " + token.getAccessToken())
				.build();

		HttpResponse<String> send = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
		Assert.assertEquals(HttpStatus.OK.value(), send.statusCode());

		List<String> list = new Gson()
				.fromJson(send.body(), new TypeToken<List<String>>() {}.getType());

		Assert.assertEquals(3, list.size());

		logger.info("Secure works authenticated!");
	}

	private JWTResponse authenticateAndGetResponse() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + "oauth/token"))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Authorization", basicToken)
				.POST(BodyPublishers.ofString(authBodyString))
				.build();

		HttpResponse<String> send = client.send(request, BodyHandlers.ofString());
		Assert.assertEquals(HttpStatus.OK.value(), send.statusCode());

		return new Gson().fromJson(send.body(), JWTResponse.class);
	}

}
