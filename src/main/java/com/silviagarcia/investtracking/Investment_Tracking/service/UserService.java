package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.UserDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import com.silviagarcia.investtracking.Investment_Tracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// Servicio encargado de la lógica de negocio relacionada con los usuarios.
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /// Busca un usuario por su nombre y lo devuelve como DTO.
    /// @param username Nombre de usuario a buscar.
    /// @return Un DTO con la información pública del usuario.
    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            UserDTO dto = new UserDTO();
            dto.setId(user.get().getId());
            dto.setUsername(user.get().getUsername());
            dto.setEmail(user.get().getEmail());
            return dto;
        }
        return null;
    }

    /// Registra un nuevo usuario en el sistema.
    public UserDTO registerUser(User user) {
        User savedUser = userRepository.save(user);
        UserDTO dto = new UserDTO();
        dto.setId(savedUser.getId());
        dto.setUsername(savedUser.getUsername());
        dto.setEmail(savedUser.getEmail());
        return dto;
    }
}