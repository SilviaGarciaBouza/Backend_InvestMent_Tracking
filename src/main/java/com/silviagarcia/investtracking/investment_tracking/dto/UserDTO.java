package com.silviagarcia.investtracking.investment_tracking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Datos públicos del usuario autenticado.
 * No incluye el campo 'password' por seguridad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}