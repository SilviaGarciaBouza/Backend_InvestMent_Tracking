package com.silviagarcia.investtracking.Investment_Tracking.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Punto de entrada para manejar errores de autenticación en el sistema JWT.
 * * Esta clase se activa cuando un usuario no autenticado intenta acceder
 * a un recurso protegido. En lugar de redireccionar, responde con un error 401
 * en formato JSON, ideal para el consumo desde la App móvil.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Se ejecuta cuando falla la autenticación.
     * * Configura la respuesta HTTP con estado 401 (Unauthorized) y un cuerpo
     * en formato JSON detallando el motivo del rechazo.
     * * @param request La petición entrante.
     * @param response La respuesta que se enviará al cliente.
     * @param authException La excepción de seguridad capturada.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Establecemos el estado HTTP 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Construimos el JSON de error manual para evitar dependencias extra aquí
        String jsonResponse = String.format(
                "{\"error\": \"No autorizado\", \"message\": \"%s\"}",
                authException.getMessage()
        );

        response.getWriter().write(jsonResponse);
    }
}