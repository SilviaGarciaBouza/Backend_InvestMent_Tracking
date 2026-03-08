package com.silviagarcia.investtracking.Investment_Tracking.repository;

import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/// Repositorio para realizar consultas sobre la tabla de usuarios.
public interface UserRepository extends JpaRepository<User, Long> {
    /// Busca un usuario por su nombre de usuario unico.
    Optional<User> findByUsername(String username);
}