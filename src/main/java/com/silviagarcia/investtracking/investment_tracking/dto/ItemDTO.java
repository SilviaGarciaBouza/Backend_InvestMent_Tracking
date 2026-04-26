package com.silviagarcia.investtracking.investment_tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO  para visualizar un activo financiero.
 * Agrupa la categoría, el historial de transacciones y el valor de mercado actual.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long id;
    private String name;
    /** Datos  de la categoría asociada. */
    private CategoryDTO category;
    /** Listado de transacciones asociadas a este activo. */
    private List<TransactionDTO> transactions;
    /** * Precio actual de mercado (obtenido de API externa).
     * No persiste en DB, se calcula para el frontend.
     */
    private Double currentPrice;
}