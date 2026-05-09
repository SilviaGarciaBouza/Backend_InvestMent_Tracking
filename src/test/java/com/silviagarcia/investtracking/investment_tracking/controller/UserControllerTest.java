package com.silviagarcia.investtracking.investment_tracking.controller;

import com.silviagarcia.investtracking.investment_tracking.model.User;
import com.silviagarcia.investtracking.investment_tracking.security.JwtService;
import com.silviagarcia.investtracking.investment_tracking.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock private JwtService jwtService;
    @Mock private UserService userService;
    @Mock private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks private UserController userController;

    @Test
    void login_ConCredencialesCorrectas_DebeRetornarOkYToken() {
        User user = new User();
        user.setUsername("silvia");
        user.setPassword("password_encriptado");
        user.setEmail("silvia@test.com");

        Map<String, String> credenciales = Map.of("email", "silvia@test.com", "password", "123");

        when(userService.findEntityByEmail("silvia@test.com")).thenReturn(user);
        when(passwordEncoder.matches("123", "password_encriptado")).thenReturn(true);
        when(jwtService.generateToken("silvia@test.com")).thenReturn("token-fake");

        ResponseEntity<?> response = userController.login(credenciales);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("token-fake", body.get("token"));
    }

    @Test
    void login_ConPasswordIncorrecto_DebeRetornar401() {
        User user = new User();
        user.setUsername("silvia");
        user.setPassword("password_encriptado");
        user.setEmail("silvia@test.com");

        when(userService.findEntityByEmail("silvia@test.com")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "password_encriptado")).thenReturn(false);

        ResponseEntity<?> response = userController.login(Map.of("email", "silvia@test.com", "password", "wrong"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void login_UsuarioNoExiste_DebeRetornar401() {
        when(userService.findEntityByEmail("noexiste@test.com")).thenReturn(null);

        ResponseEntity<?> response = userController.login(Map.of("email", "noexiste@test.com", "password", "123"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void register_ConDatosValidos_DebeRetornar201() {
        User req = new User();
        req.setEmail("silvia@test.com");
        req.setPassword("pass");
        req.setUsername("silvia");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("silvia");
        savedUser.setEmail("silvia@test.com");

        com.silviagarcia.investtracking.investment_tracking.dto.UserDTO dto =
                new com.silviagarcia.investtracking.investment_tracking.dto.UserDTO(1L, "silvia", "silvia@test.com");
        when(userService.registerUser(any(User.class))).thenReturn(dto);

        ResponseEntity<com.silviagarcia.investtracking.investment_tracking.dto.UserDTO> response = userController.register(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("silvia", response.getBody().getUsername());
    }

    @Test
    void checkHealth_DebeRetornarOk() {
        ResponseEntity<Map<String, String>> response = userController.checkHealth();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ok", response.getBody().get("status"));
    }
}
