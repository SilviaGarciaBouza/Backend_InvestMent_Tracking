package com.silviagarcia.investtracking.investment_tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silviagarcia.investtracking.investment_tracking.controller.CategoryController;
import com.silviagarcia.investtracking.investment_tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.investment_tracking.service.CategoryService;
import com.silviagarcia.investtracking.investment_tracking.security.JwtService; // Importante
import com.silviagarcia.investtracking.investment_tracking.security.JwtAuthenticationEntryPoint; // Importante
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasItem;

@WebMvcTest(CategoryController.class)
class CategoryControllerTopDownTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    // NECESARIOS PARA QUE LA SEGURIDAD NO ROMPA EL TEST
    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser // Simula un usuario autenticado para saltar el filtro de seguridad
    void getAll_returnsListFromService_asJson() throws Exception {
        // 1. Preparación de datos (Given)
        CategoryDTO dto = new CategoryDTO();
        dto.setId(1L);
        dto.setName("Acciones");

        when(categoryService.getAllCategories()).thenReturn(List.of(dto));

        // 2. Ejecución y Verificación (When & Then)
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                // Verificamos que el JSON devuelto tenga el nombre "Acciones"
                .andExpect(jsonPath("$[0].name").value("Acciones"));
    }
}