package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.Category;
import com.silviagarcia.investtracking.Investment_Tracking.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("Deberia mapear correctamente las categorias a DTOs")
    void testGetAllCategories() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Cripto");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Acciones");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<CategoryDTO> resultado = categoryService.getAllCategories();

        // 3. Assert: Verificamos que todo este correcto
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Cripto", resultado.get(0).getName());

        assertEquals(2L, resultado.get(1).getId());
        assertEquals("Acciones", resultado.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
    }
}