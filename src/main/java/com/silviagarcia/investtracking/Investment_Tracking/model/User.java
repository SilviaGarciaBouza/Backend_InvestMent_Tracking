package com.silviagarcia.investtracking.Investment_Tracking.model;

import jakarta.persistence.*;

/// Entidad que representa la tabla de usuarios en MariaDB.
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column()
    private String password;

    private String email;

    public User() {}

    /// Obtiene el identificador unico del usuario.
    public Long getId() { return id; }

    /// Obtiene el nombre de usuario.
    public String getUsername() { return username; }

    /// Obtiene la contraseña encriptada.
    public String getPassword() { return password; }

    /// Define la contraseña (se usara para guardar el hash).
    public void setPassword(String password) { this.password = password; }

    /// Obtiene el correo electronico.
    public String getEmail() { return email; }
}