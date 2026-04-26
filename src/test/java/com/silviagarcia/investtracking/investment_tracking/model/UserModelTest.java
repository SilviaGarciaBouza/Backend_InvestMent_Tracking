package com.silviagarcia.investtracking.investment_tracking.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    @DisplayName("Deberia registrar correctamente los datos basicos del usuario")
    void testUserData() {
        User user = new User();

        user.setId(1L);
        user.setUsername("silvia_investor");
        user.setPassword("securePassword123");
        user.setEmail("silvia@invest.com");

        assertEquals(1L, user.getId());
        assertEquals("silvia_investor", user.getUsername());
        assertEquals("securePassword123", user.getPassword());
        assertEquals("silvia@invest.com", user.getEmail());
    }

    @Test
    @DisplayName("Deberia permitir campos nulos si no hay restricciones de logica en el POJO")
    void testUserNullValues() {
        User user = new User();
        assertNull(user.getUsername());
        assertNull(user.getEmail());
    }
}