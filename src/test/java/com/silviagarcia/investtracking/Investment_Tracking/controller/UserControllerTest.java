package com.silviagarcia.investtracking.Investment_Tracking.controller;


import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import com.silviagarcia.investtracking.Investment_Tracking.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.silviagarcia.investtracking.Investment_Tracking.security.JwtService;
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
        // Arrange
        User user = new User();
        user.setUsername("silvia");
        user.setPassword("password_encriptado");

        Map<String, String> credenciales = Map.of("username", "silvia", "password", "123");

        when(userService.findEntityByUsername("silvia")).thenReturn(user);
        when(passwordEncoder.matches("123", "password_encriptado")).thenReturn(true);
        when(jwtService.generateToken("silvia")).thenReturn("token-fake");

        // Act
        ResponseEntity<?> response = userController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("token-fake", body.get("token"));
    }

    @Test
    void login_ConPasswordIncorrecto_DebeRetornar401() {
        // Arrange
        User user = new User();
        user.setUsername("silvia");
        user.setPassword("password_encriptado");

        when(userService.findEntityByUsername("silvia")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "password_encriptado")).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.login(Map.of("username", "silvia", "password", "wrong"));

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
