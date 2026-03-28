package com.silviagarcia.investtracking.Investment_Tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Representación ligera de una transacción financiera.
 * Incluye el ID del ítem padre para facilitar el mapeo en el cliente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    /** Cantidad de activos. */
    private Double stocks;
    /** Precio pagado por cada unidad en la compra. */
    private Double purchasePrice;
    /** Inversión total calculada en euros. */
    private Double invEur;
    /** Fecha  de la operación. */
    private LocalDateTime purchaseDate;
    /** ID del activo al que pertenece (evita recursividad infinita). */
    private Long itemId;
}