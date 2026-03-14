package com.silviagarcia.investtracking.Investment_Tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
@Service
public class PriceService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiKey = "e0415dc7f13d491e96a110c088158e6e";
    private final String baseUrl = "https://api.twelvedata.com/price";

    public Double getRealTimePrice(String symbol) {
        try {
            // 1. Configuramos la URL y el Header de seguridad
            String url = baseUrl + "?symbol=" + symbol;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "apikey " + apiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 2. Hacemos la llamada
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Twelve Data devuelve el precio como un String: {"price": "123.45"}
                String priceStr = (String) response.getBody().get("price");
                return priceStr != null ? Double.parseDouble(priceStr) : 0.0;
            }
        } catch (Exception e) {
            System.err.println("Error consultando precio para " + symbol + ": " + e.getMessage());
        }
        // Fallback si algo falla
        return 0.0;
    }
}
