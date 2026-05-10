package com.silviagarcia.investtracking.investment_tracking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Representación de los datos públicos del usuario autenticado.
 * Este objeto omite campos sensibles como la contraseña para cumplir con los
 * estándares de seguridad en la transmisión de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    /** Identificador único del usuario. */
    private Long id;
    /** Nombre de usuario para la visualización en el perfil. */
    private String username;
    /** Correo electrónico vinculado a la cuenta del usuario. */
    private String email;
}