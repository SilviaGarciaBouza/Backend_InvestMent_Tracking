package com.silviagarcia.investtracking.Investment_Tracking.model;

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
    @Column(unique = true, nullable = false, length = 255)
    private String username;

    /** Contraseña encriptada. Obligatoria. */
    @Column(nullable = false, length = 255)
    private String password;

    /** Correo electrónico. Opcional. */
    @Column(length = 255)
    private String email;


    //TODO borar lo de abajo:
    /// Obtiene el identificador unico del usuario.
   // public Long getId() { return id; }

    /// Obtiene el nombre de usuario.
   // public String getUsername() { return username; }

    /// Obtiene la contraseña encriptada.
  //  public String getPassword() { return password; }

    /// Define la contraseña (se usara para guardar el hash).
   // public void setPassword(String password) { this.password = password; }

    /// Obtiene el correo electronico.
   // public String getEmail() { return email; }
}