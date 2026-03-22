package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.UserDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import com.silviagarcia.investtracking.Investment_Tracking.security.JwtService;
import com.silviagarcia.investtracking.Investment_Tracking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/// Controlador para gestionar la autenticacion mediante Token Bearer simulado.
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /// Valida credenciales y retorna el usuario con un token de sesion unico.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userService.findEntityByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("user", new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Error: Usuario o password incorrectos");
    }

    /// Procesa el registro de nuevos usuarios en el sistema.
    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {
        return userService.registerUser(user);
    }
}