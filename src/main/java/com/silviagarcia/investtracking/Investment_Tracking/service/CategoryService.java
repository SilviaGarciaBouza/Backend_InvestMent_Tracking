package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.Investment_Tracking.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/// Servicio para gestionar las categorías de inversión disponibles.
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /// Recupera todas las categorías registradas y las convierte a DTO.
    /// @return Lista de CategoryDTO para ser consumida por el frontend.
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            return dto;
        }).collect(Collectors.toList());
    }
}