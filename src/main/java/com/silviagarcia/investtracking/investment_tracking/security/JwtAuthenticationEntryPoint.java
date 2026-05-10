package com.silviagarcia.investtracking.investment_tracking.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Punto de entrada para el manejo de fallos de autenticación.
 * Esta clase intercepta las peticiones de usuarios no autenticados que intentan
 * acceder a recursos protegidos y devuelve una respuesta 401 en formato JSON.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Se activa cuando se detecta un intento de acceso no autorizado.
     * Envía una respuesta HTTP 401 (Unauthorized) al cliente móvil con un mensaje
     * descriptivo del error para que la App pueda redirigir al Login.
     * * @param request La petición HTTP recibida.
     * @param response La respuesta HTTP saliente.
     * @param authException La excepción de seguridad capturada por Spring.
     * @throws IOException Si ocurre un error al escribir en la respuesta.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Establecemos el estado HTTP 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Construimos el JSON de error manual para evitar dependencias extra aquí
        response.getWriter().write("{\"error\": \"No autorizado\", \"message\": \"Token no válido o ausente\"}");

    }
}