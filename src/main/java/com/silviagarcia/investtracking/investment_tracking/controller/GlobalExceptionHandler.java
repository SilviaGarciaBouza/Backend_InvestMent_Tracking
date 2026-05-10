package com.silviagarcia.investtracking.investment_tracking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Controlador de asesoramiento global para la gestión centralizada de excepciones.
 * Se encarga de capturar los errores lanzados por cualquier controlador y transformarlos
 * en respuestas JSON legibles para el cliente móvil.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Maneja las excepciones de seguridad cuando un usuario no tiene permisos suficientes.
     * * @param e Excepción de tipo {@link AccessDeniedException}.
     * @return Mapa con el mensaje de error "Acceso denegado".
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleForbidden(AccessDeniedException e) {
        return Map.of("error", "Acceso denegado");
    }
    /**
     * Captura y formatea los errores de validación de los DTOs (anotaciones @Valid).
     * Concatena los mensajes de error de todos los campos que hayan fallado en la validación.
     * * @param e Excepción lanzada cuando falla la validación de argumentos.
     * @return Mapa con el detalle de los campos afectados y sus respectivos mensajes de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return Map.of("error", message);
    }
}
