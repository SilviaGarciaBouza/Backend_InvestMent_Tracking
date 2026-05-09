package com.silviagarcia.investtracking.investment_tracking.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String email = "silvia@test.com";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("ClaveTemporalWirtz2026ClavePorDefectoSegura1234");
    }

    @Test
    @DisplayName("Debería generar un token y extraer el email correctamente")
    void testGenerateAndExtractEmail() {
        String token = jwtService.generateToken(email);

        assertNotNull(token);
        String extracted = jwtService.extractEmail(token);
        assertEquals(email, extracted);
    }

    @Test
    @DisplayName("Debería validar un token correcto")
    void testTokenValidation() {
        String token = jwtService.generateToken(email);
        assertTrue(jwtService.isTokenValid(token, email));
    }

    @Test
    @DisplayName("Debería invalidar un token si el email no coincide")
    void testTokenInvalidForDifferentUser() {
        String token = jwtService.generateToken(email);
        assertFalse(jwtService.isTokenValid(token, "otro@test.com"));
    }
}
