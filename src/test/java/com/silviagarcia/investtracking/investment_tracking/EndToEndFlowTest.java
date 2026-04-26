package com.silviagarcia.investtracking.investment_tracking;

import com.silviagarcia.investtracking.investment_tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.investment_tracking.model.Category;
import com.silviagarcia.investtracking.investment_tracking.repository.CategoryRepository;
import com.silviagarcia.investtracking.investment_tracking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EndToEndFlowTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // Limpiamos la base de datos antes de cada test para evitar errores de "usuario duplicado"
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void register_login_and_access_protected_endpoint_flow() {
        // Preparar cabeceras JSON para evitar el error de "Streaming mode"
        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 1) Registrar usuario
        Map<String, String> newUser = Map.of(
                "username", "e2e_user",
                "password", "Password123",
                "email", "e2e@example.com"
        );

        HttpEntity<Map<String, String>> registerRequest = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<Map> registerResp = restTemplate.postForEntity("/api/users/register", registerRequest, Map.class);

        assertThat(registerResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 2) Login para obtener token
        Map<String, String> credentials = Map.of(
                "username", "e2e_user",
                "password", "Password123"
        );

        HttpEntity<Map<String, String>> loginRequest = new HttpEntity<>(credentials, postHeaders);
        ResponseEntity<Map> loginResp = restTemplate.postForEntity("/api/users/login", loginRequest, Map.class);

        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).containsKey("token");
        String token = (String) loginResp.getBody().get("token");
        assertThat(token).isNotBlank();

        // 3) Preparar datos en BD (categoria)
        Category cat = new Category();
        cat.setName("E2E-Category");
        categoryRepository.save(cat);

        // 4) Consumir endpoint protegido con token
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token);
        authHeaders.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders);

        ResponseEntity<CategoryDTO[]> resp = restTemplate.exchange("/api/categories", HttpMethod.GET, entity, CategoryDTO[].class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();

        boolean found = java.util.Arrays.stream(resp.getBody())
                .anyMatch(c -> c.getName().equals("E2E-Category"));
        assertThat(found).isTrue();
    }
}