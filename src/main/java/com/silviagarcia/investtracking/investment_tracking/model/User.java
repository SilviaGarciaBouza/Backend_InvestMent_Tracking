package com.silviagarcia.investtracking.investment_tracking.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad para la gestión de usuarios y credenciales.
 */
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario único y obligatorio. */
    @Column( nullable = false, length = 255)
    private String username;

    /** Contraseña encriptada. Obligatoria. */
    @Column(nullable = false, length = 255)
    private String password;

    /** Correo electrónico. Opcional. */
    @Column(unique = true, length = 255, nullable = false)
    private String email;



}