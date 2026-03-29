package com.silviagarcia.investtracking.Investment_Tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class PriceService {

    @Autowired
    private RestTemplate restTemplate;

    private final String FINNHUB_KEY = "d74n4d1r01qg1eo64d4gd74n4d1r01qg1eo64d50";

    public Map<String, Object> getBatchPrices(String symbols) {
        Map<String, Object> resultMap = new HashMap<>();
        if (symbols == null || symbols.isEmpty()) return resultMap;

        String[] symbolArray = symbols.split(",");

        for (String symbol : symbolArray) {
            Double price = 0.0;

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
        }
        return resultMap;
    }

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

    private Double getCryptoPrice(String symbol) {
        try {
            String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;
            Map res = restTemplate.getForObject(url, Map.class);
            return Double.parseDouble(res.get("price").toString());
        } catch (Exception e) { return 0.0; }
    }

    private Double getStockPrice(String symbol) {
        try {
            String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + FINNHUB_KEY;
            Map res = restTemplate.getForObject(url, Map.class);
            return Double.parseDouble(res.get("c").toString());
        } catch (Exception e) { return 0.0; }
    }

    public Double getRealTimePrice(String symbol) {
        Map<String, Object> res = getBatchPrices(symbol);
        if (res.containsKey(symbol)) {
            Map<String, String> data = (Map<String, String>) res.get(symbol);
            return Double.parseDouble(data.get("price"));
        }
        return 0.0;
    }
}