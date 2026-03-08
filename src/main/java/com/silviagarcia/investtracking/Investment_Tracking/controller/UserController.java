package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.UserDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import com.silviagarcia.investtracking.Investment_Tracking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/// Controlador para gestionar la autenticación y perfiles de usuario.
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /// Endpoint para iniciar sesión.
    /// Busca al usuario por su nombre y devuelve su información pública.
    @GetMapping("/login/{username}")
    public UserDTO login(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /// Endpoint para registrar un nuevo usuario en la base de datos.
    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {
        return userService.registerUser(user);
    }
}