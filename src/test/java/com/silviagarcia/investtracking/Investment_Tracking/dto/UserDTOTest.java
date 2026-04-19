package com.silviagarcia.investtracking.Investment_Tracking.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    @DisplayName("Debería mapear los datos públicos del usuario correctamente")
    void testUserDTO() {
        UserDTO dto = new UserDTO(1L, "silvia_invest", "silvia@mail.com");

        assertAll("Verificación de campos del UserDTO",
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("silvia_invest", dto.getUsername()),
                () -> assertEquals("silvia@mail.com", dto.getEmail())
        );
    }
}
