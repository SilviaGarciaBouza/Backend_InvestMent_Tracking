package com.silviagarcia.investtracking.investment_tracking.security.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuración para el cliente de peticiones HTTP de la aplicación.
 * Define y centraliza el Bean de {@link RestTemplate} que permite realizar llamadas
 * a servicios externos, como la API de cotizaciones financieras.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Configura un Bean de RestTemplate con límites de tiempo de espera.
     * Los timeouts se establecen para evitar que la aplicación se bloquee
     * si el servicio externo (Twelve Data) tarda demasiado en responder.
     * * @param builder Constructor preconfigurado por Spring Boot.
     * @return Una instancia de {@link RestTemplate} con límites de conexión de 3s y lectura de 5s.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}