package com.silviagarcia.investtracking.investment_tracking.security.config;

import com.silviagarcia.investtracking.investment_tracking.security.JwtAuthenticationEntryPoint;
import com.silviagarcia.investtracking.investment_tracking.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración central de seguridad de la aplicación basada en Spring Security.
 * Establece los protocolos de protección para la API REST, implementando una
 * arquitectura de autenticación mediante tokens JWT y comunicación sin estado (Stateless).
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Define la cadena de filtros de seguridad (Security Filter Chain).
     * Configura la desactivación de CSRF (innecesario en APIs sin estado),
     * establece las rutas de acceso público y privado, y gestiona el manejo de errores
     * de autenticación y autorización para devolver respuestas JSON legibles.
     * * @param http Objeto {@link HttpSecurity} para personalizar la seguridad web.
     * @return El {@link SecurityFilterChain} que procesará cada petición HTTP.
     * @throws Exception Si existe algún error en la definición de las reglas de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Se desactiva CSRF ya que la autenticación mediante Token previene este ataque
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.setCharacterEncoding("UTF-8");
                            res.getWriter().write("{\"error\": \"Acceso denegado\"}");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        //Endpoints públicos: login, registro y comprobación de salud
                        .requestMatchers("/api/users/login", "/api/users/register", "/api/users/health").permitAll()
                        // Cualquier otra ruta requiere un token JWT válido
                        .anyRequest().authenticated()
                )


                .sessionManagement(session -> session
                        // Se establece política STATELESS: no se guardará estado en el servidor (sin cookies/JSESSIONID)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define el algoritmo BCrypt para el cifrado de contraseñas de los usuarios.
     * Este componente es esencial para almacenar de forma segura las credenciales en la base de datos MariaDB.
     * * @return Instancia de {@link BCryptPasswordEncoder} utilizada por el servicio de usuarios.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}