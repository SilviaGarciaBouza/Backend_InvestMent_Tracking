package com.silviagarcia.investtracking.Investment_Tracking.dto;

import lombok.Data;

/**
 * DTO para gestionar las categorías de inversión.
 * Se utiliza principalmente para llenar los selectores (dropdowns) en la App.
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
}