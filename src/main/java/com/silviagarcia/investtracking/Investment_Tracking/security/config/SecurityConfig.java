package com.silviagarcia.investtracking.Investment_Tracking.security.config;

import com.silviagarcia.investtracking.Investment_Tracking.security.JwtAuthenticationEntryPoint;
import com.silviagarcia.investtracking.Investment_Tracking.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración principal de Spring Security.
 * Define la política de acceso, la gestión de sesiones sin estado (Stateless)
 * y la integración del filtro de seguridad JWT.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Configura la cadena de filtros de seguridad.
     * Define qué rutas son públicas (login/registro) y cuáles requieren autenticación.
     * @param http Objeto para configurar la seguridad web.
     * @return El {@link SecurityFilterChain} configurado.
     * @throws Exception Si ocurre un error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Rutas abiertas
                        .requestMatchers("/api/users/login", "/api/users/register", "/api/users/health").permitAll()
                        .anyRequest().authenticated() // Todo lo demás requiere Token
                )
                .sessionManagement(session -> session
                        // No se crean cookies de sesión
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean para el algoritmo de hashing de contraseñas.
     * Utilizado en el registro y login para validar credenciales en MariaDB.
     * @return Una instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}