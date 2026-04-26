package com.silviagarcia.investtracking.investment_tracking.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryModelTest {

    @Test
    @DisplayName("Debería guardar y recuperar el nombre correctamente")
    void testCategoryData() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Acciones");

        // Getter funciona (gracias a Lombok)
        assertEquals(1L, category.getId());
        assertEquals("Acciones", category.getName());
    }
}