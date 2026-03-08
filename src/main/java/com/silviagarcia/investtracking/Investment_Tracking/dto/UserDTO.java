package com.silviagarcia.investtracking.Investment_Tracking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/// Objeto para enviar datos del usuario al frontend sin incluir la contraseña.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}