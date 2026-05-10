package com.silviagarcia.investtracking.investment_tracking.controller;

import com.silviagarcia.investtracking.investment_tracking.dto.UserDTO;
import com.silviagarcia.investtracking.investment_tracking.model.User;
import com.silviagarcia.investtracking.investment_tracking.security.JwtService;
import com.silviagarcia.investtracking.investment_tracking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de seguridad y gestión de perfiles de usuario.
 * Maneja el acceso mediante JWT y el registro inicial.
 */
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

    /**
     * Autentica a un usuario y genera un token JWT de sesión.
     * @param credentials Mapa con 'username' y 'password'.
     * @return Respuesta con el DTO del usuario y su token, o error 401.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        User user = userService.findEntityByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtService.generateToken(user.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("user", new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Map.of("error", "Usuario o password incorrectos"));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param user Entidad usuario con los datos iniciales.
     * @return {@link UserDTO} del usuario registrado.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        UserDTO registered = userService.registerUser(user);
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }
    /**
     * Verifica la disponibilidad del servidor.
     * Utilizado por el cliente móvil para comprobar la conexión antes de sincronizar.
     * @return Mapa con el estado de salud del servicio.
     */

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}