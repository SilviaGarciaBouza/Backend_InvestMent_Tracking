package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.UserDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import com.silviagarcia.investtracking.Investment_Tracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para la gestión de cuentas de usuario y seguridad.
 */
@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Registra un nuevo usuario aplicando hashing a la contraseña.
     * @param user Entidad con datos en bruto.
     * @return {@link UserDTO} con la información pública del usuario.
     */
    @Transactional
    public UserDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}