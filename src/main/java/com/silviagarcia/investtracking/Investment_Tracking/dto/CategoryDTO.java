package com.silviagarcia.investtracking.Investment_Tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia para categorías.
 * Optimizado para cargar selectores y etiquetas en la interfaz de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    /** Identificador único de la categoría. */
    private Long id;
    /** Nombre descriptivo */
    private String name;
}