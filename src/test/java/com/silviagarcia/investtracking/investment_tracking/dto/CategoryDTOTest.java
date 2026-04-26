package com.silviagarcia.investtracking.investment_tracking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryDTOTest {

    @Test
    @DisplayName("Debería crear un CategoryDTO y mantener la integridad de sus datos")
    void testCategoryDTO() {
        // Probamos el constructor con argumentos
        CategoryDTO dto = new CategoryDTO(1L, "Criptomonedas");

        assertEquals(1L, dto.getId());
        assertEquals("Criptomonedas", dto.getName());

        // Probamos el constructor vacío y los setters
        CategoryDTO emptyDto = new CategoryDTO();
        emptyDto.setName("Acciones");
        assertEquals("Acciones", emptyDto.getName());
    }
}
