package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de categorías de inversión.
 * Proporciona los catálogos necesarios para clasificar activos en la App.
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Recupera todas las categorías disponibles.
     * @return Lista de objetos {@link CategoryDTO}.
     */
    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories();
    }
}