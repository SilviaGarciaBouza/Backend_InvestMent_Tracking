package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.investment_tracking.model.Category;
import com.silviagarcia.investtracking.investment_tracking.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio para la gestión de categorías.
 * Actúa como capa intermedia entre el repositorio de persistencia y los
 * controladores REST, realizando la transformación de entidades a DTOs.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Recupera el catálogo completo de categorías disponibles en el sistema.
     * Realiza un mapeo directo de la entidad Category al objeto de transporte CategoryDTO.
     * * @return Lista de {@link CategoryDTO} con todas las categorías registradas.
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            return dto;
        }).collect(Collectors.toList());
    }

}