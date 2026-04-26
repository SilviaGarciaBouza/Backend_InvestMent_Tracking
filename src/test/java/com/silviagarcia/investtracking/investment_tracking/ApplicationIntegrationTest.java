package com.silviagarcia.investtracking.investment_tracking;

import com.silviagarcia.investtracking.investment_tracking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
//Arrancamoos el contexto de Spring Boot en un puerto aleatorio (RANDOM_PORT).
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationIntegrationTest {
	@Autowired
	private UserRepository userRepository;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
//Verificams que UserRepository se inyecta (no nulo)
	@Test
	void contextLoads_and_repositoryIsAvailable() {
		assertThat(userRepository).isNotNull();
	}
//Hacemos una petición HTTP al root ("/") usando TestRestTemplate y comprueba que llega una respuesta HTTP válida.
	@Test
	void serverResponds_onRootEndpoint() {
		var response = restTemplate.getForEntity("http://localhost:" + port + "/", String.class);
		// Solo comprobar que el servidor responde (200, 404 u otro código HTTP válido)
		assertThat(response.getStatusCode()).isNotNull();
	}
}

