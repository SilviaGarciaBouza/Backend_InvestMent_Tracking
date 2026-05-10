package com.silviagarcia.investtracking.investment_tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Servicio encargado de la comunicación con APIs externas de cotización.
 * Soporta activos de tipo Forex, Criptomonedas y Acciones.
 */
@Service
public class PriceService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${finnhub.api.key}")
    private String finnhubKey;
    /**
     * Obtiene los precios de mercado para una lista de símbolos en paralelo.
     * * @param symbols Cadena con símbolos separados por comas.
     * @return Mapa con la cotización de cada símbolo.
     */
    public Map<String, Object> getBatchPrices(String symbols) {
        if (symbols == null || symbols.isEmpty()) return Map.of();

        Map<String, Object> resultMap = new ConcurrentHashMap<>();

        Arrays.stream(symbols.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .parallel()
                .forEach(symbol -> {
                    Double price;
                    if (symbol.contains("/")) {
                        price = getForexPrice(symbol);
                    } else if (symbol.endsWith("USDT")) {
                        price = getCryptoPrice(symbol);
                    } else {
                        price = getStockPrice(symbol);
                    }
                    Map<String, String> data = new HashMap<>();
                    data.put("price", price.toString());
                    resultMap.put(symbol, data);
                });

        return resultMap;
    }
    /** Consulta el precio de divisas mediante Exchange Rate API. */
    private Double getForexPrice(String symbol) {
        try {
            String base = symbol.split("/")[0];
            String target = symbol.split("/")[1];
            String url = "https://open.er-api.com/v6/latest/" + base;
            Map res = restTemplate.getForObject(url, Map.class);
            Map rates = (Map) res.get("rates");
            return Double.parseDouble(rates.get(target).toString());
        } catch (Exception e) { return 0.0; }
    }
    /** Consulta el precio de criptomonedas mediante Binance API. */
    private Double getCryptoPrice(String symbol) {
        try {
            String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;
            Map res = restTemplate.getForObject(url, Map.class);
            return Double.parseDouble(res.get("price").toString());
        } catch (Exception e) { return 0.0; }
    }
    /** Consulta el precio de acciones mediante Finnhub API. */
    private Double getStockPrice(String symbol) {
        try {
            String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + finnhubKey;
            Map res = restTemplate.getForObject(url, Map.class);
            return Double.parseDouble(res.get("c").toString());
        } catch (Exception e) { return 0.0; }
    }
    /** Obtiene el precio en tiempo real para un único símbolo. */
    public Double getRealTimePrice(String symbol) {
        Map<String, Object> res = getBatchPrices(symbol);
        if (res.containsKey(symbol)) {
            Map<String, String> data = (Map<String, String>) res.get(symbol);
            return Double.parseDouble(data.get("price"));
        }
        return 0.0;
    }
}