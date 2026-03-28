package com.silviagarcia.investtracking.Investment_Tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

/**
 * Servicio de conexión con la API externa Twelve Data.
 * Proporciona precios de mercado en tiempo real para los activos financieros.
 */
@Service
public class PriceService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiKey = "e0415dc7f13d491e96a110c088158e6e";
    private final String baseUrl = "https://api.twelvedata.com/price";

    /**
     * Consulta el precio actual de un símbolo financiero.
     * @param symbol Símbolo del activo .
     * @return Precio actual o 0.0 en caso de error o símbolo no encontrado.
     */
    public Double getRealTimePrice(String symbol) {
        try {
            String url = baseUrl + "?symbol=" + symbol;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "apikey " + apiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String priceStr = (String) response.getBody().get("price");
                return priceStr != null ? Double.parseDouble(priceStr) : 0.0;
            }
        } catch (Exception e) {
            System.err.println("Error en API para " + symbol + ": " + e.getMessage());
        }
        return 0.0;
    }
}