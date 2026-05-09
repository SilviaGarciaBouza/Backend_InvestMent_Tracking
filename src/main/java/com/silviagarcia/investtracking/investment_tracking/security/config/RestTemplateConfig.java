package com.silviagarcia.investtracking.investment_tracking.security.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuración para el cliente de peticiones HTTP.
 * Define el Bean de RestTemplate utilizado por el servicio de precios (PriceService)
 * para realizar consultas a la API externa de Twelve Data.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Crea una instancia compartida de RestTemplate.
     * @return Una nueva instancia de {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}