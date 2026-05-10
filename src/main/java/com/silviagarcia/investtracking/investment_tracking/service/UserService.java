package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.UserDTO;
import com.silviagarcia.investtracking.investment_tracking.model.User;
import com.silviagarcia.investtracking.investment_tracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para la gestión de cuentas de usuario y seguridad.
 * Proporciona métodos para el registro y la búsqueda de perfiles.
 */
@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    /** Busca una entidad User por su nombre de usuario. */
    @Transactional(readOnly = true)
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    /** Busca una entidad User por su correo electrónico. */
    @Transactional(readOnly = true)
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Registra un nuevo usuario aplicando el cifrado BCrypt a la contraseña.
     * * @param user Entidad usuario con datos en bruto.
     * @return El {@link UserDTO} generado para el cliente.
     */
    @Transactional
    public UserDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}