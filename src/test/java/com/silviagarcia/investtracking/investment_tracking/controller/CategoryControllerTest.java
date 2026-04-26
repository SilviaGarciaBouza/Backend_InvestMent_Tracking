package com.silviagarcia.investtracking.investment_tracking.controller;


import com.silviagarcia.investtracking.investment_tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.investment_tracking.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock private CategoryService categoryService;
    @InjectMocks private CategoryController categoryController;

    @Test
    void getAll_DebeRetornarListaDeCategorias() {
        // Arrange
        List<CategoryDTO> lista = List.of(new CategoryDTO(1L, "Cripto"));
        when(categoryService.getAllCategories()).thenReturn(lista);

        // Act
        List<CategoryDTO> result = categoryController.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Cripto", result.get(0).getName());
    }
}