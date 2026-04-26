package com.silviagarcia.investtracking.investment_tracking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Servicio encargado de la generación, extracción y validación de tokens JWT.
 * Utiliza el algoritmo HS256 y una clave secreta personalizada.
 */
@Service
public class JwtService {

    /** Clave secreta para la firma de los tokens (inyectada desde properties). */
    private final String secretKey;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    private void validateSecret() {
        if (this.secretKey == null || this.secretKey.length() < 32) {
            throw new IllegalStateException("jwt.secret must be set and at least 32 characters long");
        }
    }

    /** Genera la clave de firma a partir de la cadena secreta. */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Genera un nuevo token JWT para un usuario con validez de 10 horas.
     * @param username El nombre de usuario a identificar en el token.
     * @return El string del token JWT generado.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extrae el nombre de usuario (Subject) del token. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Método genérico para extraer cualquier información (Claim) del token. */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    /** Verifica si el token pertenece al usuario y no ha expirado. */
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /** Comprueba si el tiempo de validez del token ha finalizado. */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}