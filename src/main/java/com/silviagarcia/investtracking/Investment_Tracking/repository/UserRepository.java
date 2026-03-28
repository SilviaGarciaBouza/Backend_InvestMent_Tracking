package com.silviagarcia.investtracking.Investment_Tracking.repository;

import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la gestión y consulta de usuarios.
 * Fundamental para el sistema de seguridad y autenticación mediante JWT.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Localiza un usuario basándose en su nombre de usuario único.
     * @param username Nombre del usuario a buscar.
     * @return Un {@link Optional} que contiene el usuario si existe.
     */
    Optional<User> findByUsername(String username);
}