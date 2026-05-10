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
 * Servicio especializado en la criptografía de los JSON Web Tokens (JWT).
 * Proporciona las herramientas necesarias para la generación de tokens firmados,
 * la extracción de información (claims) y la verificación de integridad y expiración.
 */
@Service
public class JwtService {

    /** Clave secreta para la firma de los tokens (inyectada desde properties). */
    private final String secretKey;
    /**
     * Constructor que inyecta la clave secreta desde el archivo de configuración.
     * @param secretKey Cadena de caracteres secreta (mínimo 32 caracteres para HS256).
     */
    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }
    /** Valida que la clave secreta cumpla con los requisitos mínimos de seguridad al arrancar. */
    @PostConstruct
    private void validateSecret() {
        if (this.secretKey == null || this.secretKey.length() < 32) {
            throw new IllegalStateException("jwt.secret must be set and at least 32 characters long");
        }
    }

    /** Obtiene la clave de firma compatible con HMAC a partir de la clave secreta. */    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Genera un token JWT con el email del usuario como identificador (Subject).
     * El token se firma mediante HS256 y tiene una duración de 10 horas.
     * * @param email Correo electrónico del usuario autenticado.
     * @return String con el token JWT compacto.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))

                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extrae el correo electrónico contenido en el 'Subject' del token. */    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Método genérico para extraer información específica (Claims) del token.
     * @param <T> Tipo de dato esperado del Claim.
     * @param token El JWT a analizar.
     * @param claimsResolver Función para resolver el claim deseado.
     * @return El valor del claim extraído.
     */    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    /**
     * Valida si un token es íntegro, pertenece al usuario indicado y no ha caducado.
     * * @param token El JWT a validar.
     * @param email El email del usuario que realiza la petición.
     * @return true si el token es válido; false en caso contrario.
     */    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    /** Determina si el tiempo de vida del token ha sido superado. */    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}