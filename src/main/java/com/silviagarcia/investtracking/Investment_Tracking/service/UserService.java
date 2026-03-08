package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.UserDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import com.silviagarcia.investtracking.Investment_Tracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/// Servicio que gestiona la persistencia y validacion de usuarios.
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /// Busca la entidad completa del usuario incluyendo su contraseña.
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /// Busca un usuario y transforma la entidad en un DTO seguro.
    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail())).orElse(null);
    }

    /// Registra un usuario nuevo encriptando su contraseña con BCrypt.
    public UserDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}