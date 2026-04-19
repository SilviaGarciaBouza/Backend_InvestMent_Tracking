package com.silviagarcia.investtracking.Investment_Tracking.security;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String username = "silvia_user";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    @DisplayName("Debería generar un token y extraer el username correctamente")
    void testGenerateAndExtractUsername() {
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        String extracted = jwtService.extractUsername(token);
        assertEquals(username, extracted);
    }

    @Test
    @DisplayName("Debería validar un token correcto")
    void testTokenValidation() {
        String token = jwtService.generateToken(username);
        assertTrue(jwtService.isTokenValid(token, username));
    }

    @Test
    @DisplayName("Debería invalidar un token si el username no coincide")
    void testTokenInvalidForDifferentUser() {
        String token = jwtService.generateToken(username);
        assertFalse(jwtService.isTokenValid(token, "otro_usuario"));
    }
}

