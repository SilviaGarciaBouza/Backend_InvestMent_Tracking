package com.silviagarcia.investtracking.investment_tracking.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock private JwtService jwtService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Si no hay cabecera Authorization, debe continuar sin autenticar")
    void doFilter_SinCabecera_DebeContinuar() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Si el token es valido, debe establecer la autenticacion en el contexto")
    void doFilter_ConTokenValido_DebeAutenticar() throws Exception {
        String token = "Bearer token-valido";
        String jwt = "token-valido";
        String email = "silvia@test.com";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractEmail(jwt)).thenReturn(email);
        when(jwtService.isTokenValid(jwt, email)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Si el token ha expirado, debe retornar 401")
    void doFilter_ConTokenExpirado_DebeRetornar401() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token-expirado");
        when(jwtService.extractEmail("token-expirado"))
                .thenThrow(new ExpiredJwtException(null, null, "expired"));

        PrintWriter writer = new PrintWriter(new StringWriter());
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("Si el token es invalido, debe retornar 403")
    void doFilter_ConTokenInvalido_DebeRetornar403() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token-basura");
        when(jwtService.extractEmail("token-basura"))
                .thenThrow(new RuntimeException("malformed token"));

        PrintWriter writer = new PrintWriter(new StringWriter());
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(filterChain, never()).doFilter(any(), any());
    }
}
