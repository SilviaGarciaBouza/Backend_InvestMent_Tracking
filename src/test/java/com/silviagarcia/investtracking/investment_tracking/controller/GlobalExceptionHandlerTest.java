package com.silviagarcia.investtracking.investment_tracking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleForbidden_ShouldReturnAccessDeniedMessage() {
        Map<String, String> result = handler.handleForbidden(new AccessDeniedException("denied"));

        assertThat(result).containsEntry("error", "Acceso denegado");
    }

    @Test
    void handleValidation_ShouldReturnFieldErrors() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "req");
        bindingResult.addError(new FieldError("req", "email", "must not be blank"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException((MethodParameter) null, bindingResult);

        Map<String, String> result = handler.handleValidation(ex);

        assertThat(result).containsKey("error");
        assertThat(result.get("error")).contains("email");
    }
}
