package com.silviagarcia.investtracking.investment_tracking.repository;

import com.silviagarcia.investtracking.investment_tracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la gestión de usuarios.
 * Componente crítico para el proceso de autenticación y validación de tokens JWT.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de cuenta.
     * * @param username Nombre de usuario único.
     * @return Un {@link Optional} con el usuario encontrado para su validación de credenciales.
     */
    Optional<User> findByUsername(String username);
    /**
     * Busca un usuario por su dirección de correo electrónico.
     * Método principal utilizado durante el proceso de Login.
     * * @param email Correo electrónico registrado.
     * @return Un {@link Optional} con el usuario para verificar la contraseña.
     */
    Optional<User> findByEmail(String email);
}