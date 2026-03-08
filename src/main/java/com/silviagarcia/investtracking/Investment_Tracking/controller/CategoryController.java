package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/// Controlador para obtener las categorías de inversión disponibles.
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /// Devuelve la lista de todas las categorías para los selectores del frontend.
    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories();
    }
}